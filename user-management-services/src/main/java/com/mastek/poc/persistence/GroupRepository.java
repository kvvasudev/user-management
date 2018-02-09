package com.mastek.poc.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mastek.poc.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

	@Query("select group from com.mastek.poc.model.Group as group"
            + " where group.name = :groupName")
	public Group findByName(@Param("groupName") String groupName);
}
