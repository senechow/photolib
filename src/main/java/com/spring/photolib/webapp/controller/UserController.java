package com.spring.photolib.webapp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AccountAlreadyConfirmedException;
import com.spring.photolib.webapp.exception.ConfirmationMismatchException;
import com.spring.photolib.webapp.exception.NotFoundException;
import com.spring.photolib.webapp.service.PhotoService;
import com.spring.photolib.webapp.service.UserService;
import com.spring.photolib.webapp.validator.SignupValidator;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailSender mailSender;
	
	private Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	@Qualifier("signUpValidator")
	private Validator validator;

	@RequestMapping(value = "/users/new", method = RequestMethod.GET)
	public String newUser(ModelMap map) {
		logger.info("Directing to user creating page");
		map.addAttribute("user", new User());
		return "users/signup";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validator);
	}

	@RequestMapping(value = "/users/new/create", method = RequestMethod.POST)
	public String createUser(@ModelAttribute("user") @Valid User user,
			BindingResult result, ModelMap map, HttpServletRequest request) {
		logger.info("Creating a new user.");
		List<String> errors = new ArrayList<String>();

		if (result.hasErrors()) {
			
			for (ObjectError objError : result.getAllErrors()) {
				boolean added = false;
				for (String code : objError.getCodes()) {
					if (code.contains("NotEmpty.user")) {
						errors.add(code);
						added = true;
						break;
					}
				}
				if (added == false) {
					errors.add(objError.getCode());
				}
			}
			map.addAttribute("errors", errors);
			logger.error("There were errors when creating a new user. Redirecting the user to the user creation page.");
			return "users/signup";
		}
		user.setStoredConfirmationCode(buildConfirmationCode(user));
		userService.createUser(user);

		String baseUrl = request
				.getRequestURL()
				.toString()
				.substring(0, request.getRequestURL().toString().indexOf("new"));

		String sendingURL = baseUrl + user.getUid()
				+ "/confirmemail";

		 SimpleMailMessage message = new SimpleMailMessage();
		 message.setFrom("photolib@notreply.com");
		 message.setTo(user.getEmailAddress());
		 message.setSubject("Photo Library Email Confirmation");
		 message.setText(
		 "Thank you for signing up with Photo Library. Please enter in the following "
		 +
		 "confirmation code into the link provided below to confirm your email address: \n"
		 +
		 user.getStoredConfirmationCode() + "\n" + "Link: " + sendingURL);
		
		 mailSender.send(message);

		logger.info("User with id " + user.getUid() + "was successfully created. Email was sent to account email address and confirmation is required." );
		return "users/createaccountsuccessful";
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public String viewUser(@PathVariable Integer id) {
		logger.info("Viewing user account for user with id " + id);
		return "users/viewuser";
	}

	@RequestMapping(value = "/users/{id}/photos", method = RequestMethod.GET)
	public String showUsersPhotos(@PathVariable Integer id, ModelMap map) {
		logger.info("Retrieving all photos created by user " + id);
		map.addAttribute("userPhotoTitle", "label.title.usersphoto");
		map.addAttribute("userPhotoEmptyMsg", "label.emptyusersphoto");
		map.addAttribute("photoList", userService.getUserPhotos(id));
		logger.info("Successfully retrieved all photos created by user " + id);
		return "photos/photo";

	}

	@RequestMapping(value = "/users/{id}/confirmemail", method = RequestMethod.GET)
	public String showConfirmEmail(@PathVariable Integer id, ModelMap map) {
		logger.info("Directing user with id " + id + " to email confirmation page");
		if (userService.getUserInfo(id) == null) {
			logger.error("User with " + id + " does not exist");
			throw new NotFoundException();
		} else {
			map.addAttribute("user", new User());
			map.addAttribute("userid", id.toString());
			return "users/confirmemail";
		}
	}

	@RequestMapping(value = "/users/{id}/confirmemail/confirm", method = RequestMethod.POST)
	public String confirmEmail(@ModelAttribute("user") User user,
			BindingResult result, @PathVariable Integer id, ModelMap map,
			RedirectAttributes redirectAttributes) {
		logger.info("Confirming user with id " + id);
		List<String> errors = new ArrayList<String>();
		if (user.getConfirmationCode() == null
				|| user.getConfirmationCode().equals("")) {
			errors.add("error.empty.confirmationcode");
			map.addAttribute("userid", id.toString());
			map.addAttribute("errors", errors);
			logger.error("User with id " + id + "has entered in an email confirmation code.");
			return "users/confirmemail";
		}

		try {
			userService.activateAccount(user, id);
		} catch (AccountAlreadyConfirmedException e) {
			errors.add("error.activation.alreadyactivated");
			map.addAttribute("userid", id.toString());
			map.addAttribute("errors", errors);
			logger.error("User with id " + id + "has attempted to confirm their account which has already been activated.");
			return "users/confirmemail";
		} catch (ConfirmationMismatchException e) {
			errors.add("error.activation.confirmationmismatch");
			map.addAttribute("userid", id.toString());
			map.addAttribute("errors", errors);
			logger.error("User with id " + id + "has entered in an incorrect email confirmation code.");
			return "users/confirmemail";
		}

		redirectAttributes.addFlashAttribute("message", "label.message.confirmsuccessful");
		logger.info("Successfully confirmed the email for user with id " + id);
		return "redirect:/login";
	}

	private String buildConfirmationCode(User user) {
		int code = user.getEmailAddress().hashCode() + new Date().hashCode()
				+ user.hashCode() + java.util.UUID.randomUUID().hashCode();

		String confirmationCode = java.util.UUID.randomUUID().toString() + code;

		return confirmationCode;
	}
}
