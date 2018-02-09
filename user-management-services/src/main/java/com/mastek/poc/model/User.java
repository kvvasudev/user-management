package com.mastek.poc.model;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name="userdata")
public class User {
	
    private Long id;
    
    @NotNull
    @Size(min=2, max=50, message="Name must be of size 2 to 50 characters")
    private String name;
    
    @NotNull
    @Email
    private String email;
    
    @NotNull
    private Date dob;
	
	@LastModifiedDate
	@Column(name = "last_updated_date", columnDefinition = "timestamp with time zone", nullable = false)
	private ZonedDateTime lastUpdatedDate;
	
    private Organisation organisation;
    
    private Set<Group> groups = new HashSet<Group>();

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", dob=" + dob + ", lastUpdatedDate="
				+ lastUpdatedDate + ", organisation=" + organisation + ", groups=" + groups + "]";
	}

	public User(String name, String email, Date dob, Organisation organisation, Set<Group> groups) {
		super();
		this.name = name;
		this.email = email;
		this.dob = dob;
		this.organisation = organisation;
		this.groups = groups;
	}
	
	public User(String name, String email, Date dob, Set<Group> groups) {
		super();
		this.name = name;
		this.email = email;
		this.dob = dob;
		this.groups = groups;
	}
	
	public User(String name, String email, Date dob, Organisation organisation) {
		super();
		this.name = name;
		this.email = email;
		this.dob = dob;
		this.organisation = organisation;
	}

	public User() {
		super();
	}
	
}