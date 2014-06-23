package com.spring.photolib.webapp.domain;

import java.util.Date;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.sun.istack.internal.NotNull;

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
	private String name;
	
	@Column (name = "isPublic")
	@NotNull
	private boolean isPublic;
	
	@Column (name="description")
	private String description;
	
	@Column (name="imageContentType")
	private String imageContentType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", updatable=false )
	private Date creationDate;
	
	@Column(name="imageStoreName")
	private String imageStoreName;
	
	@ManyToOne
	@JoinColumn(name="uid")
	private User user;
	
	@Transient
	private MultipartFile imageFile;
	
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

}
