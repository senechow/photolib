package com.spring.photolib.webapp.dao;


import java.util.List;
import java.util.Set;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AccountAlreadyConfirmedException;
import com.spring.photolib.webapp.exception.ConfirmationMismatchException;

public interface UserDao {
	
	public List<User> listUser();
	public void createUser(User user);
	public void updateUser(User user);
	public void removeUser(Integer id);
	public User getUserInfo(Integer id);
	public List<Photo> getUserPhotos(Integer id);
	public User getUserByEmail(String email);
	public void activateAccount(User confirmUser, Integer uid) throws AccountAlreadyConfirmedException, ConfirmationMismatchException;
}
