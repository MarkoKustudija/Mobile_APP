package com.example.mobileappws3.io.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.mobileappws3.repository.UserRepository;
import com.example.mobileappws3.ui.entity.AddressEntity;
import com.example.mobileappws3.ui.entity.UserEntity;



@ExtendWith(SpringExtension.class)
//@ContextConfiguration
@SpringBootTest
class UserRepositoryTest {

	
		@Autowired
		UserRepository userRepository;
		
		static boolean recordsCreated = false;
		
		
		@BeforeEach
		void setUp() throws Exception{
//			if(!recordsCreated) createRecords();
			
		}
		
//		private void createRecords(){
//			
//		//preper UserEntity
//			
//			UserEntity userEntity = new UserEntity();
//			userEntity.setFirstName("James");
//			userEntity.setLastName("Bond");
//			userEntity.setUserId("12@k#1");
//			userEntity.setEncryptedPassword("007");
//			userEntity.setEmail("test@test.com");
//			userEntity.setEmailVerificationStatus(true);
//			
//			// Address entity
//			
//			AddressEntity addressEntity = new AddressEntity();
//			addressEntity.setType("shipping");
//			addressEntity.setAddressId("adkdka2394aks");
//	        addressEntity.setCity("Novi Sad");
//	        addressEntity.setCountry("Serbia");
//	        addressEntity.setPostalCode("21000");
//	        addressEntity.setStreetName("Bul Jovana Ducica");
//	        
//	        List<AddressEntity> adddresses = new ArrayList <>();
//	        adddresses.add(addressEntity);
//	        userEntity.setAddresses(adddresses);
//	        
//	        userRepository.save(userEntity);
//	        
//	        /// 2nd UserEntity
//	    	UserEntity userEntity2 = new UserEntity();
//			userEntity2.setFirstName("Pera");
//			userEntity2.setLastName("Peric");
//			userEntity2.setUserId("12@k#123");
//			userEntity2.setEncryptedPassword("xxx");
//			userEntity2.setEmail("test199@test.com");
//			userEntity2.setEmailVerificationStatus(true);
//			
//			//// 2nd Address Entity 
//			AddressEntity addressEntity2 = new AddressEntity();
//			addressEntity2.setType("shipping");
//			addressEntity2.setAddressId("adkdka2394lll");
//	        addressEntity2.setCity("Novi Sad");
//	        addressEntity2.setCountry("Serbia");
//	        addressEntity2.setPostalCode("21000");
//	        addressEntity2.setStreetName("Bul Jovana Ducica");
//			
//			
//	        
//	        List<AddressEntity> adddresses2 = new ArrayList <>();
//	        adddresses2.add(addressEntity2);
//	        userEntity.setAddresses(adddresses2);
//	        
//	        userRepository.save(userEntity2);
//	        
//	        recordsCreated = true;
//	        
//		}
	        
		    
		    	
		@Test
		final void testfindUserEntityByUserId() {
			
			String userId = "12@k#1";
			UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
			
			assertNotNull(userEntity);
			assertTrue(userEntity.getUserId().equals(userId));
			
		}
		
		
		@Test
		final void testGetVerifedUsers() {
			Pageable pageableRequest = PageRequest.of(0,10);
			Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
			assertNotNull(pages);
			
			List<UserEntity> userEntities = pages.getContent();
			assertNotNull(userEntities);
			assertTrue(userEntities.size() == 2);
			
		}
		
		@Test
		final void testFindUserByFirstName() {
			String firstName = "James";
			List<UserEntity> users =  userRepository.findUserByFirstName(firstName);
			assertNotNull(users);
			assertTrue(users.size() == 1);
			
			UserEntity user = users.get(0);
			assertTrue(user.getFirstName().equals(firstName));
			
		}
		
		@Test
		final void testGetUserEntityFullNameById() {
			String userId = "12@k#1";
			List<Object []> records = userRepository.getUserEntityFullNameById(userId);
			
			assertNotNull(records);
			assertTrue(records.size()==1);
			
			Object[] userDetails = records.get(0);
			
			String firstName = String.valueOf(userDetails[0]);
			String lastName = String.valueOf(userDetails[1]);
			
			assertNotNull(firstName);
			assertNotNull(lastName);
			
		}
		
//		@Test
//		final void testUpdateUserEntityEmailVerificationStatus() {
//			
//			boolean newEmailVerificationStatus = false;
//			
//			userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "12@k#1");
//			
//			UserEntity storedUserDetails =  userRepository.findByUserId("12@k#1");
//			
//			boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
//			
//			assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
//		}

	}


