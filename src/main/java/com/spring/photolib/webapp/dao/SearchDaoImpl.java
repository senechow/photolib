package com.spring.photolib.webapp.dao;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;
import com.spring.photolib.webapp.util.SortHelper;

@Repository
public class SearchDaoImpl implements SearchDao {

	@Autowired
	SessionFactory sessionFactory;
	private static final int MAX_PHOTOS_PER_PAGE = 6;

	public List<Photo> basicSearchPhotos(String query, Principal principal) {
		if (principal != null) {
			query = query.toLowerCase();
			AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
					.getPrincipal();
			return sessionFactory
					.getCurrentSession()
					.createQuery(
							"From Photo p where lower(p.name) LIKE lower(?) and (p.isPublic = true or (p.user.id = ?))")
					.setString(0, "%" + query + "%")
					.setParameter(1, user.getId()).list();
		}
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"From Photo p where lower(p.name) LIKE lower(?) AND p.isPublic = true")
				.setString(0, '%' + query + '%').list();
	}

	public List<Photo> advancedSearchPhotos(Search search, Principal principal) {
		removeTagBrackets(search);
		Query query;
		List<Photo> photoList = new ArrayList<Photo>();
		Session session = sessionFactory.getCurrentSession();
		search.setName("%" + search.getName().toLowerCase() + "%");
		if (search.getDescription().isEmpty()) {
			search.setDescription("%");
		} else {
			search.setDescription("%" + search.getDescription().toLowerCase()
					+ "%");
		}
		if(search.getCreatedSince() == null) {
			search.setCreatedSince(new Date());
		}

		if (principal != null) {
			AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
					.getPrincipal();

			if (search.getTags() != null && !search.getTags().isEmpty()) {
				query = session
						.createQuery(
								"From Photo p where lower(p.name) LIKE lower(?) AND lower(p.description) LIKE lower(?)"
										+ "AND p.creationDate <= ? AND (p.isPublic = true OR p.user.id = ?) AND p.id in"
										+ "(Select p2.id From Photo p2 join p2.tags t where t.name in (:tagNameList))")
						.setString(0, search.getName())
						.setString(1, search.getDescription())
						.setParameter(2, search.getCreatedSince())
						.setParameter(3, user.getId())
						.setParameterList("tagNameList", search.getTags());
			} else {
				query = session
						.createQuery(
								"from Photo p where lower(p.name) LIKE lower(?) AND lower(p.description) LIKE lower(?) "
										+ "AND p.creationDate <= ? AND (p.isPublic = true OR p.user.id = ?)")
						.setString(0, search.getName())
						.setString(1, search.getDescription())
						.setParameter(2, search.getCreatedSince())
						.setParameter(3, user.getId());
			}

		} else {
			if (search.getTags() != null && !search.getTags().isEmpty()) {
				query = session
						.createQuery(
								"From Photo p where lower(p.name) LIKE lower(?) AND lower(p.description) LIKE lower(?) "
										+ "AND p.creationDate <= ? AND p.isPublic = true AND p.isPublic = true AND p.id in"
										+ "(Select p2.id From Photo p2 join p2.tags t where t.name in (:tagNameList))")
						.setString(0, search.getName())
						.setString(1, search.getDescription())
						.setParameter(2, search.getCreatedSince())
						.setParameterList("tagNameList", search.getTags());
			} else {
				query = session
						.createQuery(
								"from Photo p where lower(p.name) LIKE lower(?) AND lower(p.description) LIKE lower(?) "
										+ "AND p.creationDate <= ? AND p.isPublic = true ")
						.setString(0, search.getName())
						.setString(1, search.getDescription())
						.setParameter(2, search.getCreatedSince());
			}

		}
		photoList = query.list();
		return photoList;
	}

	private void removeTagBrackets(Search search) {
		if (search.getTags() != null && !search.getTags().isEmpty()) {
			Set<String> tagSet = new HashSet<String>();
			for (String tag : search.getTags()) {
				tag = tag.replaceAll("\\[", "");
				tag = tag.replaceAll("\\]", "");
				tagSet.add(tag);
			}
			search.setTags(tagSet);
		}
	}
}
