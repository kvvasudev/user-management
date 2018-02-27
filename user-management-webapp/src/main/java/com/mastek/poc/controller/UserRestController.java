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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
@Api(value="Rest Operations for managing Users")
public class UserRestController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private UserManager userManager;
	
    @GetMapping
    @ApiOperation(value = "View a list of available users", response = List.class)
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/{userId}")
    @ApiOperation(value = "List an User with particular userId", response = User.class)
    public User getUser(@PathVariable Long userId) {
        return userRepository.findOne(userId);
    }
    
	@GetMapping("/{userId}/organisation")
	@ApiOperation(value = "Lists an organisation of a particular User with userId", response = User.class)
	public Organisation retrieveOrganisationForUser(@PathVariable Long userId) {
		return userRepository.retrieveOrganisation(userId);
	}
	
    @PostMapping
    @ApiOperation(value = "Add a new User", response = ResponseEntity.class)
    public ResponseEntity<Object> newUser(@Valid @RequestBody User user) {
    	return userManager.processUserCreationOrUpdation(user, true, false);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit an User with a particular userId", response = ResponseEntity.class)
    public ResponseEntity<Object> editUser(@Valid @RequestBody User user, @PathVariable Long userId) {
    	User existingUser = userRepository.findOne(userId);
    	if (existingUser == null) {
    		return ResponseEntity.notFound().build();
    	}
    	user.setId(userId);
    	return userManager.processUserCreationOrUpdation(user, false, true);
    }
    
}