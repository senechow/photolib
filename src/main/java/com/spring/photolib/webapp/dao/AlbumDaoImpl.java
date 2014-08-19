package com.spring.photolib.webapp.dao;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.util.UserHelper;

@Repository
public class AlbumDaoImpl implements AlbumDao {

	@Autowired
	private SessionFactory sessionFactory;
	private UserHelper userHelper = new UserHelper();

	public Album getAlbumById(Integer id) {
		Album album = (Album) sessionFactory.getCurrentSession().get(
				Album.class, id);
		return album;
	}

	public void createAlbum(Album album, Principal principal) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		try {
			album.setCreationDate(dateFormater.parse(dateFormater.format(new Date())));
			album.setLastUpdateDate(dateFormater.parse(dateFormater.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		album.setUser(userHelper.getCurrentUser(principal, sessionFactory));
		sessionFactory.getCurrentSession().save(album);

	}

	public void updateAlbum(Album album, Principal principal) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		try {
			album.setLastUpdateDate(dateFormater.parse(dateFormater.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		album.setUser(userHelper.getCurrentUser(principal, sessionFactory));
		sessionFactory.getCurrentSession().update(album);
	}

	public void removeAlbum(Album album) {
		sessionFactory.getCurrentSession().delete(album);
	}

	public List<Album> getAlbums(Principal principal) {
		if (principal != null) {
			AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
					.getPrincipal();
			return sessionFactory
					.getCurrentSession()
					.createQuery(
							"From Album a where a.isPublic = true or (a.user.id = :id)")
					.setParameter("id", user.getId()).list();
		}
		return sessionFactory.getCurrentSession()
				.createQuery("From Album where isPublic = true").list();
	}

	public Album getAlbumAndPhotosById(Integer id) {
		Album album = (Album) sessionFactory.getCurrentSession().get(
				Album.class, id);
		Hibernate.initialize(album.getPhotos());
		return album;

	}

}
