package com.mastek.poc.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name="usercredentials")
public class UserCredentials {
	
	private Long id;
	
    @NotNull
    @Column(name = "login_name")
    @Size(min=2, max=50, message="Name must be of size 2 to 50 characters")
    private String loginName;
	
    @NotNull
    @Column(name = "password")
    @Size(min=2, max=100, message="Name must be of size 2 to 100 characters")
    private String password;
    
	@LastModifiedDate
	@Column(name = "last_updated_date", columnDefinition = "timestamp with time zone", nullable = false)
	private ZonedDateTime lastUpdatedDate;
	
	//@NotNull
	private User user;
	
    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
		return user;
	}
    
	public void setUser(User user) {
		this.user = user;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return "UserCredentials [loginName=" + loginName + ", password=" + password + ", lastUpdatedDate="
				+ lastUpdatedDate + ", user=" + user + "]";
	}
    
}
