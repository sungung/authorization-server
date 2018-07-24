package com.sung.auth.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


@Entity
@Table(name = "app_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;
	
	@Column(name = "password_hint")
	private String passwordHint;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "phone_number")
	private String phoneNumber;	
	
    @Column(name = "account_enabled")
    private boolean enabled = false;
    
    @Column(name = "account_expired", nullable = false)
    private boolean accountExpired = false;
    
    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked = false;
    
    @Column(name = "credentials_expired", nullable = false)
    private boolean credentialsExpired = false;	
    
    @Column(name = "require_reset_passwd", nullable = false)
    private boolean requireResetPassword = false;    
    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_role", 
    	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<Role>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_business_unit",
    	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    	inverseJoinColumns = @JoinColumn(name = "business_unit_id", referencedColumnName = "id"))
    private Set<BusinessUnit> businessUnits = new HashSet<BusinessUnit>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordHint() {
		return passwordHint;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public boolean isRequireResetPassword() {
		return requireResetPassword;
	}

	public void setRequireResetPassword(boolean requireResetPassword) {
		this.requireResetPassword = requireResetPassword;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<BusinessUnit> getBusinessUnits() {
		return businessUnits;
	}

	public void setBusinessUnits(Set<BusinessUnit> businessUnits) {
		this.businessUnits = businessUnits;
	}    

}
