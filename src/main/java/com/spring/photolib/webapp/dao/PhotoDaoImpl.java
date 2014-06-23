package com.spring.photolib.webapp.dao;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.User;

@Repository
public class PhotoDaoImpl implements PhotoDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Photo> listPhotos(Principal principal) {
		if(principal != null) {
			AuthorizedUser user = (AuthorizedUser)((Authentication) principal).getPrincipal();
			return sessionFactory.getCurrentSession().createQuery("From Photo p where p.isPublic = true or (p.user.id = :id)").setParameter("id", user.getId()).list();
		}
		return sessionFactory.getCurrentSession().createQuery("From Photo where isPublic = true").list();
	}

	public void addPhoto(Photo photo, MultipartFile image, Principal principal) {
		Session session = sessionFactory.getCurrentSession();
		try{
			//Blob blob = Hibernate.getLobCreator(session).createBlob(image.getInputStream(), image.getSize());
			//photo.setImage(blob);
			photo.setImage(image.getBytes());
			photo.setImageContentType(image.getContentType());
			photo.setImageStoreName(image.getOriginalFilename());
			photo.setUser(getCurrentUser(principal));
			photo.setCreationDate(new Date());
			session.save(photo);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public void updatePhoto(Photo photo , MultipartFile image, Principal principal) {
		Session session = sessionFactory.getCurrentSession();
		try {
			photo.setImage(image.getBytes());
			photo.setImageContentType(image.getContentType());
			photo.setUser(getCurrentUser(principal));
			session.update(photo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void removePhoto(Integer pid) {
		Photo photo = (Photo) sessionFactory.getCurrentSession().load(Photo.class, pid);
		if(photo != null) {
			sessionFactory.getCurrentSession().delete(photo);
		}
	}

	public Photo getPhotoById(Integer pid) {
		Photo photo = (Photo) sessionFactory.getCurrentSession().createQuery("from Photo p where p.pid = :pid").setParameter("pid", pid).list().get(0);
		return photo;
	}
	
	private User getCurrentUser(Principal principal) {
		AuthorizedUser user = (AuthorizedUser)((Authentication) principal).getPrincipal();
		return (User) sessionFactory.getCurrentSession().get(User.class, user.getId());
	}


}
