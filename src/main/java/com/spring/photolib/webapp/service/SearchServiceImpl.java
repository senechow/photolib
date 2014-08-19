package com.spring.photolib.webapp.service;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.photolib.webapp.dao.SearchDao;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;
	
	@Transactional
	public List<Photo> basicSearchPhotos(String query, Principal principal) {
		return searchDao.basicSearchPhotos(query, principal);
	}
	
	@Transactional
	public List<Photo> advancedSearchPhotos(Search search, int page, Principal principal){
		return searchDao.advancedSearchPhotos(search, page, principal);
	}

}
