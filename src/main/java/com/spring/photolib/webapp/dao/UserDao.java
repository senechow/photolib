package com.spring.photolib.webapp.dao;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.spring.photolib.webapp.domain.Album;
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

	public List<Photo> getUserPhotosAndSort(Integer id, String sortType);

	public List<Album> getUserAlbums(Integer id);

	public User getUserByEmail(String email);

	public void activateAccount(User confirmUser, Integer uid)
			throws AccountAlreadyConfirmedException,
			ConfirmationMismatchException;
	
	public void banUser(Integer id);
	public void unbanUser(Integer id);

}
