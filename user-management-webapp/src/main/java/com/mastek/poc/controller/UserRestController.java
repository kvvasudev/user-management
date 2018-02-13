package com.mastek.poc.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mastek.poc.manager.UserManager;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.persistence.UserRepository;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private UserManager userManager;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userRepository.findOne(userId);
    }
    
	@GetMapping("/{userId}/organisation")
	public Organisation retrieveOrganisationForUser(@PathVariable Long userId) {
		return userRepository.retrieveOrganisation(userId);
	}

    @PostMapping
    public ResponseEntity<Object> newUser(@Valid @RequestBody User user) {
    	return userManager.processUserCreationOrUpdation(user, true, false);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> editUser(@Valid @RequestBody User user, @PathVariable Long userId) {
    	User existingUser = userRepository.findOne(userId);
    	if (existingUser == null) {
    		return ResponseEntity.notFound().build();
    	}
    	user.setId(userId);
    	return userManager.processUserCreationOrUpdation(user, false, true);
    }
    
}