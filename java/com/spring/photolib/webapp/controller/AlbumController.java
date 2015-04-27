package com.spring.photolib.webapp.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;
import com.spring.photolib.webapp.exception.AlreadyRatedException;
import com.spring.photolib.webapp.exception.UnauthorizedException;
import com.spring.photolib.webapp.service.AlbumService;
import com.spring.photolib.webapp.service.PhotoService;
import com.spring.photolib.webapp.util.AlbumHelper;
import com.spring.photolib.webapp.util.SortHelper;
import com.spring.photolib.webapp.util.SortTypes;
import com.spring.photolib.webapp.validator.PhotoValidator;

@Controller
public class AlbumController {

	@Autowired
	private AlbumService albumService;
	@Autowired
	private PhotoService photoService;
	private AlbumHelper albumHelper = new AlbumHelper();
	private SortHelper sortHelper = new SortHelper();

	private Logger logger = Logger.getLogger(AlbumController.class);

	@RequestMapping(value = "/album", method = RequestMethod.GET)
	public String listPublicAlbums(Principal principal, ModelMap map,
			HttpServletRequest request) {
		logger.info("Retrieving public album list.");

		PagedListHolder<Album> albumList = new PagedListHolder<Album>(
				albumService.getAlbums(principal), new MutableSortDefinition(
						sortHelper.getDefaultSortPhoto(), true, true));
		albumHelper.addAlbumsAndNewSearchToMap(albumList, request, map,
				SortTypes.MOST_RECENT.toString());
		logger.info("Retrieval of public albums is complete");
		return "albums/album";

	}
	
	@RequestMapping(value = "/album/sort", method = RequestMethod.GET)
	public String sortsAlbums(@ModelAttribute("albumSearch") Search search,
			BindingResult result, ModelMap map,
			HttpServletRequest request, Principal principal) {

		logger.info("Sorting all albums");
		PagedListHolder<Album> albumList = new PagedListHolder<Album>(
				albumService.getAlbums(principal),
				sortHelper.getSortDefinition(search.getSortType()));
		albumHelper.addAlbumsAndExistingSearchToMap(albumList, request, map,
				search);
		logger.info("Successfully sorted all albums");
		return "albums/album";
	}

	@RequestMapping(value = "/morealbums", method = RequestMethod.GET)
	public String moreAlbums(
			ModelMap map,
			@RequestParam(value = "sortType", defaultValue = "Most Recent", required = false) String sortType,
			HttpServletRequest request) {
		logger.info("Retrieving more albums");
		PagedListHolder<Album> albumList = (PagedListHolder) request
				.getSession().getAttribute("session_albumlist");
		albumHelper.addMoreAlbumsAndNewSearchToMap(albumList, map, sortType);
		logger.info("Retrieval of more albums completed");
		return "albums/_album";
	}

	@RequestMapping(value = "/album/new", method = RequestMethod.GET)
	public String newAlbum(ModelMap map) {
		logger.info("Directing to the album creation page.");
		map.addAttribute("album", new Album());
		return "albums/createalbum";
	}

	@RequestMapping(value = "/album/new/create", method = RequestMethod.POST)
	public String createAlbum(@ModelAttribute("album") @Valid Album album,
			BindingResult result, Principal principal, ModelMap map) {

		logger.info("Creating a new album.");
		List<String> errors = new ArrayList<String>();
		boolean hasErrors = albumHasErrors(errors, result);

		if (hasErrors) {
			logger.error("Form errors were found during album creation. Redirecting user back to album creation page.");
			map.addAttribute("errors", errors);
			return "albums/createalbum";
		} else {
			albumService.createAlbum(album, principal);
			map.addAttribute("message",
					"label.message.album.creationsuccessful");

			logger.info("Successfully created album with id " + album.getAid());
			return "albums/viewalbum";
		}
	}

	@RequestMapping(value = "/album/{aid}/edit", method = RequestMethod.GET)
	public String editAlbum(@PathVariable Integer aid, ModelMap map) {
		logger.info("Editing album with id " + aid);
		map.addAttribute("album", albumService.getAlbumById(aid));
		return "albums/editalbum";
	}

