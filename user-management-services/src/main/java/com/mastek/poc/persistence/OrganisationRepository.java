package com.mastek.poc.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mastek.poc.model.Organisation;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

}
