package com.spring.photolib.webapp.service;

import java.util.List;
import java.util.Set;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AccountAlreadyConfirmedException;
import com.spring.photolib.webapp.exception.ConfirmationMismatchException;

public interface UserService {
	
	public List<User> listUser();
	public void createUser(User user);
	public void updateUser(User user);
	public void removeUser(Integer id);
	public User getUserInfo(Integer id);
	public List<Photo> getUserPhotos(Integer uid);
	public void activateAccount(User confirmUser, Integer uid) throws AccountAlreadyConfirmedException, ConfirmationMismatchException;
	public User getUserByEmail(String email);
	
}
