package com.spring.photolib.webapp.service;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.photolib.webapp.dao.AlbumDao;
import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.exception.AlreadyRatedException;

@Service
public class AlbumServiceImpl implements AlbumService {

	@Autowired
	private AlbumDao albumDao;
	
	@Transactional
	public List<Album> getAlbums(Principal principal) {
		return albumDao.getAlbums(principal);
	}

	@Transactional
	public Album getAlbumById(Integer id) {
		return albumDao.getAlbumById(id);
	}

	@Transactional
	public void createAlbum(Album album, Principal principal) {
		albumDao.createAlbum(album, principal);
	}

	@Transactional
	public void updateAlbum(Album album, Principal principal) {
		albumDao.updateAlbum(album, principal);
	}

	@Transactional
	public void removeAlbum(Album album) {
		albumDao.removeAlbum(album);

	}
	
	@Transactional
	public Album getAlbumAndPhotosById(Integer id){
		return albumDao.getAlbumAndPhotosById(id);
	}
	
	@Transactional
	public void updateAlbumViewcount(Album album) {
		albumDao.updateAlbumViewcount(album);
	}
	
	@Transactional
	public void rateAlbum(Album album, Integer rating, Principal principal) throws AlreadyRatedException {
		albumDao.rateAlbum(album, rating, principal);
	}

}
