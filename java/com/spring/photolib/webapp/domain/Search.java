package com.spring.photolib.webapp.domain;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class Search {
	
	private String name;	
	
	private String description;
	
	private String sortType;
	
	@DateTimeFormat(pattern="yyy/MM/dd")
	@Past
	private Date createdSince;
	
	private Set<String> tags;
	
	public Search() {}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getSortType() {
		return sortType;
	}
	
	public Date getCreatedSince() {
		return createdSince;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
	public void setCreatedSince(Date createdSince) {
		this.createdSince = createdSince;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

}
