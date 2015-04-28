package com.spring.photolib.webapp.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author admin
 * 
 */
@Entity
@Table(name = "\"Rating\"")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rtid")
	private Integer rtid;

	@Column(name = "rating")
	private Float rating;

	@Column(name = "numRatings")
	private Integer numRatings;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="RATING_USERS",
				joinColumns={@JoinColumn(name = "rtid")},
				inverseJoinColumns={@JoinColumn(name="uid")})
	private Set<User> users;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Photo photo;

	public Rating() {
	}

	public Integer getRtid() {
		return rtid;
	}

	public void setRtid(Integer rtid) {
		this.rtid = rtid;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public void setNumRatings(Integer numRatings) {
		this.numRatings = numRatings;
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
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

	public Photo getPhoto() {
		return photo;
	}
	
	public Set<User> getUsers() {
		return users;
	}



}
