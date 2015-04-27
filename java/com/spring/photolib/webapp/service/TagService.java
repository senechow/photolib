package com.spring.photolib.webapp.service;

import java.util.List;

import com.spring.photolib.webapp.domain.Tag;

public interface TagService {
	
	public Tag getTagByName(String name);
	public Tag getTagById(Integer tid);
	public Tag getTagAndPhotosById(Integer tid);
	public List<Tag> getSuggestedTags(String query);

}
