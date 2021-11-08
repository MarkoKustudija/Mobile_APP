package com.example.mobileappws3.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobileappws3.exception.UserServiceException;
import com.example.mobileappws3.service.AddressService;
import com.example.mobileappws3.service.UserService;
import com.example.mobileappws3.shared.Roles;
import com.example.mobileappws3.shared.dto.AddressDto;
import com.example.mobileappws3.shared.dto.UserDto;
import com.example.mobileappws3.ui.model.request.PasswordResetRequestModel;
import com.example.mobileappws3.ui.model.request.UserDetailsRequestModel;
import com.example.mobileappws3.ui.model.response.AddressRest;
import com.example.mobileappws3.ui.model.response.ErrorMessages;
import com.example.mobileappws3.ui.model.response.OperationStatusModel;
import com.example.mobileappws3.ui.model.response.RequestOperationName;
import com.example.mobileappws3.ui.model.response.RequestOperationStatus;
import com.example.mobileappws3.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	
	////// GET USER BY ID ////////
	
	@PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
	@ApiOperation(value="The Get User Details Web Service Endpoint",
			notes="${userController.GetUser.ApiOperation.Notes}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public UserRest getUser(@PathVariable String id) { // mora biti identicna vrednost kao u path

		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);

//		ModelMapper modelMapper = new ModelMapper();
//		returnValue = modelMapper.map(userDto, UserRest.class);
     	BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}
	
	///////////////////   PRETRAGE //////////////////////////////
	
	//http://localhost:8080/mobile-app-ws/users/user_id/addresses
	@GetMapping(path = "/{id}/addresses", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<AddressRest> getUserAddresses(@PathVariable String id) {

		List<AddressRest>  returnValue = new ArrayList<>();

		List<AddressDto> addressDto = addressService.getAddresses(id);
		
		if (addressDto != null && !addressDto.isEmpty()) {

			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressDto, listType);
		}

		return returnValue;
	}
	
	
	//http://localhost:8080/mobile-app-ws/users/user_id/addresses/address_id
	@GetMapping(path = "/{userId}/addresses/{addressId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public AddressRest getUserAddress(@PathVariable String addressId) {

		AddressDto addressDto = addressService.getAddress(addressId);

		
        ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(addressDto, AddressRest.class);

		
	}
	
	//http://localhost:8080/mobile-app-ws/users/email-verification?token=sdafgd
	@GetMapping(path = "/email-verification", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public OperationStatusModel verifyEmailToke(@RequestParam (value = "token") String token) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		
		boolean isVerified = userService.verifyEmailToke(token);
		
		if(isVerified) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		} else {
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		}
		
		return returnValue;
		
		
	}
	
	///// RESET PASSWORD //////
	
	@PostMapping(path = "/password-reset-request",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})

		public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
	   
		returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		if(operationResult) {
		 returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		
		return returnValue;
	
	}
	
	
	////// GET ALL USERS ////////
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getUsers(@RequestParam (value = "page", defaultValue = "0") int page,
			@RequestParam (value = "limit", defaultValue = "25") int limit) {
		
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page,limit); 
		
		for(UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
		
	}
	
////  CREATE NEW USER ///////
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE})	
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{

		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
//		if(userDetails.getFirstName().isEmpty()) throw new NullPointerException("The object is null");
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));
		
		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);
//		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;

	}
  ///// UPADATE //////
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@PutMapping(path = "/{id}",
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})		
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto(); 
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;

		
		
	}
	
	//// DELETE /////
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
//	@Secured("ROLE_ADMIN")
	@DeleteMapping(path ="/{id}",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})		
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue; 
	}

}
