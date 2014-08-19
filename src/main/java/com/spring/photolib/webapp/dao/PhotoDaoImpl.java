package com.spring.photolib.webapp.dao;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Rating;
import com.spring.photolib.webapp.domain.Tag;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.PhotoAlreadyRatedException;
import com.spring.photolib.webapp.util.SortHelper;
import com.spring.photolib.webapp.util.UserHelper;

@Repository
public class PhotoDaoImpl implements PhotoDao {

	@Autowired
	private SessionFactory sessionFactory;
	private UserHelper userHelper = new UserHelper();
	private SortHelper sortHelper = new SortHelper();
	
	private static final int MAX_PHOTOS_PER_PAGE = 6;

	public List<Photo> listPhotos(Principal principal, int page, String sortType) {
		Session session = sessionFactory.getCurrentSession();
		List<Photo> photos = new ArrayList<Photo> ();
		String hql;
		Query query;
		
		String orderBy = sortHelper.setOrderBy(sortType);

		if (principal != null) {
			AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
					.getPrincipal();
			hql = "From Photo p where p.isPublic = true or (p.user.id = :id) " + orderBy;
			query = session.createQuery(hql).setParameter("id", user.getId());

		} else {
			hql = "From Photo p where p.isPublic = true " + orderBy;
			query = session.createQuery(hql);
		}
		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
		query.setFirstResult(page * MAX_PHOTOS_PER_PAGE);
		photos = query.list();
		return photos;
	}

	public void addPhoto(Photo photo, MultipartFile image, Principal principal) {
		Session session = sessionFactory.getCurrentSession();
		try {
			// Blob blob =
			// Hibernate.getLobCreator(session).createBlob(image.getInputStream(),
			// image.getSize());
			// photo.setImage(blob);
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
			photo.setImage(image.getBytes());
			photo.setImageContentType(image.getContentType());
			photo.setImageStoreName(image.getOriginalFilename());
			photo.setUser(userHelper.getCurrentUser(principal, sessionFactory));
			Rating rating = new Rating();
			rating.setRating(new Float(0.0));
			rating.setNumRatings(0);
			photo.setRating(rating);
			try {
				photo.setCreationDate(dateFormater.parse(dateFormater
						.format(new Date())));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (photo.getTags() != null && !photo.getTags().isEmpty()) {
				for (Tag tag : photo.getTags()) {
					if (tag != null) {
						List<Tag> dbTags = session
								.createQuery("from Tag where name = :name")
								.setParameter("name", tag.getName()).list();
						if (!dbTags.isEmpty()) {
							tag.setTid(dbTags.get(0).getTid());
						} else {
							session.save(tag);
						}
					}

				}
			}
			session.save(rating);
			session.save(photo);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updatePhoto(Photo photo, MultipartFile image,
			Principal principal) {
		Session session = sessionFactory.getCurrentSession();
		try {
			photo.setImage(image.getBytes());
			photo.setImageContentType(image.getContentType());
			photo.setUser(userHelper.getCurrentUser(principal, sessionFactory));
			if (photo.getTags() != null && !photo.getTags().isEmpty()) {
				for (Tag tag : photo.getTags()) {
					if (tag != null) {
						List<Tag> dbTags = session
								.createQuery("from Tag where name = :name")
								.setParameter("name", tag.getName()).list();
						if (!dbTags.isEmpty()) {
							tag.setTid(dbTags.get(0).getTid());
						} else {
							session.save(tag);
						}
					}

				}
			}
			session.update(photo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removePhoto(Integer pid) {
		Photo photo = (Photo) sessionFactory.getCurrentSession().load(
				Photo.class, pid);
		if (photo != null) {
			Set<Album> albums = photo.getAlbums();
			for (Album album : albums) {
				album.getPhotos().remove(photo);
				sessionFactory.getCurrentSession().update(album);
			}
			sessionFactory.getCurrentSession().delete(photo);
		}
	}

	public Photo getPhotoById(Integer pid) {
		Photo photo = (Photo) sessionFactory.getCurrentSession()
				.createQuery("from Photo p where p.pid = :pid")
				.setParameter("pid", pid).list().get(0);
		return photo;
	}

	public Photo getPhotoAndTagsById(Integer pid) {
		Photo photo = (Photo) sessionFactory.getCurrentSession()
				.createQuery("from Photo p where p.pid = :pid")
				.setParameter("pid", pid).list().get(0);
		Hibernate.initialize(photo.getTags());
		return photo;
	}

	public void ratePhoto(Photo photo, Integer rating, Principal principal)
			throws PhotoAlreadyRatedException {
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		Session session = sessionFactory.getCurrentSession();
		List<Rating> ratings = session
				.createQuery(
						"from Photo p where p.pid = ? AND p.rating.rtid in"
								+ "(select r.rtid from User u join u.ratings r where u.uid = ?)")
				.setParameter(0, photo.getPid()).setParameter(1, user.getId())
				.list();
		if (ratings.isEmpty()) {
			User domainUser = (User) session.get(User.class, user.getId());
			Rating newRating = (Rating) session.get(Rating.class, photo.getRating().getRtid());
			newRating.setNumRatings(newRating.getNumRatings() + 1);
			newRating.setRating((newRating.getRating() + rating));
			Set<User> userSet = newRating.getUsers();
			userSet.add(domainUser);
			photo.setRating(newRating);
			session.update(newRating);
		
		} else {
			throw new PhotoAlreadyRatedException();
		}
	}

}
