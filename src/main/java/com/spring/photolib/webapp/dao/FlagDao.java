package com.spring.photolib.webapp.dao;

import java.util.List;

import com.spring.photolib.webapp.domain.AlbumFlag;
import com.spring.photolib.webapp.domain.PhotoFlag;
import com.spring.photolib.webapp.domain.UserFlag;

public interface FlagDao {
	
	public void flagUser(Integer uid, UserFlag flag);
	public void flagPhoto(Integer pid, PhotoFlag flag);
	public void flagAlbum(Integer aid, AlbumFlag flag);
	public List<UserFlag> listFlaggedUsers();
	public List<PhotoFlag> listFlaggedPhotos();
	public List<AlbumFlag> listFlaggedAlbums();
	public List<UserFlag> listBannedUsers();
	public List<PhotoFlag> listFlaggedMostRecentTenPhotos();
	public List<PhotoFlag> listFlaggedMostRecentTenAlbums();
	public List<PhotoFlag> listFlaggedMostRecentTenUsers();
	public void deletePhotoFlag(Integer id);
	public void deleteAlbumFlag(Integer id);
	public void deleteUserFlag(Integer id);
	public void deleteFlag(Integer id);
}
