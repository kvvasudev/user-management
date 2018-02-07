package com.mastek.poc.controller;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mastek.poc.exception.UserManagementException;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.persistence.OrganisationRepository;
import com.mastek.poc.persistence.UserRepository;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private OrganisationRepository organisationRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userRepository.findOne(userId);
    }

    @PostMapping
    public ResponseEntity<Object> newUser(@Valid @RequestBody User user) {
    	
    	if(organisationRepository.findOne(user.getOrganisation().getId()) == null) {
    		throw new UserManagementException(String.format("OrgId %s not found", user.getOrganisation().getId()));
    	}
        
    	User existingUser = userRepository.checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail());
    	if(existingUser!=null) {
    	    throw new UserManagementException(String.format("Email address %s already registered in Organisation", user.getEmail()));
        }
    	User savedUser = userRepository.save(user);
    	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{orgId}")
    			.buildAndExpand(savedUser.getId()).toUri();

    	return ResponseEntity.created(location).build();
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> editUser(@Valid @RequestBody User user, @PathVariable Long userId) {
    	User existingUser = userRepository.findOne(userId);

    	if (existingUser == null) {
    		return ResponseEntity.notFound().build();
    	}
    	user.setId(userId);
    	userRepository.save(user);

    	return ResponseEntity.noContent().build();
    }
    
	@GetMapping("/{userId}/organisation")
	public Organisation retrieveOrganisationForUser(@PathVariable Long userId) {
		return userRepository.retrieveOrganisation(userId);
	}
}