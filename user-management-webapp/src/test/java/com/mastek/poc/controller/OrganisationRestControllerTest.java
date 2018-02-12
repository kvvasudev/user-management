package com.mastek.poc.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.persistence.OrganisationRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(OrganisationRestController.class)
public class OrganisationRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrganisationRepository orgRepository;

	@Test
	public void getOrganisationsTest() throws Exception {
		List<Organisation> orgList = new ArrayList<Organisation>();
		Organisation org = new Organisation();
		org.setId(1l);
		org.setName("MASTEK");
		org.setAddress("Reading, UK");
		orgList.add(org);
		
		String response = "{\"id\":1,\"name\":\"MASTEK\",\"address\":\"Reading, UK\"}";
		
	    when(orgRepository.findAll()).thenReturn(orgList);
	    this.mockMvc.perform(get("/organisations")).andDo(print()).andExpect(status().isOk())
	                .andExpect(content().string(containsString(response)));
	    verify(orgRepository, times(1)).findAll();
	    verifyNoMoreInteractions(orgRepository);
	}
	
	@Test
	public void getOrganisationByIDTest() throws Exception {
		Organisation org = new Organisation();
		org.setId(1l);
		org.setName("MASTEK");
		org.setAddress("Reading, UK");
		
		String response = "{\"id\":1,\"name\":\"MASTEK\",\"address\":\"Reading, UK\"}";
		
	    when(orgRepository.findOne(1l)).thenReturn(org);
	    this.mockMvc.perform(get("/organisations/1")).andDo(print()).andExpect(status().isOk())
	                .andExpect(content().string(containsString(response)));
	    
	    verify(orgRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(orgRepository);
	}
	
	@Test
	public void postOrganisationTestForSuccess() throws Exception {
		Organisation org = new Organisation();
		org.setName("MASTEK");
		org.setAddress("Reading, UK");
		
		Organisation savedOrg = new Organisation();
		savedOrg.setId(1l);
		savedOrg.setName("MASTEK");
		savedOrg.setAddress("Reading, UK");
		
		when(orgRepository.findByName("MASTEK")).thenReturn(null);
		when(orgRepository.save(any(Organisation.class))).thenReturn(savedOrg);
		this.mockMvc.perform(post("/organisations")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(org)))
		.andExpect(status().isCreated())
		.andExpect(header().string("location", containsString("http://localhost/organisations/1")));
		verify(orgRepository, times(1)).findByName("MASTEK");
		verify(orgRepository, times(1)).save(any(Organisation.class));
	    verifyNoMoreInteractions(orgRepository);
	}
	
	@Test
	public void postOrganisationTestForDuplicate() throws Exception {
		Organisation org = new Organisation();
		org.setName("MASTEK");
		org.setAddress("Reading, UK");
		
		Organisation savedOrg = new Organisation();
		savedOrg.setId(1l);
		savedOrg.setName("MASTEK");
		savedOrg.setAddress("Reading, UK");
		
		when(orgRepository.findByName("MASTEK")).thenReturn(savedOrg);
		this.mockMvc.perform(post("/organisations")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(org)))
		.andExpect(status().is4xxClientError());
		verify(orgRepository, times(1)).findByName("MASTEK");
	    verifyNoMoreInteractions(orgRepository);
	}
	
	@Test
	public void putOrganisationTestForSuccess() throws Exception {
		Organisation org = new Organisation();
		org.setId(1l);
		org.setName("MASTEK");
		org.setAddress("Reading, Berkshire, UK");
		
		Organisation savedOrg = new Organisation();
		savedOrg.setId(1l);
		savedOrg.setName("MASTEK");
		savedOrg.setAddress("Reading, UK");
		
		when(orgRepository.findOne(1l)).thenReturn(savedOrg);
		when(orgRepository.save(any(Organisation.class))).thenReturn(org);
		this.mockMvc.perform(put("/organisations/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(org)))
		.andExpect(status().is2xxSuccessful());
		verify(orgRepository, times(1)).findOne(1l);
		verify(orgRepository, times(1)).save(any(Organisation.class));
	    verifyNoMoreInteractions(orgRepository);
	}
	
	@Test
	public void putOrganisationTestForFailure() throws Exception {
		Organisation org = new Organisation();
		org.setId(21l);
		org.setName("MASTEK");
		org.setAddress("Reading, Berkshire, UK");
	
		when(orgRepository.findOne(1l)).thenReturn(null);
		this.mockMvc.perform(put("/organisations/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(org)))
		.andExpect(status().isNotFound());
		verify(orgRepository, times(1)).findOne(1l);
	    verifyNoMoreInteractions(orgRepository);
	}
	

	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
