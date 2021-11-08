package com.example.mobileappws3.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.mobileappws3.ui.entity.AddressEntity;
import com.example.mobileappws3.ui.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long> {

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	AddressEntity findByAddressId(String addressId);
	
	

}