	@RequestMapping(value = "/album/{aid}", method = RequestMethod.GET)
	public String viewAlbum(@PathVariable Integer aid, ModelMap map,
			Principal principal) {
		logger.info("Retrieving album with id " + aid);
		Album album = albumService.getAlbumAndPhotosById(aid);
		album.setViewCount(album.getViewCount() + 1);
		albumService.updateAlbumViewcount(album);
		if (album.getDescription().equals("")) {
			album.setDescription("No description is available at this time");
		}
		Set<Photo> photoList = buildPhotoList(album, principal);
		map.addAttribute("album", album);
		map.addAttribute("photoList", photoList);
		logger.info("Album with id " + aid + " was successfully retrieved");
		return "albums/viewalbum";
	}

	@RequestMapping(value = "/album/{aid}", method = RequestMethod.POST)
	public String updateAlbum(@ModelAttribute("album") @Valid Album album,
			BindingResult result, @PathVariable Integer aid,
			Principal principal, ModelMap map) {

		logger.info("Updating album with id " + album.getAid());
		Album oldAlbum = albumService.getAlbumAndPhotosById(aid);
		if (!correctUser(oldAlbum, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ getCurrentUser(principal).getId()
					+ " to edit album with id " + oldAlbum.getAid());
			throw new UnauthorizedException();
		} else {
			List<String> errors = new ArrayList<String>();

			boolean hasErrors = albumHasErrors(errors, result);

			if (hasErrors) {
				logger.error("There were form errors when editting album with id "
						+ album.getAid()
						+ " redirecting user to edit album page");
				map.addAttribute("errors", errors);
				return "albums/editalbum";
			} else {
				album.setPhotos(oldAlbum.getPhotos());
				albumService.updateAlbum(album, principal);
				Set<Photo> photoList = buildPhotoList(album, principal);
				map.addAttribute("message",
						"label.message.album.updatesuccessful");
				map.addAttribute("photoList", photoList);
				logger.info("Successfully updated album with id "
						+ album.getAid());
				return "albums/viewalbum";
			}
		}
	}

	@RequestMapping(value = "/album/{aid}", method = RequestMethod.DELETE)
	public String deleteAlbum(@PathVariable Integer aid, Principal principal,
			ModelMap map, RedirectAttributes redirectAttributes) {
		logger.info("Deleting album with id " + aid);
		Album album = albumService.getAlbumById(aid);
		if (!correctUser(album, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ getCurrentUser(principal).getId()
					+ " to delete album with id " + aid);
			throw new UnauthorizedException();
		} else {
			albumService.removeAlbum(album);
			redirectAttributes.addFlashAttribute("message",
					"label.message.album.deletesuccessful");
			logger.info("Photo with id " + aid + "was successfully deleted");
			return "redirect:/album";
		}
	}

	@RequestMapping(value = "/album/{aid}/addphoto", method = RequestMethod.GET)
	public String addPhotosToAlbum(@PathVariable Integer aid,
			Principal principal, ModelMap map) {
		logger.info("Navigating to add photos to album page for album with id "
				+ aid);
		map.addAttribute("photoList", photoService.listPhotos(principal));
		map.addAttribute("album", albumService.getAlbumAndPhotosById(aid));
		return "albums/addphototoalbum";
	}

