package com.mastek.poc.persistence;

import java.util.Calendar;
import java.util.HashSet;

import javax.transaction.Transactional;

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

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrganisationRepository organisationRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        populateUser(user);
        populateOrganisation(organisation);
    }

    public static void populateUser(User user) {
        user.setName("Vasudev Kaveri");
        user.setEmail("veera.kaveri@mastek.com");
        
        Calendar cal = Calendar.getInstance();
        cal.set(1988, 10, 28);
        user.setDob(new java.sql.Date(cal.getTime().getTime()));
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
    @Transactional
    public void testUserSave() {
    	organisation = organisationRepository.saveAndFlush(organisation);
    	long originalOrgId = organisation.getId();
    	Assert.assertTrue(originalOrgId > 0);
    	
    	User newUser = new User(user.getName(), user.getEmail(), user.getDob(), organisation);
    	
    	Group adminGroup = new Group();
    	adminGroup.setName("Admin");
    	
    	Group mgmtGroup = new Group();
    	mgmtGroup.setName("Management");
    	
        newUser.setGroups(new HashSet<Group>(){{add(adminGroup);}{add(mgmtGroup);}});
        newUser = userRepository.saveAndFlush(newUser);
        Assert.assertTrue(newUser.getId() > 0);
        
        userRepository.delete(newUser);
        organisationRepository.delete (organisation);
    }
    
    @Test
    @Transactional
    public void testUserSaveWithExistingGroup() {
    	organisation = organisationRepository.saveAndFlush(organisation);
    	long originalOrgId = organisation.getId();
    	Assert.assertTrue(originalOrgId > 0);
    	
        User newUser = new User(user.getName(), user.getEmail(), user.getDob(), organisation);
        HashSet<Group> groupSet = new HashSet<Group>();
        
        Group adminGroup = new Group();
        adminGroup.setName("Admin");
        groupSet.add(adminGroup);
        
        newUser.setGroups(groupSet);

        newUser = userRepository.saveAndFlush(newUser);
        Assert.assertTrue(newUser.getId() > 0);
        
        Group existingGroup = groupRepository.findByName(adminGroup.getName());
        Assert.assertTrue(existingGroup.getId() > 0);
        
        User anotherUser = new User("VKTest2", "vktest2@mastek.com", user.getDob(), organisation);
        HashSet<Group> anotherGroupSet = new HashSet<Group>();
        anotherGroupSet.add(existingGroup);
        anotherUser.setGroups(anotherGroupSet);
        
        anotherUser = userRepository.saveAndFlush(anotherUser);
        Assert.assertTrue(anotherUser.getId() > 0);
        
        userRepository.delete(newUser);
        userRepository.delete(anotherUser);
        organisationRepository.delete(organisation);
        
    }
    
    @Test
    @Transactional
    public void testUserUpdateAddGroup() {
    	
    	organisation = organisationRepository.saveAndFlush(organisation);
    	long originalOrgId = organisation.getId();
    	Assert.assertTrue(originalOrgId > 0);
    	
        User newUser = new User(user.getName(), user.getEmail(), user.getDob(), organisation);
        HashSet<Group> groupSet = new HashSet<Group>();
        
        Group adminGroup = new Group();
        adminGroup.setName("Admin");
        groupSet.add(adminGroup);
        
        newUser.setGroups(groupSet);

        newUser = userRepository.saveAndFlush(newUser);
        Assert.assertTrue(newUser.getId() > 0);
    	
    	User existingUser = userRepository.findOne(newUser.getId());
    	Assert.assertTrue(existingUser.getId() > 0);
    	
    	Group anotherGroup = new Group();
        anotherGroup.setName("HR");
        
        existingUser.getGroups().add(anotherGroup);
    	
        existingUser = userRepository.saveAndFlush(existingUser);
        
        Assert.assertTrue(existingUser.getId() > 0);
        
        userRepository.delete(existingUser);
        organisationRepository.delete(organisation);
    }
    
    @Test
    @Transactional
    public void testRetrieveOrganisationOfUser() {
    	organisation = organisationRepository.saveAndFlush(organisation);
    	Assert.assertTrue(organisation.getId() > 0);
    	User newUser = new User(user.getName(), user.getEmail(), user.getDob(), organisation);
    	
    	Group appsGroup = new Group();
    	appsGroup.setName("AppsDev");
    	
    	newUser.setGroups(new HashSet<Group>(){{add(appsGroup);}});
    	newUser = userRepository.save(newUser);
        Assert.assertTrue(newUser.getId() > 0);
        
        Organisation orgFromRepo = userRepository.retrieveOrganisation(newUser.getId());
        Assert.assertTrue(orgFromRepo.getId() == organisation.getId());
        
        userRepository.delete(newUser);
        organisationRepository.delete(organisation);
    }
    
    @Test
    @Transactional
    public void testCheckUniqueUserEmailInOrganisationForSuccess() {
    	organisation = organisationRepository.save(organisation);
    	long orgIdInitial = organisation.getId();
    	Assert.assertTrue(orgIdInitial > 0);
    	
    	User newUser = new User(user.getName(), user.getEmail(), user.getDob(), organisation);;
    	
    	Group groupOne = new Group();
    	groupOne.setName("AppsDev");
    	newUser.setGroups(new HashSet<Group>(){{add(groupOne);}});
    	newUser = userRepository.save(newUser);
        Assert.assertTrue(newUser.getId() > 0);
        
        User existingUser = userRepository.checkUniqueUserEmailInOrganisation(organisation, newUser.getEmail());
        Assert.assertNotNull(existingUser);
        Assert.assertTrue(existingUser.getEmail().equalsIgnoreCase(user.getEmail()) && existingUser.getOrganisation().getId() == orgIdInitial);
        
        userRepository.delete(newUser);
        organisationRepository.delete(organisation);
    }
    
    @Test
    @Transactional
    public void testCheckUniqueUserEmailInOrganisationForFailure() {
    	organisation = organisationRepository.save(organisation);
    	Assert.assertTrue(organisation.getId() > 0);
    	        
        User existingUser = userRepository.checkUniqueUserEmailInOrganisation(organisation, "testuser@comp.com");
        Assert.assertNull(existingUser);
        
        organisationRepository.delete(organisation);
    }

}
