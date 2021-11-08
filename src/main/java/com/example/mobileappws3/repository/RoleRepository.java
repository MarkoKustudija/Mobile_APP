package com.example.mobileappws3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.mobileappws3.ui.entity.RoleEntity;


@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
	
	RoleEntity findByName(String name);
	
	
	
	
	
	

}
