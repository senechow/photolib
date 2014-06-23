package com.spring.photolib.webapp.service;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.photolib.webapp.dao.UserDao;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AccountAlreadyConfirmedException;
import com.spring.photolib.webapp.exception.ConfirmationMismatchException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Transactional
	public void createUser(User user) {
		userDao.createUser(user);

	}

	@Transactional
	public void updateUser(User user) {
		userDao.updateUser(user);

	}

	@Transactional
	public void removeUser(Integer id) {
		userDao.removeUser(id);

	}

	@Transactional
	public List<User> listUser() {
		return userDao.listUser();
	}

	@Transactional
	public User getUserInfo(Integer id) {
		return userDao.getUserInfo(id);
		
	}

	@Transactional
	public List<Photo> getUserPhotos(Integer uid) {
		return userDao.getUserPhotos(uid);
	}

	@Transactional
	public void activateAccount(User confirmUser, Integer uid) throws AccountAlreadyConfirmedException, ConfirmationMismatchException {
		userDao.activateAccount(confirmUser, uid);
	}

	@Transactional
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

}
