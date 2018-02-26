package com.mastek.poc.manager;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Calendar;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.mastek.poc.TestConfig;
import com.mastek.poc.exception.UserManagementException;
import com.mastek.poc.model.Group;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.persistence.GroupRepository;
import com.mastek.poc.persistence.OrganisationRepository;
import com.mastek.poc.persistence.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class UserManagerTest {
	
	@Autowired
	private UserManager userManager;

	@MockBean
	private OrganisationRepository orgRepository;
    
	@MockBean
	private UserRepository userRepository;
    
	@MockBean
	private GroupRepository groupRepository;
	
	@Test(expected = UserManagementException.class)
	public void testProcessUserCreationFailureWithNoOrg() {
		User user = populateUser(false, true, false);
		when(orgRepository.findOne(1l)).thenReturn(null);
		when(orgRepository.findByName("MASTEK")).thenReturn(null);
		userManager.processUserCreationOrUpdation(user, true, false);
		verify(orgRepository, times(1)).findOne(1l);
		verify(orgRepository, times(1)).findByName("MASTEK");
	    verifyNoMoreInteractions(orgRepository);
	}
	
	@Test(expected = UserManagementException.class)
	public void testProcessUserCreationFailureWithExistingEmail() {
		User user = populateUser(false, true, false);
		User anotherUser = populateUser(true, true, true);
		when(orgRepository.findOne(1l)).thenReturn(null);
		when(orgRepository.findByName("MASTEK")).thenReturn(user.getOrganisation());
		when(userRepository.checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail())).thenReturn(anotherUser);
		userManager.processUserCreationOrUpdation(user, true, false);
		verify(orgRepository, times(1)).findOne(1l);
		verify(orgRepository, times(1)).findByName("MASTEK");
	    verifyNoMoreInteractions(orgRepository);
	    verify(userRepository, times(1)).checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail());
	    verifyNoMoreInteractions(userRepository);
	}
	
	@Test(expected = UserManagementException.class)
	public void testProcessUserUpdationFailureWithExistingEmail() {
		User user = populateUser(true, true, true);
		User anotherUser = populateUser(true, true, true);
		anotherUser.setId(2l);
		when(orgRepository.findOne(1l)).thenReturn(user.getOrganisation());
		when(userRepository.checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail())).thenReturn(anotherUser);
		userManager.processUserCreationOrUpdation(user, false, true);
		verify(orgRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(orgRepository);
	    verify(userRepository, times(1)).checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail());
	    verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void testProcessUserCreationSuccess() {
		User user = populateUser(false, true, true);
		User createdUser = populateUser(true, true, true);
		when(orgRepository.findOne(1l)).thenReturn(user.getOrganisation());
		when(userRepository.checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail())).thenReturn(null);
		when(groupRepository.findOne(1l)).thenReturn(null);
		when(groupRepository.findByName("Admin")).thenReturn(user.getGroups().iterator().next());
		when(groupRepository.findOne(2l)).thenReturn(null);
		when(groupRepository.findByName("Management")).thenReturn(null);
		when(userRepository.save(user)).thenReturn(createdUser);
		ResponseEntity<Object> responseEntity = userManager.processUserCreationOrUpdation(user, true, false);
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		verify(orgRepository, times(1)).findOne(1l);
		verifyNoMoreInteractions(orgRepository);
		verify(userRepository, times(1)).checkUniqueUserEmailInOrganisation(user.getOrganisation(), user.getEmail());
		verify(groupRepository, times(1)).findOne(1l);
		verify(groupRepository, times(1)).findOne(2l);
		verify(groupRepository, times(1)).findByName("Admin");
		verify(groupRepository, times(1)).findByName("Management");
		verify(userRepository, times(1)).save(user);
		verifyNoMoreInteractions(groupRepository);
		verifyNoMoreInteractions(userRepository);
	}
	
	public User populateUser(boolean setUserId, boolean setOrgId, boolean setGroupId) {
		Organisation org = populateOrganisation(setOrgId);
        Calendar cal = Calendar.getInstance();
        cal.set(1988, 10, 28);
		User user = new User("Vasu Kaveri", "vasu.kaveri@mastek.com", new java.sql.Date(cal.getTime().getTime()), org);
		if(setUserId) {
		    user.setId(1l);
		}
		Group adminGroup = new Group();
    	adminGroup.setName("Admin");
    	if(setGroupId) {
    	    adminGroup.setId(1l);
    	}
    	Group mgmtGroup = new Group();
    	mgmtGroup.setName("Management");
    	if(setGroupId) {
    	    mgmtGroup.setId(2l);
    	}
    	user.setGroups(new HashSet<Group>(){{add(adminGroup);}{add(mgmtGroup);}});
    	return user;
	}
	
	public Organisation populateOrganisation(boolean setIds) {
		Organisation org = new Organisation();
		if(setIds) {
		    org.setId(1l);
		}
		org.setName("MASTEK");
		org.setAddress("Reading, UK");
		return org;
	}
    
}
