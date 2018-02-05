package com.mastek.poc.persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mastek.poc.TestConfig;
import com.mastek.poc.model.Organisation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrganisationRepositoryTest {
	
	private static Organisation organisation = new Organisation();

    @Autowired
    private OrganisationRepository organisationRepository;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        populateOrganisation(organisation);
    }

    public static void populateOrganisation(Organisation organisation) {
    	organisation.setName("Mastek");
    	organisation.setAddress("Pennant House\r\n" + 
    			"2 Napier Court\r\n" + 
    			"Reading\r\n" + 
    			"RG1 8BW, UK");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        organisation = null;
    }

    @Test
    public void testOrganisationSave() {
    	organisation = organisationRepository.save(organisation);
    	Assert.assertTrue(organisation.getId() > 0);
    	organisationRepository.delete(organisation);
    }
    
    @Test
    public void testOrganisationFind() {
    	organisation = organisationRepository.save(organisation);
    	
    	organisation = organisationRepository.findOne(organisation.getId());
    	Assert.assertTrue(organisation.getId() > 0);
    	organisationRepository.delete(organisation);
    }

}
