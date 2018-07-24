package com.sung.auth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sung.auth.service.AuthUserDetailsService;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AuthUserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private DaoAuthenticationProvider authenticationProvider;
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider(){
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder);
		return authProvider;
	}
	
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}	
}
