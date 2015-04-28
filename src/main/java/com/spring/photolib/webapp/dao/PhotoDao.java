package com.spring.photolib.webapp.dao;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.exception.AlreadyRatedException;

public interface PhotoDao {
	
	public List<Photo> listPhotos(Principal principal);
	public void addPhoto(Photo photo, MultipartFile image, Principal principal);
	public void updatePhoto(Photo photo , MultipartFile image, Principal principal);
	public void removePhoto(Integer pid);
	public Photo getPhotoById(Integer pid);
	public Photo getPhotoAndTagsById(Integer pid);
	public void ratePhoto(Photo photo, Integer rating, Principal principal) throws AlreadyRatedException;
	public void updatePhotoViewCount(Photo photo);
}
