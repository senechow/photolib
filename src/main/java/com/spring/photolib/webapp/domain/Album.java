package com.spring.photolib.webapp.domain;

import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "\"Album\"")
public class Album {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="aid")
	private Integer aid;
	
	@Column(name="name")
	@NotNull
	@NotEmpty
	@Size(max=50)
	private String name;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", updatable = false, nullable=false)
	@DateTimeFormat(pattern="yyy/MM/dd")
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date", nullable = false )
	@DateTimeFormat(pattern="yyy/MM/dd")
	private Date lastUpdateDate;
	
	@Column(name="description")
	@Size(max=255)
	private String description;
	
	@Column(name="isPublic")
	private boolean isPublic;
	
	@Column(name="album_size")
	private int albumSize;
	
	@Column(name="viewCount")
	private int viewCount;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="ALBUM_PHOTO",
				joinColumns={@JoinColumn(name = "aid")},
				inverseJoinColumns={@JoinColumn(name="pid")})
	private Set<Photo> photos;
	
	@ManyToOne
	@JoinColumn(name="uid")
	private User user;
	
	@OneToMany(mappedBy = "album", cascade=CascadeType.ALL)
	private Set<AlbumFlag> albumFlags;
	
	@OneToOne(mappedBy = "album", cascade = CascadeType.ALL)
	private AlbumRating rating;

	public Album() {}
	
	public Integer getAid() {
		return aid;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public boolean getIsPublic() {
		return isPublic;
	}
	
	public int getAlbumSize() {
		return albumSize;
	}
	
	public int getViewCount() {
		return viewCount;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Set<Photo> getPhotos() {
		return photos;
	}
	
	public User getUser() {
		return user;
	}
	
	public Set<AlbumFlag> getAlbumFlags() {
		return albumFlags;
	}
	
	public AlbumRating getRating() {
		return rating;
	}
	
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public void setAlbumSize(int albumSize) {
		this.albumSize = albumSize;
	}
	
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	public void setAlbumFlags(Set<AlbumFlag> albumFlags) {
		this.albumFlags = albumFlags;
	}

	public void setRating(AlbumRating rating) {
		this.rating = rating;
	}
}
