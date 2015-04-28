package com.spring.photolib.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "\"UserFlag\"")
public class UserFlag extends Flag{
	
	@ManyToOne
	@JoinColumn(name="uid")
	private User user;
	
	public UserFlag() {}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
