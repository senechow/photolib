package com.spring.photolib.webapp.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.spring.photolib.webapp.domain.Album;
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
	
	public List<Photo> getUserPhotosAndSort(Integer id, String sortType) {
		List photos = getUserPhotos(id);
		sortHelper.sortPhoto(sortType, photos);
		return photos;
	}
	
	private List<Photo> getUserPhotos(Integer id) {
		//List<Photo> photos = sessionFactory.getCurrentSession().createQuery("from Photo p inner join p.user as u where p.user.uid = :id").setParameter("id", id).list(); 
		List<Photo> photos = sessionFactory.getCurrentSession().createQuery("from Photo p where p.user.uid = :id").setParameter("id", id).list();
		return photos;
	}
	
	public User getUserByEmail(String email) {
		List <User> userList = sessionFactory.getCurrentSession().createQuery("from User u where u.emailAddress = :email_address").setString("email_address",email).list();
		if(userList.isEmpty()) {
			throw new UsernameNotFoundException("Error: User not found");
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

	public List<Album> getUserAlbums(Integer id) {
		List<Album> albums = sessionFactory.getCurrentSession().createQuery("from Album a where a.user.uid = :id").setParameter("id", id).list();
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
