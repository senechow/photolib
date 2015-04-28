package com.spring.photolib.webapp.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.photolib.webapp.domain.Tag;

@Repository
public class TagDaoImpl implements TagDao {

	@Autowired
	SessionFactory sessionFactory;
	
	public Tag getTagByName(String name) {
		List<Tag> tagList = sessionFactory.getCurrentSession().createQuery("from Tag where name = :name").setParameter("name", name).list();
		return tagList.get(0);
	}

	public Tag getTagById(Integer tid) {
		return (Tag) sessionFactory.getCurrentSession().get(Tag.class, tid);
	}
	
	public Tag getTagAndPhotosById(Integer tid) {
		Tag tag = (Tag) sessionFactory.getCurrentSession().get(Tag.class, tid);
		Hibernate.initialize(tag.getPhotos());
		return tag;
	}

	public List<Tag> getSuggestedTags(String query) {
		return sessionFactory.getCurrentSession().createQuery("from Tag where name LIKE :query").setParameter("query", query + "%").list();
	}

}
