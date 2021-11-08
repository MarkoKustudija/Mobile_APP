package com.example.mobileappws3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mobileappws3.repository.AddressRepository;
import com.example.mobileappws3.repository.UserRepository;
import com.example.mobileappws3.service.AddressService;
import com.example.mobileappws3.shared.dto.AddressDto;
import com.example.mobileappws3.ui.entity.AddressEntity;
import com.example.mobileappws3.ui.entity.UserEntity;


@Service
public class AddressServiceImpl  implements AddressService{
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String userId) {
		
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		 
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for (AddressEntity addressEntity : addresses) {
		returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		 	 
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		
		AddressDto returnValue = null;
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity != null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		return returnValue;
	}

	
	
	

}
