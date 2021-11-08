package com.example.mobileappws3.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mobileappws3.exception.UserServiceException;
import com.example.mobileappws3.repository.PasswordResetTokenRepository;
import com.example.mobileappws3.repository.RoleRepository;
import com.example.mobileappws3.repository.UserRepository;
import com.example.mobileappws3.security.UserPrincipals;
import com.example.mobileappws3.service.UserService;
import com.example.mobileappws3.shared.Utils;
import com.example.mobileappws3.shared.dto.AddressDto;
import com.example.mobileappws3.shared.dto.UserDto;
import com.example.mobileappws3.ui.entity.PasswordResetTokenEntity;
import com.example.mobileappws3.ui.entity.RoleEntity;
import com.example.mobileappws3.ui.entity.UserEntity;
import com.example.mobileappws3.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	Utils utils;
		
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	RoleRepository roleRepository;
	

	@Override
	public UserDto createUser(UserDto user) {
		
		if(userRepository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Record already exist!");
		
		
		for(int i=0; i<user.getAddresses().size(); i++) {
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
			
		}
		
//		UserEntity userEntity = new UserEntity();
//		BeanUtils.copyProperties(user, userEntity);
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		
		
		String publicUserId = utils.generateUserId(30);
		
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));	
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
		userEntity.setEmailVerificationStatus(false);
		
		//Set roles
		
		Collection<RoleEntity> roleEntities = new HashSet<>();
		   for(String role: user.getRoles()) {
		   
			RoleEntity roleEntity = roleRepository.findByName(role);
			if(roleEntity != null) {
				roleEntities.add(roleEntity);
			}
		
		}
		   
		   userEntity.setRoles(roleEntities);
		
		
		UserEntity storedUserDetails =  userRepository.save(userEntity);
		
//		UserDto returnValue = new UserDto();
//		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);
		
		return returnValue;
	}
	
//// LOAD USER  BY USER NAME  -> LOGIN AUTHORIZATION
//	@Override
//	public UserDetails loadUserByUsername(String email) 
//			throws UsernameNotFoundException {
//		
//		UserEntity userEntity =  userRepository.findByEmail(email);
//		
//		if(userEntity == null) 
//			throw new UsernameNotFoundException(email);
//				
//		return new UserPrincipal(userEntity);
	
//	}
	
	@Override
	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
		return new UserPrincipals(userEntity );
		
//		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), 
//				userEntity.getEmailVerificationStatus(),
//				true, true,
//				true, new ArrayList<>());

//		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	

	@Override
	public UserDto getUser(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		
		
		return returnValue;
		
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		// userRepository allways return back UserEntity
		
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null)
			throw new UsernameNotFoundException("User with id: " + userId + " not found");		
		
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
		
	
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) 
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		
		UserEntity  updatedUserDetails = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails, returnValue);
		
		
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) 
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
	    userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		List<UserDto> returnValue = new ArrayList<>();
		
		if(page>0) page = page-1;
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		Page<UserEntity> usersPage =  userRepository.findAll(pageableRequest);
		
		List<UserEntity> users = usersPage.getContent();
		
		for(UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		
		return returnValue;
	}

	@Override
	public boolean verifyEmailToke(String token) {

		boolean returnValue = false;
		
		// Find user by token
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);
		
		if(userEntity != null) {
			boolean hasTokenExpired = Utils.hasTokenExpired(token);
			if(!hasTokenExpired) {
				userEntity.setEmailVerificationToken(null); 
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				returnValue = true;
			}
		}
		
		return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		
		boolean returnValue = false;
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) {
			 return returnValue;
		}
		
		
     String token = new Utils().generatePasswordResetToken(userEntity.getUserId());
		
		PasswordResetTokenEntity passwordResetTokenEntity =  new PasswordResetTokenEntity();
		passwordResetTokenEntity.setToken(token);
		passwordResetTokenEntity.setUserDetails(userEntity);
		passwordResetTokenRepository.save(passwordResetTokenEntity);
		
//		returnValue = new AmazonSES().sendPasswordResetRequest(
//               userEntity.getFirstName(),
//               userEntity.getEmail(),
//               token);
		
		
		
		return returnValue;
	}





}
