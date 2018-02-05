package com.mastek.poc.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mastek.poc.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
