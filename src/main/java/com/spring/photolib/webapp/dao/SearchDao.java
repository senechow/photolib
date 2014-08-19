package com.spring.photolib.webapp.dao;

import java.security.Principal;
import java.util.List;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;

public interface SearchDao {
	
	public List<Photo> basicSearchPhotos(String query, Principal principal);
	public List<Photo> advancedSearchPhotos(Search search, int page, Principal principal);
}
