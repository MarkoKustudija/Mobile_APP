package com.example.mobileappws3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.mobileappws3.ui.entity.PasswordResetTokenEntity;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{

	

}
