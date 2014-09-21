package com.spring.photolib.webapp.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "\"PhotoRating\"")
public class PhotoRating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prid")
	private Integer prid;

	@Column(name = "rating")
	private Float rating;

	@Column(name = "numRatings")
	private Integer numRatings;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="PHOTO_RATING_USERS",
				joinColumns={@JoinColumn(name = "prid")},
				inverseJoinColumns={@JoinColumn(name="uid")})
	private Set<User> users;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private Photo photo;
	
	public PhotoRating() {}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	public Photo getPhoto() {
		return photo;
	}
	
	public Integer getPrid() {
		return prid;
	}

	public void setPrid(Integer prid) {
		this.prid = prid;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public void setNumRatings(Integer numRatings) {
		this.numRatings = numRatings;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public Float getRating() {
		return rating;
	}

	public Integer getNumRatings() {
		return numRatings;
	}
	
	public Set<User> getUsers() {
		return users;
	}


}
