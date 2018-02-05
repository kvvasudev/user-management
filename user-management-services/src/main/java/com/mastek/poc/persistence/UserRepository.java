package com.mastek.poc.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mastek.poc.model.Organisation;
import com.mastek.poc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select user.organisation from com.mastek.poc.model.User as user"
            + " where user.id = :userId")
	public Organisation retrieveOrganisation(@Param("userId") Long userId);
}
