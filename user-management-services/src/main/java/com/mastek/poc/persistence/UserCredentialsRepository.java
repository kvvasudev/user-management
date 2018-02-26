package com.mastek.poc.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mastek.poc.model.User;
import com.mastek.poc.model.UserCredentials;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
	@Query("select usercred.user from com.mastek.poc.model.UserCredentials as usercred"
            + " where usercred.loginName = :userName and usercred.password = :password")
	public User authenticateUser(@Param("userName") String userName, @Param("password") String password);

	@Query("select usercred from com.mastek.poc.model.UserCredentials as usercred"
            + " where usercred.user = :user")
	public UserCredentials findUserCredentialsByUser(@Param("user") User user);
	
	@Query("select usercred from com.mastek.poc.model.UserCredentials as usercred"
            + " where usercred.loginName = :loginName")
	public UserCredentials findByLoginName(@Param("loginName") String loginName);
}
