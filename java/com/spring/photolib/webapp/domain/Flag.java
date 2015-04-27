package com.spring.photolib.webapp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="\"Flag\"")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Flag {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="fid")
	private Integer fid;
	
	@NotEmpty
	@Column(name="reason")
	private String reason;
	
	@Column(name="description")
	@Size(max=255)
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", updatable=false )
	@DateTimeFormat(pattern="yyy/MM/dd")
	private Date creationDate;
	
	public Flag() {}

	public Integer getfid() {
		return fid;
	}

	public String getReason() {
		return reason;
	}

	public String getDescription() {
		return description;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setfid(Integer fid) {
		this.fid = fid;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
