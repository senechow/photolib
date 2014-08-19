package com.spring.photolib.webapp.controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Flag;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.PhotoFlag;
import com.spring.photolib.webapp.domain.Role;
import com.spring.photolib.webapp.domain.Search;
import com.spring.photolib.webapp.domain.Tag;
import com.spring.photolib.webapp.exception.PhotoAlreadyRatedException;
import com.spring.photolib.webapp.exception.UnauthorizedException;
import com.spring.photolib.webapp.service.PhotoService;
import com.spring.photolib.webapp.util.SortTypes;
import com.spring.photolib.webapp.util.TagPropertyEditor;
import com.spring.photolib.webapp.validator.PhotoValidator;

@Controller
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	private Logger logger = Logger.getLogger(PhotoController.class);

	private static final String[] PHOTO_FLAG_REASONS = {
			"Inappropriate language", "Inappropriate image",
			"Other (Please specify in description)" };

	@RequestMapping(value = { "", "/", "/photo" }, method = RequestMethod.GET)
	public String listPhotos(
			ModelMap map,
			Principal principal,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page) {
		logger.info("Retrieving photo list.");
		map.addAttribute("photoList", photoService.listPhotos(principal, page));
		Search search = new Search();
		search.setSortType(SortTypes.MOST_RECENT.toString());
		map.addAttribute("search", search);
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());

		logger.info("Photo list retrieval completed.");
		return "photos/photo";
	}
	
	@RequestMapping(value = "/morephotos" , method = RequestMethod.GET)
	public String addListPhotos( @RequestParam(value = "sortType", defaultValue = "Most Recent", required = false) String sortType,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page, ModelMap map,
			Principal principal
			) {
		logger.info("Adding more photos to the list.");
		map.addAttribute("photoList", photoService.listPhotosAndSort(principal, sortType, page));
		Search search = new Search();
		search.setSortType(sortType);
		map.addAttribute("search", search);
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());

		logger.info("More photos have been added.");
		return "photos/_photo";
	}

	@RequestMapping(value = "/photo/sort")
	public String sortPhotos(@ModelAttribute("search") Search search,
			BindingResult result, ModelMap map, Principal principal) {
		logger.info("Retrieving and sorting photos by type "
				+ search.getSortType());
		map.addAttribute("photoList", photoService.listPhotosAndSort(principal,
				search.getSortType(), 0));
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
		map.addAttribute("search", search);
		logger.info("Finished retrieving and sorting photos by type "
				+ search.getSortType());
		return "photos/photo";
	}

	@RequestMapping(value = "/photo/new", method = RequestMethod.GET)
	public String newPhoto(ModelMap map) {
		logger.info("Directing to photo creation page");
		map.addAttribute("photo", new Photo());
		return "photos/addphoto";
	}

	@RequestMapping(value = "/photo/new/create", method = RequestMethod.POST)
	public String createPhoto(@ModelAttribute("photo") @Valid Photo photo,
			BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile,
			Principal principal, ModelMap map) {

		logger.info("Creating a new photo.");
		List<String> errors = new ArrayList<String>();

		boolean hasErrors = photoHasErrors(errors, result, imageFile);

		if (hasErrors) {
			logger.error("Form errors were found during photo creation. Redirecting user back to photo creation page.");
			map.addAttribute("errors", errors);
			return "photos/addphoto";
		} else {
			photoService.addPhoto(photo, imageFile, principal);

			map.addAttribute("message",
					"label.message.photo.creationsuccessful");
			logger.info("Photo with id " + photo.getPid()
					+ " was successfully created.");
			// return "redirect:/photo/" + photo.getPid();
			return "photos/viewphoto";
		}
	}

	@RequestMapping(value = "/photo/{pid}/edit", method = RequestMethod.GET)
	public String editPhoto(@PathVariable Integer pid, Principal principal,
			ModelMap map) {
		logger.info("Directing to photo editting page for photo id " + pid);
		Photo photo = photoService.getPhotoAndTagsById(pid);

		if (!correctUser(photo, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ getCurrentUser(principal).getId()
					+ " to edit photo with id " + pid);
			throw new UnauthorizedException();
		} else {
			map.addAttribute("photo", photo);
			return "photos/editphoto";
		}
	}

	@RequestMapping(value = "/photo/{pid}", method = RequestMethod.POST)
	public String updatePhoto(@ModelAttribute("photo") Photo photo,
			BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile,
			@PathVariable Integer pid, Principal principal, ModelMap map) {

		logger.info("Updating photo with id " + pid);
		Photo oldPhoto = photoService.getPhotoById(photo.getPid());

		if (!correctUser(oldPhoto, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ getCurrentUser(principal).getId()
					+ " to edit photo with id " + pid);
			throw new UnauthorizedException();
		} else {
			List<String> errors = new ArrayList<String>();

			boolean hasErrors = photoHasErrors(errors, result, imageFile);

			if (hasErrors) {
				logger.error("There were form errors when editting photo with id "
						+ pid + " redirecting user to edit photo page");
				map.addAttribute("errors", errors);
				return "photos/editphoto";
			} else {
				photoService.updatePhoto(photo, imageFile, principal);
				Photo updatedPhoto = photoService.getPhotoAndTagsById(pid);
				map.addAttribute("photo", updatedPhoto);
				map.addAttribute("message",
						"label.message.photo.editsuccessful");
				logger.info("Photo with id " + pid + "was successfully updated");
				return "photos/viewphoto";
			}
		}
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Set.class, "tags", new TagPropertyEditor());
	}

	private Set<Tag> createSetFromStrings(String unformatedTagNames) {
		unformatedTagNames.replaceAll("\\s", "");
		Set<Tag> tagNames = new LinkedHashSet<Tag>();
		boolean endOfString = false;
		while (!endOfString) {
			Tag tag = new Tag();
			if (unformatedTagNames.contains(",")) {
				tag.setName(unformatedTagNames.substring(0,
						unformatedTagNames.indexOf(",")));
				unformatedTagNames = unformatedTagNames
						.substring(unformatedTagNames.indexOf(",") + 1);

			} else {
				tag.setName(unformatedTagNames);
				endOfString = true;
			}
			tagNames.add(tag);
		}
		return tagNames;
	}

	@RequestMapping(value = "/photo/{pid}", method = RequestMethod.DELETE)
	public String deletePhoto(@PathVariable Integer pid, Principal principal,
			RedirectAttributes redirectAttributes) {
		logger.info("Deleting photo with id " + pid);
		Photo photo = photoService.getPhotoById(pid);
		if (!correctUser(photo, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ getCurrentUser(principal).getId()
					+ " to delete photo with id " + photo.getPid());
			throw new UnauthorizedException();
		} else {
			photoService.removePhoto(pid);
			redirectAttributes.addFlashAttribute("message",
					"label.message.photo.deletesuccessful");
			logger.info("Photo with id " + pid + "was successfully deleted");
			return "redirect:/photo";

		}

	}

	@RequestMapping(value = "/photo/{pid}", method = RequestMethod.GET)
	public String getPhotoById(@PathVariable Integer pid, ModelMap map)
			throws IOException {
		logger.info("Retrieving photo with id " + pid);
		Photo photo = photoService.getPhotoAndTagsById(pid);
		if (photo.getDescription().equals("")) {
			photo.setDescription("No description is avaliable.");
		}
		map.addAttribute("photo", photo);
		logger.info("Successfully retrieved photo with id " + pid);
		return "photos/viewphoto";
	}

	@RequestMapping(value = "/photo/{pid}/image")
	public void getPhotoImage(@PathVariable Integer pid,
			HttpServletResponse response) throws IOException {
		logger.info("Retrieving photo image with id " + pid);
		Photo photo = photoService.getPhotoById(pid);
		if (photo.getImage() != null) {
			response.setContentType(photo.getImageContentType());
			ServletOutputStream out = response.getOutputStream();
			out.write(photo.getImage());
			out.flush();
			out.close();
			logger.info("Successfully retrieved photo image with id " + pid);
		}
	}

	@RequestMapping(value = "/photo/{pid}/rate")
	public String ratePhoto(@RequestParam("rating") Integer rating,
			@PathVariable Integer pid, Principal principal, ModelMap map,
			RedirectAttributes redirectAttributes) {
		if (principal == null) {
			logger.error("Unauthorized attempt by an unregistered user to rate photo with id "
					+ pid);
			throw new UnauthorizedException();
		}

		Photo photo = photoService.getPhotoById(pid);
		List<String> errors = new ArrayList<String>();
		if (photo.getUser().getUid().equals(getCurrentUser(principal))) {

			logger.error("User attempted to rate their own photo");
			errors.add("error.rating.rateownphoto");
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/photo/" + pid;

		} else {
			try {
				photoService.ratePhoto(photo, rating, principal);
			} catch (PhotoAlreadyRatedException e) {
				errors.add("error.rating.ratetwice");
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/photo/" + pid;
			}
		}
		redirectAttributes.addFlashAttribute("message",
				"label.message.photo.ratingsuccessful");
		return "redirect:/photo/" + pid;
	}

	private boolean correctUser(Photo photo, Principal principal) {
		AuthorizedUser user = getCurrentUser(principal);
		Role userRole = user.getRole();
		boolean correct = false;
		if (photo.getUser().getUid() == user.getId()
				|| userRole.getRole().equals("admin")) {
			correct = true;
		}

		return correct;
	}

	private boolean photoHasErrors(List<String> errors, BindingResult result,
			MultipartFile imageFile) {
		boolean hasErrors = false;

		PhotoValidator photoValidator = new PhotoValidator();

		if (photoValidator.hasEmptyImage(imageFile)) {
			hasErrors = true;
			result.rejectValue("imageFile", "error.image.empty", "image.empty");
			errors.add("error.image.empty");
		}

		if (!photoValidator.hasAcceptedImageType(imageFile)) {
			hasErrors = true;
			result.rejectValue("imageFile", "error.image.notaccepted",
					"image.notaccepted");
			errors.add("error.image.notaccepted");
		}

		if (result.hasErrors()) {
			hasErrors = true;
			for (ObjectError objError : result.getAllErrors()) {
				if (!errors.contains(objError.getCode()))
					for (String errorCode : objError.getCodes()) {
						if (errorCode.contains(".photo.")) {
							errors.add(errorCode);
							break;
						}
					}
			}
		}

		return hasErrors;
	}

	private AuthorizedUser getCurrentUser(Principal principal) {
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		return user;
	}

}
