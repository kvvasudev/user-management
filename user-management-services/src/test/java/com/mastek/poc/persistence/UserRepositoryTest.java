package com.mastek.poc.persistence;

import java.util.Calendar;
import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mastek.poc.TestConfig;
import com.mastek.poc.model.Group;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserRepositoryTest {
	
	private static User user = new User();
	private static Organisation organisation = new Organisation();
	private static Group group = new Group();

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrganisationRepository organisationRepository;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        populateUser(user);
        populateOrganisation(organisation);
        populateGroup(group);
    }

    public static void populateUser(User user) {
        user.setName("Vasudev Kaveri");
        user.setEmail("veera.kaveri@mastek.com");
        
        Calendar cal = Calendar.getInstance();
        cal.set(1988, 10, 28);
        user.setDob(new java.sql.Date(cal.getTime().getTime()));
    }
    
    public static void populateGroup(Group group) {
    	group.setName("AppsDev");
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
        user = null;
        organisation = null;
    }

    @Test
    public void testUserSave() {
    	organisation = organisationRepository.save(organisation);
    	Assert.assertTrue(organisation.getId() > 0);
    	
    	user.setName("Vasudev Kaveri01");
    	user.setOrganisation(organisation);
    	user.setGroups(new HashSet<Group>(){{add(group);}});
        user = userRepository.save(user);
        Assert.assertTrue(user.getId() > 0);
        
        userRepository.delete(user);
        organisationRepository.delete(organisation);
    }
    
    @Test
    public void testRetrieveOrganisationOfUser() {
    	organisation = organisationRepository.save(organisation);
    	Assert.assertTrue(organisation.getId() > 0);
    	user.setOrganisation(organisation);
    	user.setName("Vasudev Kaveri02");
    	user.setGroups(new HashSet<Group>(){{add(group);}});
        user = userRepository.save(user);
        Assert.assertTrue(user.getId() > 0);
        
        Organisation orgFromRepo = userRepository.retrieveOrganisation(user.getId());
        Assert.assertTrue(orgFromRepo.getId() == organisation.getId());
        
        userRepository.delete(user);
        organisationRepository.delete(organisation);
    }
    
    @Test
    public void testCheckUniqueUserEmailInOrganisationForSuccess() {
    	organisation = organisationRepository.save(organisation);
    	Assert.assertTrue(organisation.getId() > 0);
    	user.setOrganisation(organisation);
    	user.setName("Vasudev Kaveri02");
    	user.setGroups(new HashSet<Group>(){{add(group);}});
        user = userRepository.save(user);
        Assert.assertTrue(user.getId() > 0);
        
        User existingUser = userRepository.checkUniqueUserEmailInOrganisation(organisation, user.getEmail());
        Assert.assertNotNull(existingUser);
        Assert.assertTrue(existingUser.getEmail().equalsIgnoreCase(user.getEmail()) && existingUser.getOrganisation().getId() == user.getOrganisation().getId());
        
        userRepository.delete(user);
        organisationRepository.delete(organisation);
    }
    
    @Test
    public void testCheckUniqueUserEmailInOrganisationForFailure() {
    	organisation = organisationRepository.save(organisation);
    	Assert.assertTrue(organisation.getId() > 0);
    	user.setOrganisation(organisation);
    	user.setName("Vasudev Kaveri02");
    	user.setGroups(new HashSet<Group>(){{add(group);}});
        user = userRepository.save(user);
        Assert.assertTrue(user.getId() > 0);
        
        User existingUser = userRepository.checkUniqueUserEmailInOrganisation(organisation, "testuser@comp.com");
        Assert.assertNull(existingUser);
        
        userRepository.delete(user);
        organisationRepository.delete(organisation);
    }

}
