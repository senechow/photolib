package com.spring.photolib.webapp.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.photolib.webapp.dao.UserDao;
import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AccountAlreadyConfirmedException;
import com.spring.photolib.webapp.exception.ConfirmationMismatchException;
import com.spring.photolib.webapp.util.SortTypes;

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
		return userDao.getUserPhotosAndSort(uid, SortTypes.MOST_RECENT.toString());
	}

	@Transactional
	public void activateAccount(User confirmUser, Integer uid) throws AccountAlreadyConfirmedException, ConfirmationMismatchException {
		userDao.activateAccount(confirmUser, uid);
	}

	@Transactional
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Transactional
	public List<Album> getUserAlbums(Integer uid) {
		return userDao.getUserAlbums(uid);
	}

	@Transactional
	public List<Photo> getUserPhotosAndSort(Integer uid, String sortType) {
		return userDao.getUserPhotosAndSort(uid,sortType);
	}
	
	@Transactional
	public void changePassword(User oldUserInfo, User newUserInfo) {
		oldUserInfo.setPassword(newUserInfo.getPassword());
		oldUserInfo.setPasswordConfirm(newUserInfo.getPasswordConfirm());
		oldUserInfo.setPasswordOld(newUserInfo.getPassword());
		userDao.updateUser(oldUserInfo);
	}
	
	@Transactional
	public void resetPassword(User user) {
		SecureRandom rand = new SecureRandom();
		rand.setSeed(new byte[16]);
		String newPassword = generateRandomString(rand.nextInt(15) + 20) ;
		user.setPassword(newPassword);
		user.setPasswordConfirm(newPassword);
		user.setPasswordOld(newPassword);
		userDao.updateUser(user);
	}
	
	@Transactional
	public void banUser(Integer id) {
		userDao.banUser(id);
	}
	
	@Transactional
	public void unbanUser(Integer id) {
		userDao.unbanUser(id);
	}
	
	private String generateRandomString(int length) {

		StringBuffer buffer = new StringBuffer();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		
		int charactersLength = characters.length();

		try {
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
			for (int i = 0; i < length; i++) {
				double index = rand.nextInt(2) * charactersLength;
				buffer.append(characters.charAt((int) index));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		
		return buffer.toString();
	}


}
