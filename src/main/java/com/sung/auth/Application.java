package com.sung.auth;

import java.security.Principal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 
 * Get token
 * =========
 * $ curl -X POST -d 'grant_type=password&username=openport@vict.com.au&password=webbdock78' --user openport:secret localhost:18081/oauth/token
 * {"access_token":"e1df6cb7-5adf-475c-be3c-8d27704ba072","token_type":"bearer","refresh_token":"ee9ff153-39c2-4396-870e-204fc059ba29","expires_in":3599,"scope":"read write"}
 * 
 * 
 * Refresh token
 * =============
 * $ curl -X POST -d 'refresh_token=ee9ff153-39c2-4396-870e-204fc059ba29&grant_type=refresh_token' --user openport:secret localhost:18081/oauth/token
 * {"access_token":"649f1a23-62ad-4b96-aa29-91764906e6de","token_type":"bearer","refresh_token":"ee9ff153-39c2-4396-870e-204fc059ba29","expires_in":3599,"scope":"read write"}
 *
 *
 * Access resource
 * ===============
 * $ curl -H "Authorization: bearer ebe2eb70-72ea-44fe-bf91-b0efc145926f" localhost:18081/secret/me
 * {"name":"user","organization":"White House"}
 * 
 * 
 * With CORS
 * =========
 * $ curl -H "Origin: https://www.vict.com.au" \
 * > -H "Access-Control-Request-Method: GET" \
 * > -H "Access-Control-Request-Headers: X-Requested-With" \
 * > -H "Authorization: bearer 21780109-290e-43a7-bd56-fa7836ffd2db" \
 * > -X OPTIONS localhost:18081/secret/me -I
 * 
 * 
 * 
 * curl -X DELETE --user openport:secret localhost:18081/oauth/token/ebe2eb70-72ea-44fe-bf91-b0efc145926f
 * 
 */
@SpringBootApplication
@RestController
public class Application {
	
	private static Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	ConsumerTokenServices tokenServices; 
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	    @Override
	    protected MethodSecurityExpressionHandler createExpressionHandler() {
	        return new OAuth2MethodSecurityExpressionHandler();
	    }
	}	
	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServiceConfiguration extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
			.cors().and()
			.antMatcher("/secret/**").authorizeRequests()		
			.antMatchers("/secret/open/**").permitAll()
			.anyRequest().authenticated();
		}		
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource(){
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("https://www.vict.com.au"));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}	
	
	/*
	 * Unable to find clean way to add deletion of token when user logging out from UI.
	 * Add URL into resource server to remove a token from repository
	 */
	@DeleteMapping("/secret/oauth/token/{token}")
	public ResponseEntity<Void> revokeToken(@PathVariable String token, Principal principal) throws Exception {
		//checkResourceOwner(user, principal);
		if (tokenServices.revokeToken(token)) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	private void checkResourceOwner(String user, Principal principal) {
		if (principal instanceof OAuth2Authentication) {
			OAuth2Authentication authentication = (OAuth2Authentication) principal;
			if (!authentication.isClientOnly() && !user.equals(principal.getName())) {
				throw new AccessDeniedException(String.format("User '%s' cannot obtain tokens for user '%s'", principal.getName(), user));
			}
		}
	}
	
}
