package com.mastek.poc.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastek.poc.model.Group;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.model.UserCredentials;
import com.mastek.poc.persistence.UserCredentialsRepository;
import com.mastek.poc.persistence.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRegistrationController.class)
public class UserRegistrationControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserCredentialsRepository userCredRepository;

	@MockBean
	private UserRepository userRepository;
	
	@Test
	public void registerUserTestForSuccess() throws Exception {
		User user = populateUser(true);
		UserCredentials userCred = new UserCredentials();
		userCred.setLoginName("vkaveri");
		userCred.setPassword("Pa$$w0rd");
		userCred.setUser(user);
		
		when(userCredRepository.findUserCredentialsByUser(any(User.class))).thenReturn(null);
		when(userRepository.findOne(userCred.getUser().getId())).thenReturn(user);
		when(userCredRepository.findByLoginName(userCred.getLoginName())).thenReturn(null);
		this.mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(userCred)))
		.andExpect(status().is2xxSuccessful());
		verify(userCredRepository, times(1)).findUserCredentialsByUser(any(User.class));
		verify(userCredRepository, times(1)).findByLoginName(any(String.class));
		verify(userCredRepository, times(1)).save(any(UserCredentials.class));
	    verifyNoMoreInteractions(userCredRepository);
	    verify(userRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void registerUserTestForFailure_UserRegistered() throws Exception {
		User user = populateUser(true);
		UserCredentials userCred = new UserCredentials();
		userCred.setLoginName("vkaveri");
		userCred.setPassword("Pa$$w0rd");
		userCred.setUser(user);
		
		when(userCredRepository.findUserCredentialsByUser(any(User.class))).thenReturn(userCred);
		this.mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(userCred)))
		.andExpect(status().is4xxClientError());
		verify(userCredRepository, times(1)).findUserCredentialsByUser(any(User.class));
	    verifyNoMoreInteractions(userCredRepository);
	}
	
	@Test
	public void registerUserTestForFailure_NotAValidUser() throws Exception {
		User user = populateUser(true);
		UserCredentials userCred = new UserCredentials();
		userCred.setLoginName("vkaveri");
		userCred.setPassword("Pa$$w0rd");
		userCred.setUser(user);
		
		when(userCredRepository.findUserCredentialsByUser(any(User.class))).thenReturn(null);
		when(userRepository.findOne(userCred.getUser().getId())).thenReturn(null);
		this.mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(userCred)))
		.andExpect(status().is4xxClientError());
		verify(userCredRepository, times(1)).findUserCredentialsByUser(any(User.class));
	    verifyNoMoreInteractions(userCredRepository);
	    verify(userRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void registerUserTestForFailure_LoginNameUsed() throws Exception {
		User user = populateUser(true);
		UserCredentials userCred = new UserCredentials();
		userCred.setLoginName("vkaveri");
		userCred.setPassword("Pa$$w0rd");
		userCred.setUser(user);
		
		when(userCredRepository.findUserCredentialsByUser(any(User.class))).thenReturn(null);
		when(userRepository.findOne(userCred.getUser().getId())).thenReturn(user);
		when(userCredRepository.findByLoginName(userCred.getLoginName())).thenReturn(userCred);
		this.mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(userCred)))
		.andExpect(status().is4xxClientError());
		verify(userCredRepository, times(1)).findUserCredentialsByUser(any(User.class));
		verify(userCredRepository, times(1)).findByLoginName(any(String.class));
	    verifyNoMoreInteractions(userCredRepository);
	    verify(userRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void authenticateUserTestForSuccess() throws Exception {
		User user = populateUser(true);
		UserCredentials userCred = new UserCredentials();
		userCred.setLoginName("vkaveri");
		userCred.setPassword("Pa$$w0rd");
		
		when(userCredRepository.authenticateUser(any(String.class),any(String.class))).thenReturn(user);
		this.mockMvc.perform(post("/user/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(userCred)))
		.andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("vasu.kaveri@mastek.com")));
		verify(userCredRepository, times(1)).authenticateUser(any(String.class),any(String.class));
	    verifyNoMoreInteractions(userCredRepository);
	}
	
	@Test
	public void authenticateUserTestForFailure() throws Exception {
		UserCredentials userCred = new UserCredentials();
		userCred.setLoginName("vkaveri");
		userCred.setPassword("Pa$$w0rd");
		
		when(userCredRepository.authenticateUser(any(String.class),any(String.class))).thenReturn(null);
		this.mockMvc.perform(post("/user/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(userCred)))
		.andExpect(status().is4xxClientError());
		verify(userCredRepository, times(1)).authenticateUser(any(String.class),any(String.class));
	    verifyNoMoreInteractions(userCredRepository);
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
