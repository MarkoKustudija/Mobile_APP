package com.example.mobileappws3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.mobileappws3.ui.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

	
	//// prva 3 metoda su:  Spring Data JPA Query metode (findBy....)  /////
	
	UserEntity findByEmail(String email);
	
	UserEntity findByUserId(String userId);

	UserEntity findUserByEmailVerificationToken(String token);
	
	/// Native SQL queries
		
	@Query(value = "select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
			countQuery = "select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);
	
	@Query(value = "select * from Users u where u.first_name = ?1",
			nativeQuery = true)
	List<UserEntity> findUserByFirstName(String firstName);
	
//	@Transactional
//	@Modifying
//	@Query(value = "update users u set u.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus where u.user_id = :userId",
//			nativeQuery = true)
//	void updateUserEmailVerificationStatus(@Param ("emailVerificationStatus")boolean emailVerificationStatus, 
//			@Param ("userId") String userId); 
//	
	
	
	/// JPQL  queries -   java persistence query language ////
	
	
	@Query("select user from UserEntity user where user.userId =:userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
	@Query("select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
	List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);
	
	
//	@Transactional
//	@Modifying
//	@Query("update UserEntity u set u.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus where u.userId = :userId")
//	void updateUserEntityEmailVerificationStatus(@Param ("emailVerificationStatus")boolean emailVerificationStatus, 
//			@Param ("userId") String userId); 
	
	
	
}



