package com.spring.photolib.webapp.dao;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AlbumRating;
import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.PhotoRating;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AlreadyRatedException;
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
		
		
			album.setCreationDate(new Date());
			album.setLastUpdateDate(new Date());
			album.setAlbumSize(0);
			album.setViewCount(0);
			AlbumRating rating = new AlbumRating();
			rating.setRating(new Float(0.0));
			rating.setNumRatings(0);
			album.setRating(rating);
		
		album.setUser(userHelper.getCurrentUser(principal, sessionFactory));
		sessionFactory.getCurrentSession().save(album);

	}

	public void updateAlbum(Album album, Principal principal) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		try {
			album.setLastUpdateDate(dateFormater.parse(dateFormater
					.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		album.setUser(userHelper.getCurrentUser(principal, sessionFactory));
		if (album.getPhotos() != null && !album.getPhotos().isEmpty()) {
			album.setAlbumSize(album.getPhotos().size());
		} else {
			album.setAlbumSize(0);
		}

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

	public void updateAlbumViewcount(Album album) {
		sessionFactory.getCurrentSession().update(album);
	}

	public void rateAlbum(Album album, Integer rating, Principal principal)
			throws AlreadyRatedException {
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		Session session = sessionFactory.getCurrentSession();
		List<AlbumRating> ratings = session
				.createQuery(
						"from Album a where a.aid = ? AND a.rating.arid in"
								+ "(select r.arid from User u join u.albumRatings r where u.uid = ?)")
				.setParameter(0, album.getAid()).setParameter(1, user.getId())
				.list();
		if (ratings.isEmpty()) {
			User domainUser = (User) session.get(User.class, user.getId());
			AlbumRating newRating = (AlbumRating) session.get(
					AlbumRating.class, album.getRating().getArid());
			newRating.setNumRatings(newRating.getNumRatings() + 1);
			newRating.setRating((newRating.getRating() + rating));
			Set<User> userSet = newRating.getUsers();
			userSet.add(domainUser);
			album.setRating(newRating);
			session.update(newRating);

		} else {
			throw new AlreadyRatedException();
		}
	}

}
