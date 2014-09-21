package com.spring.photolib.webapp.service;

import java.security.Principal;
import java.util.List;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;

public interface SearchService {
	
	public List<Photo> basicSearchPhotos(String query, Principal principal);
	public List<Photo> advancedSearchPhotos(Search query, Principal principal);
	
}
