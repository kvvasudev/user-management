package com.mastek.poc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mastek.poc.exception.UserManagementException;
import com.mastek.poc.model.User;
import com.mastek.poc.model.UserCredentials;
import com.mastek.poc.persistence.UserCredentialsRepository;
import com.mastek.poc.persistence.UserRepository;

@RestController
@RequestMapping("/user")
public class UserRegistrationController {
	@Autowired
	private UserCredentialsRepository userCredRepository;
	
	@Autowired
	private UserRepository userRepository;
	
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserCredentials userCred) {
    	if(userCredRepository.findUserCredentialsByUser(userCred.getUser()) != null) {
    		throw new UserManagementException(String.format("User is already registered %s", userCred.getUser()));
    	}
    	if(userRepository.findOne(userCred.getUser().getId()) == null){
    		throw new UserManagementException(String.format("No Valid User with id %s exists", userCred.getUser().getId()));
    	}
    	if(userCredRepository.findByLoginName(userCred.getLoginName())!=null) {
    		throw new UserManagementException(String.format("Login Name %s already used", userCred.getLoginName()));
    	}
    	
    	userCredRepository.save(userCred);
    	return ResponseEntity.noContent().build();
    }
    
	@PostMapping("/authenticate")
	public ResponseEntity<Object> authenticateUser(@Valid @RequestBody UserCredentials userCred) {
		User user = null;
		user = userCredRepository.authenticateUser(userCred.getLoginName(), userCred.getPassword());
		if(user == null) {
			throw new UserManagementException("Not a valid LoginName / Password combination");
		}else {
		    return ResponseEntity.ok(user);
		}
	}
    
}
