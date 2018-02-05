package com.mastek.poc.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name="organisation")
public class Organisation {

	private Long id;
    
    private String name;
	
    private String address;
	
	@LastModifiedDate
	@Column(name = "last_updated_date", columnDefinition = "timestamp with time zone", nullable = false)
	private ZonedDateTime lastUpdatedDate;
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return "Organisation [id=" + id + ", name=" + name + ", address=" + address + "]";
	}

}
