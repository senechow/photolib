package com.spring.photolib.webapp.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AlbumFlag;
import com.spring.photolib.webapp.domain.Flag;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.PhotoFlag;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.domain.UserFlag;

@Repository
public class FlagDaoImpl implements FlagDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	private static final int MAX_PHOTOS_PER_PAGE = 10;
	
	public void flagUser(Integer uid, UserFlag flag) {
		Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		try {
			flag.setCreationDate(dateFormater.parse(dateFormater
					.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User user = (User) session.get(User.class, uid);
		flag.setUser(user);
		Set<UserFlag> userFlags = user.getUserFlags();
		userFlags.add(flag);
		user.setUserFlags(userFlags);
		session.update(user);
	}

	public void flagPhoto(Integer pid, PhotoFlag flag) {
		Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		try {
			flag.setCreationDate(dateFormater.parse(dateFormater
					.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Photo photo = (Photo) session.get(Photo.class, pid);
		flag.setPhoto(photo);
		Set<PhotoFlag> photoFlags = photo.getPhotoFlags();
		photoFlags.add(flag);
		photo.setPhotoFlags(photoFlags);
		session.update(photo);
	}

	public void flagAlbum(Integer aid, AlbumFlag flag) {
		Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		try {
			flag.setCreationDate(dateFormater.parse(dateFormater
					.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Album album = (Album) session.get(Album.class, aid);
		flag.setAlbum(album);
		Set<AlbumFlag> albumFlags = album.getAlbumFlags();
		albumFlags.add(flag);
		album.setAlbumFlags(albumFlags);
		session.update(album);
	}

	public List<UserFlag> listFlaggedUsers() {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		query = session.createQuery("from UserFlag u where u.user.banned = false order by u.creationDate desc");
		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
		query.setFirstResult(0);
		return query.list();
	}
	
	public List<UserFlag> listBannedUsers() {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		query = session.createQuery("from UserFlag u where u.user.banned = true order by u.creationDate desc");
		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
		query.setFirstResult(0);
		return query.list();
	}


	public List<PhotoFlag> listFlaggedMostRecentTenPhotos() {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		query = session.createQuery("from PhotoFlag order by creationDate desc");
		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
		query.setFirstResult(0);
		return query.list();
	}


	public List<AlbumFlag> listFlaggedAlbums() {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		query = session.createQuery("from AlbumFlag order by creationDate desc");
		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
		query.setFirstResult(0);
		return query.list();
	}

	public List<PhotoFlag> listFlaggedPhotos() {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		query = session.createQuery("from PhotoFlag order by creationDate desc");
//		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
//		query.setFirstResult(page * MAX_PHOTOS_PER_PAGE);
		return query.list();
	}
	
	
	public List<PhotoFlag> listFlaggedMostRecentTenAlbums() {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		query = session.createQuery("from AlbumFlag order by creationDate desc");
//		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
//		query.setFirstResult(0);
		return query.list();
	}

	public List<PhotoFlag> listFlaggedMostRecentTenUsers() {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		query = session.createQuery("from UserFlag u where u.user.banned = false order by creationDate desc");
//		query.setMaxResults(MAX_PHOTOS_PER_PAGE);
//		query.setFirstResult(0);
		return query.list();
	}
	
	public void deleteFlag(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Flag flag = (Flag) session.get(Flag.class, id);
		session.delete(flag);
	}

	public void deletePhotoFlag(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		PhotoFlag photoFlag = (PhotoFlag) session.get(PhotoFlag.class, id);
		session.delete(photoFlag);
		
	}

	public void deleteAlbumFlag(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		AlbumFlag albumFlag = (AlbumFlag) session.get(AlbumFlag.class, id);
		session.delete(albumFlag);
		
	}

	public void deleteUserFlag(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		UserFlag userFlag = (UserFlag) session.get(UserFlag.class, id);
		session.delete(userFlag);
		
	}

}
