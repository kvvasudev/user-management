package com.mastek.poc.manager;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mastek.poc.exception.UserManagementException;
import com.mastek.poc.model.Group;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.persistence.GroupRepository;
import com.mastek.poc.persistence.OrganisationRepository;
import com.mastek.poc.persistence.UserRepository;

@Service
public class UserManager {
	
	private static Logger logger = LoggerFactory.getLogger(UserManager.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private OrganisationRepository organisationRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Transactional
	public ResponseEntity<Object> processUserCreationOrUpdation(User user, boolean createUser, boolean updateUser){
		logger.info("User To be Processed: "+user);
    	Organisation org = null;
    	if(org==null && user.getOrganisation().getId() != null) {
    		org = organisationRepository.findOne(user.getOrganisation().getId());
    	} 
    	if(org==null && user.getOrganisation().getName() != null) {
    	    org = organisationRepository.findByName(user.getOrganisation().getName());
    	} 
    	if(org != null) {
    		user.getOrganisation().setId(org.getId());
    		user.getOrganisation().setName(org.getName());
    	} else {
    		throw new UserManagementException(String.format("No Organisation found for orgId %s or orgName %s", user.getOrganisation().getId(), user.getOrganisation().getName()));
    	}
    	
    	User existingUser = userRepository.checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail());
    	if(existingUser!=null && createUser) {
    	    throw new UserManagementException(String.format("Email address %s already registered for Organisation %s", user.getEmail(), org.getName()));
        }
    	
    	if(updateUser && existingUser.getId() != user.getId()) {
    		throw new UserManagementException(String.format("Email address %s already registered for Organisation %s", user.getEmail(), org.getName()));
    	}
    	    	
    	Set<Group> groupSet = new HashSet<Group>(); 
    	for(Group group : user.getGroups()) {
    		Group groupToAdd = null;
    		if(groupToAdd ==null && group.getId() != null) {
    			groupToAdd = groupRepository.findOne(group.getId());
    		}
    		if(groupToAdd ==null && group.getName() != null) {
    			groupToAdd = groupRepository.findByName(group.getName());
    		}
    		if(groupToAdd != null) {
    			groupSet.add(groupToAdd);
    		}else {
    			groupSet.add(group);
    		}
    			
    	}
    	
    	user.setOrganisation(org);
    	user.setGroups(groupSet);
    	
    	logger.info("User To be Saved: "+user);
    	User savedUser = userRepository.save(user);
    	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
    			.buildAndExpand(savedUser.getId()).toUri();

    	return ResponseEntity.created(location).build();
    }
}
