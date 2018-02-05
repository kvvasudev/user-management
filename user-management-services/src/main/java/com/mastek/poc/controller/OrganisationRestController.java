package com.mastek.poc.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.persistence.OrganisationRepository;

@RestController
@RequestMapping("/organisations")
public class OrganisationRestController {
    private OrganisationRepository organisationRepository;

    public OrganisationRestController(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @GetMapping
    public List<Organisation> getOrganisations() {
        return organisationRepository.findAll();
    }

    @PostMapping
    public void newOrganisation(Organisation organisation) {
    	organisationRepository.save(organisation);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void editOrganisation(Organisation organisation) {
    	organisationRepository.save(organisation);
    }
    
}