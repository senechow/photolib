package com.spring.photolib.webapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AlbumFlag;
import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.PhotoFlag;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.domain.UserFlag;
import com.spring.photolib.webapp.exception.UnauthorizedException;
import com.spring.photolib.webapp.service.AlbumService;
import com.spring.photolib.webapp.service.FlagService;
import com.spring.photolib.webapp.service.PhotoService;
import com.spring.photolib.webapp.service.UserService;

@Controller
public class FlagController {

	@Autowired
	FlagService flagService;
	@Autowired
	UserService userService;
	@Autowired
	PhotoService photoService;
	@Autowired
	AlbumService albumService;
	
	private Logger logger = Logger.getLogger(FlagController.class);

	private static final String[] PHOTO_FLAG_REASONS = {
			"Inappropriate language", "Inappropriate image",
			"Other (Please specify in description)" };

	private static final String[] USER_FLAG_REASONS = { "Inappropriate name",
			"Spamming", "Posting inappropriate material",
			"Other (Please specify in description)" };

	private static final String[] ALBUM_FLAG_REASONS = { "Inappropriate language",
		"Inappropriate images",
		"Other (Please specify in description)" };
	
	@RequestMapping(value = "/photo/{pid}/flag")
	public String flagPhoto(@PathVariable Integer pid, ModelMap map) {
		logger.info("Directing the user to the flag photo page for flag with id "
				+ pid);
		map.addAttribute("flag", new PhotoFlag());
		map.addAttribute("photo", photoService.getPhotoAndTagsById(pid));
		map.addAttribute("flaggingReasons", PHOTO_FLAG_REASONS);
		return "flags/flagphoto";
	}

	@RequestMapping(value = "/photo/{pid}/confirmflag")
	public String confirmFlagPhoto(
			@ModelAttribute("flag") @Valid PhotoFlag flag,
			BindingResult result, @PathVariable Integer pid,
			Principal principal, ModelMap map) {
		logger.info("Flagging photo with id " + pid);
		if (principal == null) {
			logger.error("Unauthorized attempt by an unregistered user to flag photo with id "
					+ pid);
			throw new UnauthorizedException();
		}
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			getErrors(result, errors);
			map.addAttribute("flaggingReasons", PHOTO_FLAG_REASONS);
			logger.error("There were form errors when flagging photo with id "
					+ pid + " redirecting user to flag photo page");
			map.addAttribute("errors", errors);
			return "flags/flagphoto";
		}

		flagService.flagPhoto(pid, flag);
		logger.info("The photo with id " + pid
				+ " was successfully flagged.");
		map.addAttribute("message", "label.message.flag.flagphotosuccessful");
		map.addAttribute("photo", photoService.getPhotoAndTagsById(pid));
		return "photos/viewphoto";
	}

	@RequestMapping(value = "/users/{uid}/flag")
	public String flagUser(@PathVariable Integer uid, ModelMap map) {
		logger.info("Directing the user to the flag user page for flag with id "
				+ uid);
		map.addAttribute("flag", new UserFlag());
		User user = userService.getUserInfo(uid);
		user.setPassword("");
		user.setPasswordConfirm("");
		user.setPasswordOld("");
		user.setConfirmationCode("");
		user.setStoredConfirmationCode("");
		map.addAttribute("user", user);
		map.addAttribute("flaggingReasons", USER_FLAG_REASONS);
		return "flags/flaguser";
	}

	@RequestMapping(value = "/user/{uid}/confirmflag")
	public String confirmFlagUser(@ModelAttribute("flag") @Valid UserFlag flag,
			BindingResult result, @PathVariable Integer uid,
			Principal principal, ModelMap map) {
		logger.info("Flagging user with id " + uid);
		if (principal == null) {
			logger.error("Unauthorized attempt by an unregistered user to flag user with id "
					+ uid);
			throw new UnauthorizedException();
		} else if (getCurrentUser(principal).getId() == uid) {
			logger.error("A user has attempted to flag themselves");
			List<String> errors = new ArrayList<String>();
			errors.add("error.flag.flagself");
			map.addAttribute("errors", errors);
			map.addAttribute("user", userService.getUserInfo(uid));
			return "users/viewuser";
		} else if (result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			getErrors(result, errors);
			map.addAttribute("flaggingReasons", USER_FLAG_REASONS);
			logger.error("There were form errors when flagging user with id "
					+ uid + " redirecting user to flag user page");
			map.addAttribute("errors", errors);
			return "flags/flaguser";
		} else {
			flagService.flagUser(uid, flag);
			logger.info("The User with id " + uid
					+ " was successfully flagged.");
			map.addAttribute("message", "label.message.flag.flagusersuccessful");
			map.addAttribute("user", userService.getUserInfo(uid));
			return "users/viewuser";
		}
	}
	
	@RequestMapping(value = "/album/{aid}/flag")
	public String flagAlbum(@PathVariable Integer aid, ModelMap map) {
		logger.info("Directing the user to the flag album page for flag with id "
				+ aid);
		map.addAttribute("flag", new AlbumFlag());
		map.addAttribute("album", albumService.getAlbumById(aid));
		map.addAttribute("flaggingReasons", ALBUM_FLAG_REASONS);
		return "flags/flagalbum";
	}
	
	@RequestMapping(value = "/album/{aid}/confirmflag")
	public String confirmFlagAlbum(@ModelAttribute("flag") @Valid AlbumFlag flag,
			BindingResult result, @PathVariable Integer aid,
			Principal principal, ModelMap map) {
		logger.info("Flagging album with id " + aid);
		if (principal == null) {
			logger.error("Unauthorized attempt by an unregistered user to flag albumr with id "
					+ aid);
			throw new UnauthorizedException();
		}else if (result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			getErrors(result, errors);
			map.addAttribute("flaggingReasons", ALBUM_FLAG_REASONS);
			logger.error("There were form errors when flagging album with id "
					+ aid + " redirecting user to flag album page");
			map.addAttribute("errors", errors);
			return "flags/flaguser";
		} else {
			flagService.flagAlbum(aid, flag);
			logger.info("The User with id " + aid
					+ " was successfully flagged.");
			Album album = albumService.getAlbumAndPhotosById(aid);
			map.addAttribute("message", "label.message.flag.flagalbumsuccessful");
			map.addAttribute("album", album);
			Set<Photo> photoList = buildPhotoList(album, principal);
			map.addAttribute("photoList", photoList);
			return "albums/viewalbum";
		}
	}

	private void getErrors(BindingResult result, List<String> errors) {
		for (ObjectError objError : result.getAllErrors()) {
			if (!errors.contains(objError.getCode()))
				for (String errorCode : objError.getCodes()) {
					if (errorCode.contains(".flag.")) {
						errors.add(errorCode);
						break;
					}
				}
		}
	}

	private AuthorizedUser getCurrentUser(Principal principal) {
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		return user;
	}
	
	private Set<Photo> buildPhotoList(Album album, Principal principal) {
		AuthorizedUser user = getCurrentUser(principal);
		Set<Photo> photoList = album.getPhotos();
		if(user == null || user.getId() != album.getUser().getUid()) {
			Set<Photo> publicPhotoList = new HashSet <Photo>();
			for(Photo photo : photoList) {
				if(photo.getIsPublic()) {
					publicPhotoList.add(photo);
				}
			}
			return publicPhotoList;
		}
		else {
			return photoList;
		}
		
	}


}
