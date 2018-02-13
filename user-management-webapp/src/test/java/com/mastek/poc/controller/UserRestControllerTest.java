package com.mastek.poc.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastek.poc.manager.UserManager;
import com.mastek.poc.model.Group;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.persistence.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private UserManager userManager;

	@Test
	public void getUsersTest() throws Exception {
        
		List<User> userList = new ArrayList<User>();
		User user = populateUser(true);
		userList.add(user);
		
		String response = "{\"id\":1,\"name\":\"Vasu Kaveri\",\"email\":\"vasu.kaveri@mastek.com\",\"dob\":\"1988-11-28\"";
	    when(userRepository.findAll()).thenReturn(userList);
	    this.mockMvc.perform(get("/users")).andDo(print()).andExpect(status().isOk())
	                .andExpect(content().string(containsString(response)));
	    verify(userRepository, times(1)).findAll();
	    verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void getUserByIDTest() throws Exception {
		User user = populateUser(true);
		
		String response = "{\"id\":1,\"name\":\"Vasu Kaveri\",\"email\":\"vasu.kaveri@mastek.com\",\"dob\":\"1988-11-28\"";
	    
	    when(userRepository.findOne(1l)).thenReturn(user);
	    this.mockMvc.perform(get("/users/1")).andDo(print()).andExpect(status().isOk())
	                .andExpect(content().string(containsString(response)));
	    
	    verify(userRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void getUserOrganisationTest() throws Exception {
		Organisation org = populateOrganisation(true);
		
		String response = "{\"id\":1,\"name\":\"MASTEK\",\"address\":\"Reading, UK\"}";
		
	    when(userRepository.retrieveOrganisation(1l)).thenReturn(org);
	    this.mockMvc.perform(get("/users/1/organisation")).andDo(print()).andExpect(status().isOk())
	                .andExpect(content().string(containsString(response)));
	    
	    verify(userRepository, times(1)).retrieveOrganisation(1l);
	    verifyNoMoreInteractions(userRepository);
	}
	
	
	@Test
	public void postUserTestForSuccess() throws Exception {
		User user = populateUser(false);
		URI location = UriComponentsBuilder.fromUriString("http://localhost:8090/users/305").build().toUri();
		ResponseEntity<Object> responseEntity = ResponseEntity.created(location).build();
		when(userManager.processUserCreationOrUpdation(any(User.class), eq(true), eq(false))).thenReturn(responseEntity);
		this.mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(user)))
		.andExpect(status().is2xxSuccessful());
		verify(userManager, times(1)).processUserCreationOrUpdation(any(User.class),eq(true),eq(false));
	    verifyNoMoreInteractions(userManager);
	}
	
	@Test
	public void putUserTestForSuccess() throws Exception {
		User user = populateUser(true);
		when(userRepository.findOne(1l)).thenReturn(user);
		URI location = UriComponentsBuilder.fromUriString("http://localhost:8090/users/1").build().toUri();
		ResponseEntity<Object> responseEntity = ResponseEntity.created(location).build();
		when(userManager.processUserCreationOrUpdation(any(User.class), eq(false), eq(true))).thenReturn(responseEntity);
		this.mockMvc.perform(put("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(user)))
		.andExpect(status().is2xxSuccessful());
		verify(userRepository, times(1)).findOne(1l);
		verifyNoMoreInteractions(userRepository);
		verify(userManager, times(1)).processUserCreationOrUpdation(any(User.class),eq(false),eq(true));
	    verifyNoMoreInteractions(userManager);
	}
	
	@Test
	public void putUserTestForFailure() throws Exception {
		User user = populateUser(true);
		when(userRepository.findOne(1l)).thenReturn(null);
		this.mockMvc.perform(put("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(user)))
		.andExpect(status().isNotFound());
		verify(userRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(userRepository);
	}
	

	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public User populateUser(boolean setIds) {
		Organisation org = populateOrganisation(setIds);
        Calendar cal = Calendar.getInstance();
        cal.set(1988, 10, 28);
		User user = new User("Vasu Kaveri", "vasu.kaveri@mastek.com", new java.sql.Date(cal.getTime().getTime()), org);
		if(setIds) {
		    user.setId(1l);
		}
		Group adminGroup = new Group();
    	adminGroup.setName("Admin");
    	if(setIds) {
    	    adminGroup.setId(1l);
    	}
    	Group mgmtGroup = new Group();
    	mgmtGroup.setName("Management");
    	if(setIds) {
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