	@RequestMapping(value = "/album/{aid}/addphoto/update", method = RequestMethod.POST)
	public String updateAlbumWithPhotos(@ModelAttribute("album") Album album,
			BindingResult result, @PathVariable Integer aid, ModelMap map,
			Principal principal) {
		logger.info("Adding photos to album with id " + aid);
		Album oldAlbum = albumService.getAlbumById(aid);
		oldAlbum.setPhotos(album.getPhotos());
		if (!correctUser(oldAlbum, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ getCurrentUser(principal).getId()
					+ " to delete album with id " + aid);
			throw new UnauthorizedException();
		} else {
			albumService.updateAlbum(oldAlbum, principal);
			Set<Photo> photoList = buildPhotoList(oldAlbum, principal);
			map.addAttribute("message",
					"label.message.album.addphotossuccessful");
			map.addAttribute("album", oldAlbum);
			map.addAttribute("photoList", photoList);
			logger.info("The photos were successfully added to the album with id "
					+ aid);
			return "albums/viewalbum";
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Set.class, "photos",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						logger.info("Converting element into a photo");
						Integer pid = new Integer((String) element);
						Photo photo = photoService.getPhotoById(pid);
						return photo;
					}
				});
	}

	@RequestMapping(value = "/album/{aid}/image")
	public void getAlbumPhotoImage(@PathVariable Integer aid,
			HttpServletResponse response) throws IOException {
		logger.info("Showing most recently created photo in album with id "
				+ aid);
		Album album = albumService.getAlbumAndPhotosById(aid);
		if (album.getPhotos() != null || !album.getPhotos().isEmpty()) {
			List<Photo> photos = new ArrayList<Photo>(album.getPhotos());

			Collections.sort(photos, new Comparator<Photo>() {
				public int compare(Photo a, Photo b) {
					return b.getCreationDate().compareTo(a.getCreationDate());
				}
			});
			Photo MostRecentPhoto = photos.get(0);
			if (MostRecentPhoto.getImage() != null) {
				response.setContentType(MostRecentPhoto.getImageContentType());
				ServletOutputStream out = response.getOutputStream();
				out.write(MostRecentPhoto.getImage());
				out.flush();
				out.close();
				logger.info("Successfully retrieved most recent photo in album with id "
						+ aid);
			}
		}

	}

	@RequestMapping(value = "/album/{aid}/rate")
	public String rateAlbum(@RequestParam("rating") Integer rating,
			@PathVariable Integer aid, Principal principal, ModelMap map,
			RedirectAttributes redirectAttributes) {
		if (principal == null) {
			logger.error("Unauthorized attempt by an unregistered user to rate album with id "
					+ aid);
			throw new UnauthorizedException();
		}

		Album album = albumService.getAlbumById(aid);
		List<String> errors = new ArrayList<String>();
		if (album.getUser().getUid().equals(getCurrentUser(principal))) {

			logger.error("User attempted to rate their own album");
			errors.add("error.rating.rateownalbum");
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/album/" + aid;

		} else {
			try {
				albumService.rateAlbum(album, rating, principal);
			} catch (AlreadyRatedException e) {
				errors.add("error.rating.ratealbumtwice");
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/album/" + aid;
			}
		}
		redirectAttributes.addFlashAttribute("message",
				"label.message.album.ratingsuccessful");
		return "redirect:/album/" + aid;
	}

	private boolean albumHasErrors(List<String> errors, BindingResult result) {

		boolean hasErrors = false;

		if (result.hasErrors()) {
			hasErrors = true;
			for (ObjectError objError : result.getAllErrors()) {
				if (!errors.contains(objError.getCode()))
					for (String errorCode : objError.getCodes()) {
						if (errorCode.contains(".album.")) {
							errors.add(errorCode);
							break;
						}
					}
			}
		}

		return hasErrors;
	}

	private boolean correctUser(Album album, Principal principal) {
		AuthorizedUser user = getCurrentUser(principal);
		boolean correct = false;
		if (album.getUser().getUid() == user.getId()) {
			correct = true;
		}

		return correct;
	}

	private AuthorizedUser getCurrentUser(Principal principal) {
		if (principal == null) {
			return null;
		}
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		return user;
	}

	private Set<Photo> buildPhotoList(Album album, Principal principal) {
		AuthorizedUser user = getCurrentUser(principal);
		Set<Photo> photoList = album.getPhotos();
		if (user == null || user.getId() != album.getUser().getUid()) {
			Set<Photo> publicPhotoList = new HashSet<Photo>();
			for (Photo photo : photoList) {
				if (photo.getIsPublic()) {
					publicPhotoList.add(photo);
				}
			}
			return publicPhotoList;
		} else {
			return photoList;
		}

	}

}
