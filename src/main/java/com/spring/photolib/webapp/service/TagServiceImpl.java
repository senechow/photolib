package com.spring.photolib.webapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.photolib.webapp.dao.TagDao;
import com.spring.photolib.webapp.domain.Tag;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;
	
	@Transactional
	public Tag getTagByName(String name) {
		return tagDao.getTagByName(name);
	}

	@Transactional
	public Tag getTagById(Integer tid) {
		return tagDao.getTagById(tid);
	}

	@Transactional
	public List<Tag> getSuggestedTags(String query) {
		return tagDao.getSuggestedTags(query);
	}
	
	@Transactional
	public Tag getTagAndPhotosById(Integer tid) {
		return tagDao.getTagAndPhotosById(tid);
	}

}
