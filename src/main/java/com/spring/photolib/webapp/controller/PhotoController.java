package com.spring.photolib.webapp.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.exception.UnauthorizedException;
import com.spring.photolib.webapp.service.PhotoService;
import com.spring.photolib.webapp.validator.PhotoValidator;

@Controller
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	private Logger logger = Logger.getLogger(PhotoController.class);

	@RequestMapping(value = { "","/", "/photo" }, method = RequestMethod.GET)
	public String listPhotos(ModelMap map, Principal principal) {
		logger.info("Retrieving photo list.");
		map.addAttribute("photoList", photoService.listPhotos(principal));
		logger.info("Photo list retrieval completed.");
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
			logger.error("Form errors were found during photo creation. Redirecting user back ot photo creation page.");
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
		Photo photo = photoService.getPhotoById(pid);

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
				map.addAttribute("message",
						"label.message.photo.editsuccessful");
				logger.info("Photo with id " + pid + "was successfully updated");
				return "photos/viewphoto";
			}
		}
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
		Photo photo = photoService.getPhotoById(pid);
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

	private boolean correctUser(Photo photo, Principal principal) {
		AuthorizedUser user = getCurrentUser(principal);
		boolean correct = false;
		if (photo.getUser().getUid() == user.getId()) {
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
