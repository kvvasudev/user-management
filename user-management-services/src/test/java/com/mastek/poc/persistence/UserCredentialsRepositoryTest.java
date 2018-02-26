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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mastek.poc.TestConfig;
import com.mastek.poc.model.Group;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;
import com.mastek.poc.model.UserCredentials;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class UserCredentialsRepositoryTest {

	private static User user = new User();
	private static Organisation organisation = new Organisation();
	
	@Autowired
	private UserCredentialsRepository userCredRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrganisationRepository orgRepository;
	
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
    public void testUserCredentialsSave() {
    	organisation = orgRepository.saveAndFlush(organisation);
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
        
        UserCredentials userCred = new UserCredentials();
        userCred.setLoginName("vkaveri");
        userCred.setPassword("pa$$w0rd");
        userCred.setUser(newUser);
        
        userCred = userCredRepository.saveAndFlush(userCred);
        Assert.assertTrue(userCred.getId() > 0);
        
        userRepository.delete(newUser);
        orgRepository.delete (organisation);
        userCredRepository.delete(userCred);
    }
    
    @Test
    @Transactional
    public void testFindUserCredentialsByUser() {
    	organisation = orgRepository.saveAndFlush(organisation);
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
        
        UserCredentials userCred = new UserCredentials();
        userCred.setLoginName("vkaveri");
        userCred.setPassword("pa$$w0rd");
        userCred.setUser(newUser);
        
        userCred = userCredRepository.saveAndFlush(userCred);
        Assert.assertTrue(userCred.getId() > 0);
        
        UserCredentials userCredFromRepo = userCredRepository.findUserCredentialsByUser(newUser);
        Assert.assertTrue(userCredFromRepo.getLoginName().equals(userCred.getLoginName()) &&
        		userCredFromRepo.getPassword().equals(userCred.getPassword()));
        
        userRepository.delete(newUser);
        orgRepository.delete (organisation);
        userCredRepository.delete(userCred);
    }
    
    @Test
    @Transactional
    public void testFindByLoginName() {
    	organisation = orgRepository.saveAndFlush(organisation);
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
        
        UserCredentials userCred = new UserCredentials();
        userCred.setLoginName("vkaveri");
        userCred.setPassword("pa$$w0rd");
        userCred.setUser(newUser);
        
        userCred = userCredRepository.saveAndFlush(userCred);
        Assert.assertTrue(userCred.getId() > 0);
        
        UserCredentials userCredFromRepo = userCredRepository.findByLoginName(userCred.getLoginName());
        Assert.assertTrue(userCredFromRepo.getLoginName().equals(userCred.getLoginName()) &&
        		userCredFromRepo.getPassword().equals(userCred.getPassword()));
        
        userRepository.delete(newUser);
        orgRepository.delete (organisation);
        userCredRepository.delete(userCred);
    }
    
    @Test
    @Transactional
    public void testAuthenticateUser() {
    	organisation = orgRepository.saveAndFlush(organisation);
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
        
        UserCredentials userCred = new UserCredentials();
        userCred.setLoginName("vkaveri");
        userCred.setPassword("pa$$w0rd");
        userCred.setUser(newUser);
        
        userCred = userCredRepository.saveAndFlush(userCred);
        Assert.assertTrue(userCred.getId() > 0);
        
        User userFromRepo = userCredRepository.authenticateUser(userCred.getLoginName(), userCred.getPassword());
        Assert.assertTrue(userFromRepo.getEmail().equals(newUser.getEmail()) &&
        		userFromRepo.getId() == newUser.getId());
        
        userRepository.delete(newUser);
        orgRepository.delete (organisation);
        userCredRepository.delete(userCred);
    }
}
