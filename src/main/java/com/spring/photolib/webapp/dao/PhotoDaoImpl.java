package com.spring.photolib.webapp.dao;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.PhotoRating;
import com.spring.photolib.webapp.domain.Tag;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AlreadyRatedException;
import com.spring.photolib.webapp.util.UserHelper;

@Repository
public class PhotoDaoImpl implements PhotoDao {

	@Autowired
	private SessionFactory sessionFactory;
	private UserHelper userHelper = new UserHelper();

	public List<Photo> listPhotos(Principal principal) {
		Session session = sessionFactory.getCurrentSession();
		List<Photo> photos = new ArrayList<Photo>();
		Query query;

		// String orderBy = sortHelper.setOrderBy(sortType);

		if (principal != null) {
			AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
					.getPrincipal();
			String hql = "From Photo p where p.isPublic = true or (p.user.id = :id) ";
			query = session.createQuery(hql).setParameter("id", user.getId());

		} else {
			String hql = "From Photo p where p.isPublic = true ";
			query = session.createQuery(hql);
		}

		photos = query.list();
		return photos;
	}

	public void addPhoto(Photo photo, MultipartFile image, Principal principal) {
		Session session = sessionFactory.getCurrentSession();
		try {

			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
			photo.setImage(image.getBytes());
			photo.setImageContentType(image.getContentType());
			photo.setImageStoreName(image.getOriginalFilename());
			photo.setUser(userHelper.getCurrentUser(principal, sessionFactory));
			photo.setViewCount(0);
			PhotoRating rating = new PhotoRating();
			rating.setRating(new Float(0.0));
			rating.setNumRatings(0);
			photo.setRating(rating);
		
				photo.setCreationDate(new Date());
			
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
		try {
			File file = new File("Test."
					+ photo.getImageContentType().substring(
							photo.getImageContentType().indexOf("/") + 1));
			DiskFileItem fileItem = new DiskFileItem(photo.getName(),
					new String(photo.getImage(), "UTF-8"), true,
					photo.getName(), 1000000, file);
			fileItem.getOutputStream();
			MultipartFile multipartFile = new CommonsMultipartFile(
					fileItem);
			photo.setImageFile(multipartFile);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return photo;
	}

	public Photo getPhotoAndTagsById(Integer pid) {
		Photo photo = getPhotoById(pid);
		Hibernate.initialize(photo.getTags());
		return photo;
	}

	public void ratePhoto(Photo photo, Integer rating, Principal principal)
			throws AlreadyRatedException {
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		Session session = sessionFactory.getCurrentSession();
		List<PhotoRating> ratings = session
				.createQuery(
						"from Photo p where p.pid = ? AND p.rating.prid in"
								+ "(select r.prid from User u join u.photoRatings r where u.uid = ?)")
				.setParameter(0, photo.getPid()).setParameter(1, user.getId())
				.list();
		if (ratings.isEmpty()) {
			User domainUser = (User) session.get(User.class, user.getId());
			PhotoRating newRating = (PhotoRating) session.get(
					PhotoRating.class, photo.getRating().getPrid());
			newRating.setNumRatings(newRating.getNumRatings() + 1);
			newRating.setRating((newRating.getRating() + rating));
			Set<User> userSet = newRating.getUsers();
			userSet.add(domainUser);
			photo.setRating(newRating);
			session.update(newRating);

		} else {
			throw new AlreadyRatedException();
		}
	}

	public void updatePhotoViewCount(Photo photo) {
		sessionFactory.getCurrentSession().update(photo);

	}

}
