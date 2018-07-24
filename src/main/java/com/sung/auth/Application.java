package com.sung.auth;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sung.auth.model.User;
import com.sung.auth.service.AuthUserDetails;


/**
 * 
 * Get token
 * =========
 * $ curl -X POST -d 'grant_type=password&username=user&password=secret' --user fooClientIdPassword:secret localhost:18081/oauth/token
 * {"access_token":"e1df6cb7-5adf-475c-be3c-8d27704ba072","token_type":"bearer","refresh_token":"ee9ff153-39c2-4396-870e-204fc059ba29","expires_in":3599,"scope":"read write"}
 * 
 * 
 * Refresh token
 * =============
 * $ curl -X POST -d 'refresh_token=ee9ff153-39c2-4396-870e-204fc059ba29&grant_type=refresh_token' --user fooClientIdPassword:secret localhost:18081/oauth/token
 * {"access_token":"649f1a23-62ad-4b96-aa29-91764906e6de","token_type":"bearer","refresh_token":"ee9ff153-39c2-4396-870e-204fc059ba29","expires_in":3599,"scope":"read write"}
 *
 *
 * Access resource
 * ===============
 * $ curl -H "Authorization: bearer 649f1a23-62ad-4b96-aa29-91764906e6de" localhost:18081/me
 * {"name":"user","organization":"White House"}
 * 
 */
@SpringBootApplication
@RestController
public class Application {
	
	private static Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServiceConfiguration extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
		}		
	}
	

}
