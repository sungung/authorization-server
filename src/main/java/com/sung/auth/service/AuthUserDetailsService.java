package com.sung.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sung.auth.model.User;
import com.sung.auth.repository.UserRepository;

@Service
public class AuthUserDetailsService implements UserDetailsService {

	private final static Logger logger = LoggerFactory.getLogger(AuthUserDetailsService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameIgnoreCase(username);
		if (user == null){
			throw new UsernameNotFoundException(username);
		}
		return new AuthUserDetails(user);
	}
	
	public User save(User user){
		return save(user, true);
	}
	
	public User save(User user, boolean withNewPassword) {
		if (!withNewPassword) {
			User saved = userRepository.getOne(user.getId());
			user.setPassword(saved.getPassword());			
		}
		return userRepository.save(user);
	}
	
	public User findOne(String username) {
		return userRepository.findByUsernameIgnoreCase(username);
	}

}
