package com.spring.photolib.webapp.dao;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Role;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AccountAlreadyConfirmedException;
import com.spring.photolib.webapp.exception.ConfirmationMismatchException;
import com.spring.photolib.webapp.util.SortHelper;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	private SortHelper sortHelper = new SortHelper();
	
	private static final int MAX_PHOTOS_PER_PAGE = 6;

	public void createUser(User user) {
		Session session = sessionFactory.getCurrentSession(); 
		user.setRole((Role) session.createQuery("from Role where role = 'registered_user'").list().get(0));
		user.setActivated(false);
		user.setCreationDate(new Date());
		user.setBanned(false);
		if(!user.getFirstName().isEmpty()){
			user.setFirstName(user.getFirstName().substring(0,1).toUpperCase() + user.getFirstName().substring(1));
		}
		if(!user.getLastName().isEmpty()){
			user.setLastName(user.getLastName().substring(0,1).toUpperCase() + user.getLastName().substring(1));
		}
		sessionFactory.getCurrentSession().save(user);
	}

	public void updateUser(User user) {
		if(!user.getFirstName().isEmpty()){
			user.setFirstName(user.getFirstName().substring(0,1).toUpperCase() + user.getFirstName().substring(1));
		}
		if(!user.getLastName().isEmpty()){
			user.setLastName(user.getLastName().substring(0,1).toUpperCase() + user.getLastName().substring(1));
		}
		sessionFactory.getCurrentSession().update(user);

	}

	public void removeUser(Integer id) {
		User user = (User) sessionFactory.getCurrentSession().load(User.class, id);
		if(user != null) {
			sessionFactory.getCurrentSession().delete(user);
		}

	}

	public List<User> listUser() {
		return sessionFactory.getCurrentSession().createQuery("from User").list();
	}

	public User getUserInfo(Integer id) {
		return (User) sessionFactory.getCurrentSession().get(User.class, id);
	}
	
	public List<Photo> getUserPhotos(Integer id, Principal principal) {
		if(principal != null) {
			AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
					.getPrincipal();
			if(user.getId().equals(id)) {
				Query query = sessionFactory.getCurrentSession().createQuery("from Photo p where p.user.uid = :id ").setParameter("id", id);
				List<Photo> photos = query.list();
				return photos;
			}
		}
		Query query = sessionFactory.getCurrentSession().createQuery("from Photo p where p.user.uid = :id and p.isPublic = true").setParameter("id", id);
		List<Photo> photos = query.list();
		return photos;
	}
	
	public User getUserByEmail(String email) throws  UsernameNotFoundException{
		List <User> userList = sessionFactory.getCurrentSession().createQuery("from User u where u.emailAddress = :email_address").setString("email_address",email).list();
		if(userList.isEmpty()) {
			return null;
		}
		return (User) userList.get(0);
	}

	public void activateAccount(User confirmUser, Integer uid) throws AccountAlreadyConfirmedException, ConfirmationMismatchException {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.get(User.class, uid);
		if(user.getActivated() || (user.getConfirmationCode() != null && user.getStoredConfirmationCode().equals(user.getConfirmationCode()))) {
			throw new AccountAlreadyConfirmedException();
		}
		else if(confirmUser.getConfirmationCode().equals(user.getStoredConfirmationCode())) {
			user.setConfirmationCode(confirmUser.getConfirmationCode());
			user.setActivated(true);
			session.update(user);	
		}
		else {
			throw new ConfirmationMismatchException();
		}
	}

	public List<Album> getUserAlbums(Integer id, Principal principal) {
		if(principal != null) {
			AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
					.getPrincipal();
			if(user.getId().equals(id)) {
				List<Album> albums = sessionFactory.getCurrentSession().createQuery("from Album a where a.user.uid = :id").setParameter("id", id).list();
				return albums;
			}
		}
		List<Album> albums = sessionFactory.getCurrentSession().createQuery("from Album a where a.user.uid = :id and a.isPublic = true").setParameter("id", id).list();
		return albums;
	}

	public void banUser(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.get(User.class, id);
		user.setBanned(true);
		session.update(user);	
	}

	public void unbanUser(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.get(User.class, id);
		user.setBanned(false);
		session.update(user);
	}
}
