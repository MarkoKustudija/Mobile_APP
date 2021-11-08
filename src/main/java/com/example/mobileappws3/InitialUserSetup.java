package com.example.mobileappws3;

import java.util.Arrays;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.mobileappws3.repository.AuthorityRepository;
import com.example.mobileappws3.repository.RoleRepository;
import com.example.mobileappws3.repository.UserRepository;
import com.example.mobileappws3.shared.Roles;
import com.example.mobileappws3.shared.Utils;
import com.example.mobileappws3.ui.entity.AuthorityEntity;
import com.example.mobileappws3.ui.entity.RoleEntity;
import com.example.mobileappws3.ui.entity.UserEntity;

@Component
@Transactional
public class InitialUserSetup {
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Utils utils;
	
	@Autowired
	UserRepository userRepository;
	
	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("Ready to print");
		
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
		
		createRole(Roles.ROLE_USER.name(),Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(),Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
		
        if(roleAdmin == null) return;
        
        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("James");
        adminUser.setLastName("Bond");
        adminUser.setEmail("bond@gmail.com");
        adminUser.setEmailVerificationStatus(true);
//        adminUser.setEmailVerificationToken(null);
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("007"));
        adminUser.setRoles(Arrays.asList(roleAdmin));
        
//        userRepository.save(adminUser);
               
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) {		
	AuthorityEntity authority = authorityRepository.findByName(name);
	
		if(authority == null) {	
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
				
		}
		return authority;		
	}
	
	@Transactional
	private RoleEntity createRole(
			String name, Collection<AuthorityEntity> authoities) {		
		
		RoleEntity role = roleRepository.findByName(name);
		
		if(role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authoities);
			roleRepository.save(role);
		}
		
		return role;
		
	}
	
	

}
