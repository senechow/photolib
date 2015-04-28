package com.spring.photolib.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.photolib.webapp.dao.FlagDao;
import com.spring.photolib.webapp.domain.AlbumFlag;
import com.spring.photolib.webapp.domain.PhotoFlag;
import com.spring.photolib.webapp.domain.UserFlag;

@Service
public class FlagServiceImpl implements FlagService {
	
	@Autowired
	private FlagDao flagDao;

	@Transactional
	public void flagUser(Integer uid, UserFlag flag) {
		flagDao.flagUser(uid, flag);

	}

	@Transactional
	public void flagPhoto(Integer pid, PhotoFlag flag) {
		flagDao.flagPhoto(pid, flag);

	}
	
	@Transactional
	public void flagAlbum(Integer aid, AlbumFlag flag) {
		flagDao.flagAlbum(aid, flag);
	}

	@Transactional
	public List<UserFlag> listFlaggedUsers() {
		return flagDao.listFlaggedUsers();
	}

	@Transactional
	public List<PhotoFlag> listFlaggedPhotos() {
		return flagDao.listFlaggedPhotos();
	}

	@Transactional
	public List<AlbumFlag> listFlaggedAlbums() {
		return flagDao.listFlaggedAlbums();
	}
	
	@Transactional
	public List<UserFlag> listBannedUsers() {
		return flagDao.listBannedUsers();
	}
	
	@Transactional
	public List<PhotoFlag> listFlaggedMostRecentTenPhotos() {
		return flagDao.listFlaggedMostRecentTenPhotos();
	}
	
	@Transactional
	public List<PhotoFlag> listFlaggedMostRecentTenAlbums() {
		return flagDao.listFlaggedMostRecentTenAlbums();
	}
	
	@Transactional
	public List<PhotoFlag> listFlaggedMostRecentTenUsers() {
		return flagDao.listFlaggedMostRecentTenUsers();
	}

	@Transactional
	public void deletePhotoFlag(Integer id) {
		flagDao.deletePhotoFlag(id);
		
	}

	@Transactional
	public void deleteAlbumFlag(Integer id) {
		flagDao.deleteAlbumFlag(id);
		
	}

	@Transactional
	public void deleteUserFlag(Integer id) {
		flagDao.deleteUserFlag(id);
		
	}

	@Transactional
	public void deleteFlag(Integer id) {
		flagDao.deleteFlag(id);
		
	}


}
