package com.spring.photolib.webapp.dao;

import java.util.List;

import com.spring.photolib.webapp.domain.Tag;

public interface TagDao {
	
	public Tag getTagByName(String name);
	public Tag getTagById(Integer tid);
	public Tag getTagAndPhotosById(Integer tid);
	public List<Tag> getSuggestedTags(String query);

}
