package com.spring.photolib.webapp.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spring.photolib.webapp.dao.PhotoDao;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.exception.AlreadyRatedException;

@Service
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private PhotoDao photoDao;
	
	@Transactional
	public List<Photo> listPhotos(Principal principal) {
		return photoDao.listPhotos(principal);
	}

	@Transactional
	public void addPhoto(Photo photo, MultipartFile image, Principal principal) {
		photoDao.addPhoto(photo, image, principal);
	}

	@Transactional
	public void updatePhoto(Photo photo, MultipartFile image, Principal principal) {
		photoDao.updatePhoto(photo, image, principal);
		
	}

	@Transactional
	public void removePhoto(Integer pid) {
		photoDao.removePhoto(pid);
		
	}

	@Transactional
	public Photo getPhotoById(Integer pid) {
		return photoDao.getPhotoById(pid);
	}
	
	
	@Transactional
	public Photo getPhotoAndTagsById(Integer pid) {
		return photoDao.getPhotoAndTagsById(pid);
	}

//	@Transactional
//	public List<Photo> listPhotosAndSort(Principal principal, String sortType, int page) {
//		return photoDao.listPhotos(principal, page, sortType);
//	}
	
	@Transactional
	public void ratePhoto(Photo photo, Integer rating, Principal principal) throws AlreadyRatedException {
		photoDao.ratePhoto(photo, rating, principal);
	}

	@Transactional
	public void updatePhotoViewCount(Photo photo) {
		photoDao.updatePhotoViewCount(photo);
		
	}

}
