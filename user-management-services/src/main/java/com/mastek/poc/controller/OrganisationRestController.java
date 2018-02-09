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
import com.mastek.poc.persistence.OrganisationRepository;

@RestController
@RequestMapping("/organisations")
public class OrganisationRestController {
	
	@Autowired
    private OrganisationRepository organisationRepository;

    @GetMapping
    public List<Organisation> getOrganisations() {
        return organisationRepository.findAll();
    }
    
    @GetMapping("/{orgId}")
    public Organisation getOrganisation(@PathVariable Long orgId) {
    	return  organisationRepository.findOne(orgId);
    }

    @PostMapping
    public ResponseEntity<Object> newOrganisation(@Valid @RequestBody Organisation organisation) {
    	Organisation existingOrg = organisationRepository.findByName(organisation.getName());
    	if(existingOrg!=null) {
    		throw new UserManagementException(String.format("Organisation Name %s Already Created, chose a different one", organisation.getName()));
    	}
    	Organisation savedOrg = organisationRepository.save(organisation);
    	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{orgId}")
    			.buildAndExpand(savedOrg.getId()).toUri();

    	return ResponseEntity.created(location).build();
    }

    @PutMapping("/{orgId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> editOrganisation(@Valid @RequestBody Organisation organisation, @PathVariable Long orgId) {
    	Organisation existingOrg = organisationRepository.findOne(orgId);
    	
    	if(existingOrg == null) {
    		return ResponseEntity.notFound().build();
    	}
    	existingOrg.setAddress(organisation.getAddress());
    	organisationRepository.save(existingOrg);
    	return ResponseEntity.noContent().build();
    }
    
}