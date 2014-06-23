package com.spring.photolib.webapp.domain;

import java.util.Collection;
import java.util.Date;


import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthorizedUser extends User {

	private Integer id;

	private String emailAddress;

	private String firstName;

	private String lastName;

	private Date creationDate;

	private Role role;
	
	private Set<Photo> photos;

	public AuthorizedUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public AuthorizedUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Set<Photo> getPhotos() {
		return photos;
	}

	public Role getRole() {
		return role;
	}
}
