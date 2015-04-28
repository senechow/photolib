package com.spring.photolib.webapp.domain;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "\"PhotoFlag\"")
@AttributeOverrides({
    @AttributeOverride(name="reason", column=@Column(name="reason")),
    @AttributeOverride(name="description", column=@Column(name="description"))
})
public class PhotoFlag extends Flag{
	
	@ManyToOne
	@JoinColumn(name="pid")
	private Photo photo;
	
	public PhotoFlag() {}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	

}
