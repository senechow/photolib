package com.spring.photolib.webapp.service;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.photolib.webapp.domain.Photo;

public interface PhotoService {
	
	public List<Photo> listPhotos(Principal principal);
	public void addPhoto(Photo photo, MultipartFile image, Principal principal);
	public void updatePhoto(Photo photo, MultipartFile image, Principal principal);
	public void removePhoto(Integer pid);
	public Photo getPhotoById(Integer pid);

}
