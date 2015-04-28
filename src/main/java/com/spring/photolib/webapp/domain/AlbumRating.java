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
@Table(name = "\"AlbumRating\"")
public class AlbumRating {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "arid")
	private Integer arid;

	@Column(name = "rating")
	private Float rating;

	@Column(name = "numRatings")
	private Integer numRatings;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="ALBUM_RATING_USERS",
				joinColumns={@JoinColumn(name = "arid")},
				inverseJoinColumns={@JoinColumn(name="uid")})
	private Set<User> users;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private Album album;
	
	public AlbumRating() {	}
	
	public Integer getArid() {
		return arid;
	}

	public void setArid(Integer arid) {
		this.arid = arid;
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
	
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public Album getAlbum() {
		return album;
	}
	
}
