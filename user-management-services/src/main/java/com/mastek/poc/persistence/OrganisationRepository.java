package com.mastek.poc.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mastek.poc.model.Organisation;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
	@Query("select org from com.mastek.poc.model.Organisation as org"
            + " where org.name = :orgName")
	public Organisation findByName(@Param("orgName") String orgName);
}
