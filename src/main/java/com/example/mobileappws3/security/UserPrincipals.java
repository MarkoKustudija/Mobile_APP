package com.example.mobileappws3.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.mobileappws3.ui.entity.AuthorityEntity;
import com.example.mobileappws3.ui.entity.RoleEntity;
import com.example.mobileappws3.ui.entity.UserEntity;

public class UserPrincipals implements UserDetails {

	
	private static final long serialVersionUID = -1307721302858205846L;
	
	
	private UserEntity userEntity;
	private String userId;

	public UserPrincipals(UserEntity userEntity) {
		this.userEntity = userEntity;
		this.setUserId(userEntity.getUserId());
	}

	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> authorities = new HashSet<>();
	    Collection<AuthorityEntity> authorityEntities = new HashSet<>();
		
		Collection< RoleEntity> roles = userEntity.getRoles();
		if(roles == null) return authorities;
		
		roles.forEach((role) -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			authorityEntities.addAll(role.getAuthorities());
			
		});
		
		authorityEntities.forEach((authorityEntity) -> {
			authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
			
		});
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return userEntity.getEncryptedPassword();
	}

	@Override
	public String getUsername() {
		return userEntity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.userEntity.getEmailVerificationStatus();
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}
