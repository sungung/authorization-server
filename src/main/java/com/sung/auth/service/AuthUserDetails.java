package com.sung.auth.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sung.auth.model.User;

public class AuthUserDetails implements UserDetails {
	
	private User user;
	
	public AuthUserDetails(User user) {
		this.user = user;
	}
	
	public static class AuthRole implements GrantedAuthority{		
		private String authority;		
		public AuthRole(String authority) {
			this.authority = authority;
		}
		@Override
		public String getAuthority() {
			return authority;
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles().stream().map(role -> new AuthRole(role.getName())).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return !user.isAccountExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !user.isAccountLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return !user.isCredentialsExpired();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}

	public User getUser() {
		return user;
	}

	
}
