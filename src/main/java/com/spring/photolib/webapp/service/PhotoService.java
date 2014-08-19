package com.spring.photolib.webapp.service;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.exception.PhotoAlreadyRatedException;

public interface PhotoService {
	
	public List<Photo> listPhotos(Principal principal, int page);
	public List<Photo> listPhotosAndSort(Principal principal, String sortType, int page);
	public void addPhoto(Photo photo, MultipartFile image, Principal principal);
	public void updatePhoto(Photo photo, MultipartFile image, Principal principal);
	public void removePhoto(Integer pid);
	public Photo getPhotoById(Integer pid);
	public Photo getPhotoAndTagsById(Integer pid);
	public void ratePhoto(Photo photo, Integer rating, Principal principal) throws PhotoAlreadyRatedException;
}
