package com.mastek.poc.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name="USER")
public class User {
	
    private Long id;
    
    private String name;
    
    private String email;
    
	@Column(name="dob", columnDefinition = "DATETIME")
    private ZonedDateTime dateOfBirth;
	
	@LastModifiedDate
	@Column(name = "last_updated_date", columnDefinition = "DATETIME", nullable = false)
	private ZonedDateTime lastUpdatedDate;
	
    private Organisation organisation;
    
    private Set<Group> groups;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ZonedDateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(ZonedDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
    @ManyToOne
    @JoinColumn(name = "org_id")
    public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@PrePersist
    public void prePersist() {
        this.lastUpdatedDate = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdatedDate = ZonedDateTime.now(ZoneId.of("UTC"));
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", dateOfBirth=" + dateOfBirth + "]";
	}

	public User(String name, String email, ZonedDateTime dateOfBirth, Organisation organisation, Set<Group> groups) {
		super();
		this.name = name;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.organisation = organisation;
		this.groups = groups;
	}
	
	public User(String name, String email, ZonedDateTime dateOfBirth, Set<Group> groups) {
		super();
		this.name = name;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.groups = groups;
	}
	
	public User(String name, String email, ZonedDateTime dateOfBirth, Organisation organisation) {
		super();
		this.name = name;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.organisation = organisation;
	}

	public User() {
		super();
	}
    
}