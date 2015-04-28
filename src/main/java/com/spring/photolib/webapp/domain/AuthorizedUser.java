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
	
	private Set<Album> albums;
	
	private String userName;
	
	private boolean banned;

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
	
	public void setAlbums(Set<Album> albums){
		this.albums = albums;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setBanned(boolean banned) {
		this.banned = banned;
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
	
	public String getUserName() {
		return userName;
	}
	
	public boolean getBanned() {
		return banned;
	}
}
