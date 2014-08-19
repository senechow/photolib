package com.spring.photolib.webapp.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "\"Tag\"")
public class Tag {
	
	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="tid")
	private Integer tid;
	
	@Column(name="name")
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="tags")
	Set<Photo> photos;
	
	public Tag() {}
	
	public Integer getTid() {
		return tid;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<Photo> getPhotos() {
		return photos;
	}
	
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

}
