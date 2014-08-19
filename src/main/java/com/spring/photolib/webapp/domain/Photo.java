package com.spring.photolib.webapp.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "\"Photo\"")
public class Photo {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column (name="pid")
	private Integer pid;
	
	@Column (name = "image")
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte [] image;
	
	@Column (name = "name")
	@NotNull
	@NotEmpty
	@Size(max=50)
	private String name;
	
	@Column (name = "isPublic")
	@NotNull
	private boolean isPublic;
	
	@Column (name="description")
	@Size(max=255)
	private String description;
	
	@Column (name="imageContentType")
	private String imageContentType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", updatable=false )
	@DateTimeFormat(pattern="yyy/MM/dd")
	private Date creationDate;
	
	@Column(name="imageStoreName")
	private String imageStoreName;
	
	@ManyToOne
	@JoinColumn(name="uid")
	private User user;
	
	@Transient
	private MultipartFile imageFile;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="PHOTO_TAG",
				joinColumns={@JoinColumn(name = "pid")},
				inverseJoinColumns={@JoinColumn(name="tid")})
	private Set<Tag> tags;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="photos")
	private Set<Album> albums;
	
	@OneToOne(mappedBy = "photo", cascade = CascadeType.ALL)
	private Rating rating;
	
	@OneToMany(mappedBy = "photo", cascade=CascadeType.ALL)
	private Set<PhotoFlag> photoFlags;
	
	public Photo() {}
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public void setImage(byte [] image) {
		this.image = image;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	
	public void setImageStoreName(String imageStoreName) {
		this.imageStoreName = imageStoreName;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}
	

	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	public void setPhotoFlags(Set<PhotoFlag> photoFlags) {
		this.photoFlags = photoFlags;
	}
	
	public Integer getPid() {
		return pid;
	}
	
	public byte [] getImage() {
		return image;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getIsPublic() {
		return isPublic;
	}
	
	public String getDescription() {
		return description;
	}
		
	public String getImageContentType() {
		return imageContentType;
	}
	
	public User getUser() {
		return user;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public MultipartFile getImageFile() {
		return imageFile;
	}
	
	public String getImageStoreName() {
		return imageStoreName;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}
	
	public Set<Album> getAlbums() {
		return albums;
	}
	
	public Rating getRating() {
		return rating;
	}

	public Set<PhotoFlag> getPhotoFlags() {
		return photoFlags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Photo))
			return false;
		Photo other = (Photo) obj;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}



}
