package com.sung.auth.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sung.auth.model.User;
import com.sung.auth.service.AuthUserDetails;
import com.sung.auth.service.AuthUserDetailsService;

@RestController
public class AuthController {
	
	@Autowired
	private AuthUserDetailsService userDetailsService;
	
	@RequestMapping("/me")
	public Map<String, Object> user(Authentication authentication){
		Map<String, Object> map = new LinkedHashMap<>();
		UserDetails user = (AuthUserDetails)authentication.getPrincipal();
		map.put("name", authentication.getName());
		map.put("user", user);		
		return map;
	}	
	
	@GetMapping("/user/{username}")
	public User getUser(String username){
		return userDetailsService.findOne(username);
	}
	
	@PostMapping("/user")
	public User createUser(User user){
		return userDetailsService.save(user, true);
	}
	
	@PutMapping("/user/{username}")
	public User updateUser(String username, User user){
		return userDetailsService.save(user, false);
	}
	
	@PutMapping("/user/{username}/password")
	public User resetPassword(String username, Map payload) throws Exception{
		User user = userDetailsService.findOne(username);
		if (user != null) {
			if (!user.getPassword().equals(payload.get("OLD_PASSWD"))) {
				throw new Exception("Old password not matched");
			}
			user.setPassword((String)payload.get("NEW_PASSWD"));			
		} else {
			throw new Exception("User not found");
		}
		return userDetailsService.save(user, true);
	}
}
