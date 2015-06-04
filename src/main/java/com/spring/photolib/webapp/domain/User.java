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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jasypt.hibernate4.type.EncryptedStringType;

@TypeDef(name = "encryptedString", typeClass = EncryptedStringType.class, parameters = { @Parameter(name = "encryptorRegisteredName", value = "hibernateStringEncryptor") })
@Entity
@Table(name = "\"User\"")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uid")
	private Integer uid;

	@Column(name = "email_address", unique = true)
	@NotNull
	@NotEmpty
	@Email
	private String emailAddress;
	
	@Column(name="password_old")
	@NotEmpty
	@Type(type = "encryptedString")
	private String passwordOld;

	@Column(name = "password")
	@NotNull
	@NotEmpty
	@Size(min=6)
	@Type(type = "encryptedString")
	private String password;

	@Column(name = "password_confirm")
	@NotNull
	@NotEmpty
	@Size(min=6)
	@Type(type = "encryptedString")
	private String passwordConfirm;

	@Column(name="user_name")
	@NotNull
	@NotEmpty
	private String userName;
	
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", updatable = false)
	private Date creationDate;

	@ManyToOne
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "uid") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "rid") })
	private Role role;
	
	@Column(name="activated")
	private boolean activated;
	
	@Column(name="confirmation_code")
	private String confirmationCode;
	
	@Column(name="stored_confirmation_code")
	private String storedConfirmationCode;
	
	@OneToMany(mappedBy = "user", cascade= CascadeType.ALL)
	private Set<Photo> photos;
	
	@OneToMany(mappedBy = "user", cascade= CascadeType.ALL)
	private Set<Album> albums;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	private Set<PhotoRating> photoRatings;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	private Set<AlbumRating> albumRatings;
	
	@OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
	private Set<UserFlag> userFlags;
	
	@Column(name="banned")
	private boolean banned;

	public User() {}


	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}
	
	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
	
	public void setStoredConfirmationCode(String storedConfirmationCode) {
		this.storedConfirmationCode = storedConfirmationCode;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPhotoRatings(Set<PhotoRating> photoRatings) {
		this.photoRatings = photoRatings;
	}
	
	public void setAlbumRatings(Set<AlbumRating> albumRatings) {
		this.albumRatings = albumRatings;
	}
	
	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public void setUserFlags(Set<UserFlag> userFlags) {
		this.userFlags = userFlags;
	}
	
	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public Integer getUid() {
		return uid;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Set<Photo> getPhotos() {
		return photos;
	}

	public Role getRole() {
		return role;
	}
	
	public boolean getActivated() {
		return activated;
	}
	
	public String getConfirmationCode() {
		return confirmationCode;
	}
	
	public String getStoredConfirmationCode() {
		return storedConfirmationCode;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Set<Album> getAlbums() {
		return albums;
	}
	
	public Set<PhotoRating> getPhotoRatings() {
		return photoRatings;
	}
	
	public Set<AlbumRating> getAlbumRatings() {
		return albumRatings;
	}

	public String getPasswordOld() {
		return passwordOld;
	}

	public Set<UserFlag> getUserFlags() {
		return userFlags;
	}
	
	public boolean getBanned() {
		return banned;
	}

}
