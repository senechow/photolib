package com.spring.photolib.webapp.service;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.exception.AlreadyRatedException;

public interface AlbumService {
	
	public List<Album> getAlbums(Principal principal);
	public Album getAlbumById(Integer id);
	public void createAlbum(Album album, Principal principal);
	public void updateAlbum(Album album, Principal principal);
	public void removeAlbum(Album album);
	public Album getAlbumAndPhotosById(Integer id);
	public void updateAlbumViewcount(Album album);
	public void rateAlbum(Album album, Integer rating, Principal principal) throws AlreadyRatedException;

}
