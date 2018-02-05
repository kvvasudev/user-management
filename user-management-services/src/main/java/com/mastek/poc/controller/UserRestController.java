package com.mastek.poc.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.persistence.UserRepository;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public void newUser(User user) {
    	userRepository.save(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void editExam(User user) {
    	userRepository.save(user);
    }
    
	@GetMapping("/users/{userId}/organisation")
	public Organisation retrieveOrganisationForUser(@PathVariable Long userId) {
		return userRepository.retrieveOrganisation(userId);
	}
}