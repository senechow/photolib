package com.spring.photolib.webapp.dao;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	public List<Photo> getUserPhotos(Integer id, Principal principal);

	public List<Album> getUserAlbums(Integer id, Principal principal);

	public User getUserByEmail(String email) throws  UsernameNotFoundException;

	public void activateAccount(User confirmUser, Integer uid)
			throws AccountAlreadyConfirmedException,
			ConfirmationMismatchException;
	
	public void banUser(Integer id);
	public void unbanUser(Integer id);

}
