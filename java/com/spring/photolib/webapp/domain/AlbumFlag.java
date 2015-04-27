package com.spring.photolib.webapp.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "\"AlbumFlag\"")
@AttributeOverrides({
    @AttributeOverride(name="reason", column=@Column(name="reason")),
    @AttributeOverride(name="description", column=@Column(name="description"))
})
public class AlbumFlag extends Flag {
	
	@ManyToOne
	@JoinColumn(name="aid")
	private Album album;
	
	public AlbumFlag() {}
	
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public Album getAlbum() {
		return album;
	}

}
