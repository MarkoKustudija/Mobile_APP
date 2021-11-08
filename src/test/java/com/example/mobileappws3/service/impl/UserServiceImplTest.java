package com.example.mobileappws3.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.mobileappws3.exception.UserServiceException;
import com.example.mobileappws3.repository.UserRepository;
import com.example.mobileappws3.shared.Utils;
import com.example.mobileappws3.shared.dto.AddressDto;
import com.example.mobileappws3.shared.dto.UserDto;
import com.example.mobileappws3.ui.entity.AddressEntity;
import com.example.mobileappws3.ui.entity.UserEntity;


@SpringBootTest
class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	String userId = "k1l3kk49";
	String encryptedPassword = "dkamd899admnnda";
	
	UserEntity userEntity;
	
	
	@BeforeEach
	void setUp() throws Exception {
		
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Marko");
		userEntity.setLastName("Kustudija");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("jda02023");
		userEntity.setAddresses(getAddressEntity());
		
	}
	

	@Test
	final void testGetUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser("test@test.com");
		assertNotNull(userDto);
		assertEquals("Marko", userDto.getFirstName());

	}
	
	@Test
	final void testGetUser_UserNamenotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});

	}
	
	@Test
	final void testCreateUser_createUserServiceException() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Marko");
		userDto.setLastName("Kustudija");
		userDto.setEmail("test@test.com");
		userDto.setPassword("amlaml33m4l");
	
		assertThrows(UserServiceException.class, () -> {
			userService.createUser(userDto);
		});

	}
	
	@Test
	final void testCreateUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("jdja8991mdka");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
				
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		
		userDto.setFirstName("Marko");
		userDto.setLastName("Kustudija");
		userDto.setEmail("test@test.com");
		userDto.setPassword("amlaml33m4l");
		
		UserDto storedUserDetails = userService.createUser(userDto);
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("amlaml33m4l");
		verify(userRepository, times(1)).save(any(UserEntity.class));
        
	}
	
	private List<AddressDto> getAddressesDto() {

		AddressDto addressDto = new AddressDto();
		addressDto.setCountry("Canada");
		addressDto.setCity("Vancuver");
		addressDto.setPostalCode("21000");
		addressDto.setStreetName("Stojana Novakovica 29");
		addressDto.setType("shiping");

		AddressDto billingAddress = new AddressDto();
		billingAddress.setCountry("Canada");
		billingAddress.setCity("Vancuver");
		billingAddress.setPostalCode("21000");
		billingAddress.setStreetName("Stojana Novakovica 29");
		billingAddress.setType("billing");

		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddress);

		return addresses;

	}
	
	private List<AddressEntity> getAddressEntity() {

		List<AddressDto> addresses = getAddressesDto();

		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		return new ModelMapper().map(addresses, listType);

	}
	

	
	
	


}
