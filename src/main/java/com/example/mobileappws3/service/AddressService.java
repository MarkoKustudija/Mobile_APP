package com.example.mobileappws3.service;

import java.util.List;

import com.example.mobileappws3.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getAddresses(String userId);

	AddressDto getAddress(String addressId);
	
	
	
	


}
