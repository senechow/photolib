package com.spring.photolib.webapp.controller;

import java.security.Principal;
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
import org.springframework.security.core.Authentication;
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

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.Flag;
import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;
import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.exception.AccountAlreadyConfirmedException;
import com.spring.photolib.webapp.exception.ConfirmationMismatchException;
import com.spring.photolib.webapp.exception.NotFoundException;
import com.spring.photolib.webapp.exception.UnauthorizedException;
import com.spring.photolib.webapp.service.PhotoService;
import com.spring.photolib.webapp.service.UserService;
import com.spring.photolib.webapp.util.SortTypes;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailSender mailSender;
	private Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	@Qualifier("userValidator")
	private Validator validator;
	
	private static final String [] USER_FLAG_REASONS = {"Inappropriate name", "Spamming", 
		"Posting inappropriate material", "Other (Please specify in description)"};

	@RequestMapping(value = "/users/new", method = RequestMethod.GET)
	public String newUser(ModelMap map) {
		logger.info("Directing to user creating page");
		map.addAttribute("user", new User());
		return "users/signup";
	}

	@InitBinder("user")
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
		user.setPasswordOld(user.getPassword());
		userService.createUser(user);

		String baseUrl = request
				.getRequestURL()
				.toString()
				.substring(0, request.getRequestURL().toString().indexOf("new"));

		String sendingURL = baseUrl + user.getUid() + "/confirmemail";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("photolib@notreply.com");
		message.setTo(user.getEmailAddress());
		message.setSubject("Photo Library Email Confirmation");
		message.setText("Thank you for signing up with Photo Library. Please enter in the following "
				+ "confirmation code into the link provided below to confirm your email address: \n"
				+ user.getStoredConfirmationCode()
				+ "\n"
				+ "Link: "
				+ sendingURL);

		mailSender.send(message);

		logger.info("User with id "
				+ user.getUid()
				+ "was successfully created. Email was sent to account email address and confirmation is required.");
		return "users/createaccountsuccessful";
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public String viewUser(@PathVariable Integer id, ModelMap map, Principal principal) {
		logger.info("Viewing user account for user with id " + id);
		User user = userService.getUserInfo(id);
		user.setPassword("");
		user.setPasswordConfirm("");
		user.setConfirmationCode("");
		user.setStoredConfirmationCode("");
		user.setPasswordOld("");
		map.addAttribute("user", user);
		if(getCurrentUser(principal).getId().equals(id)) {
			map.addAttribute("myAccountTitle", "label.title.myaccount");
		}
		return "users/viewuser";
	}

	@RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
	public String editUser(@PathVariable Integer id, ModelMap map,
			Principal principal) {
		logger.info("Direction user to the edit user account page for user with id "
				+ id);
		if (principal == null) {
			logger.error("Unauthorized attempt by a non registered user to update user with id "
					+ id);
			throw new UnauthorizedException();
		} else if (!correctUser(id, principal)) {
			AuthorizedUser currUser = getCurrentUser(principal);
			logger.error("Unauthorized attempt by user with id "
					+ currUser.getId() + " to edit user with id " + id);
			throw new UnauthorizedException();
		} else {
			User user = userService.getUserInfo(id);
			user.setPassword("");
			user.setPasswordConfirm("");
			user.setConfirmationCode("");
			user.setStoredConfirmationCode("");
			user.setPasswordOld("");
			map.addAttribute("user", user);
			map.addAttribute("myAccountTitle", "label.title.myaccount");
			return "users/edituser";
		}
	}

	@RequestMapping(value = "/users/{id}/update", method = RequestMethod.POST)
	public String updateUser(@ModelAttribute("user") @Valid User user,
			BindingResult result, @PathVariable Integer id,
			Principal principal, ModelMap map,
			RedirectAttributes redirectAttributes) {
		logger.info("Updating user with id " + id);
		List<String> errors = new ArrayList<String>();
		AuthorizedUser currUser = getCurrentUser(principal);
		if (principal == null) {
			logger.error("Unauthorized attempt by a non registered user to update user with id "
					+ id);
			throw new UnauthorizedException();
		} else if (!correctUser(id, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ currUser.getId() + " to edit user with id " + id);
			throw new UnauthorizedException();
		} else {
			for (FieldError fieldError : result.getFieldErrors()) {
				boolean added = false;
				if (!fieldError.getField().contains("password")) {
					for (String code : fieldError.getCodes()) {
						if (code.contains("NotEmpty.user")) {
							errors.add(code);
							added = true;
							break;
						}
					}
					if (added == false) {
						errors.add(fieldError.getCode());
					}
				}
			}
			if (!errors.isEmpty()) {
				map.addAttribute("errors", errors);
				logger.error("There were errors when updating the user. Redirecting the user to the user edit page.");
				return "users/edituser";
			} else {
				User oldUser = userService.getUserInfo(id);
				oldUser.setUserName(user.getUserName());
				oldUser.setFirstName(user.getFirstName());
				oldUser.setLastName(user.getLastName());
				oldUser.setEmailAddress(user.getEmailAddress());
				currUser.setUserName(oldUser.getUserName());
				userService.updateUser(oldUser);
				redirectAttributes.addFlashAttribute("message",
						"label.message.user.updatesuccessful");
				// map.addAttribute("message",
				// "label.message.user.updatesuccessful");
				logger.info("Successfully updated user with id " + id);
				return "redirect:/users/" + oldUser.getUid();
			}
		}

	}

	@RequestMapping(value = "/users/{id}/photos", method = RequestMethod.GET)
	public String showUsersPhotos(@PathVariable Integer id, ModelMap map) {
		logger.info("Retrieving all photos created by user " + id);
		map.addAttribute("userPhotoTitle", "label.title.usersphoto");
		map.addAttribute("emptyPhotoMsg", "label.emptyusersphoto");
		map.addAttribute("photoList", userService.getUserPhotos(id));
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
		Search search = new Search();
		search.setSortType(SortTypes.MOST_RECENT.toString());
		map.addAttribute("search", search);
		logger.info("Successfully retrieved all photos created by user " + id);
		return "photos/photo";
	}

	@RequestMapping(value = "/users/{id}/photos/sort", method = RequestMethod.GET)
	public String sortUsersPhotos(@ModelAttribute("search") Search search,
			BindingResult result, @PathVariable Integer id, ModelMap map) {
		logger.info("Sorting all photos created by user " + id);
		map.addAttribute("userPhotoTitle", "label.title.usersphoto");
		map.addAttribute("emptyPhotoMsg", "label.emptyusersphoto");
		map.addAttribute("photoList",
				userService.getUserPhotosAndSort(id, search.getSortType()));
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
		map.addAttribute("search", search);
		logger.info("Successfully sorted all photos created by user " + id);
		return "photos/photo";

	}

	@RequestMapping(value = "/users/{id}/albums", method = RequestMethod.GET)
	public String showUsersAlbums(@PathVariable Integer id, ModelMap map) {
		logger.info("Retrieving all albums created by user " + id);
		map.addAttribute("userAlbumTitle", "label.title.usersalbums");
		map.addAttribute("emptyAlbumMsg", "label.emptyusersalbum");
		map.addAttribute("albumList", userService.getUserAlbums(id));
		logger.info("Successfully retrieved all albums created by user " + id);
		return "albums/album";

	}

	@RequestMapping(value = "/users/{id}/confirmemail", method = RequestMethod.GET)
	public String showConfirmEmail(@PathVariable Integer id, ModelMap map) {
		logger.info("Directing user with id " + id
				+ " to email confirmation page");
		if (userService.getUserInfo(id) == null) {
			logger.error("User with " + id + " does not exist");
			throw new NotFoundException();
		} else {
			User user = userService.getUserInfo(id);
			user.setPassword("");
			user.setPasswordConfirm("");
			user.setConfirmationCode("");
			user.setStoredConfirmationCode("");
			user.setPasswordOld("");
			map.addAttribute("user", user);
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
			FieldError fieldError = new FieldError("user", "confirmationCode", "error.empty.confirmationcode");
			result.addError(fieldError);
			map.addAttribute("userid", id.toString());
			map.addAttribute("errors", errors);
			logger.error("User with id " + id
					+ "has entered in an email confirmation code.");
			return "users/confirmemail";
		}

		try {
			userService.activateAccount(user, id);
		} catch (AccountAlreadyConfirmedException e) {
			errors.add("error.activation.alreadyactivated");
			FieldError fieldError = new FieldError("user", "confirmationCode", "error.empty.error.activation.alreadyactivated");
			result.addError(fieldError);
			map.addAttribute("userid", id.toString());
			map.addAttribute("errors", errors);
			logger.error("User with id "
					+ id
					+ "has attempted to confirm their account which has already been activated.");
			return "users/confirmemail";
		} catch (ConfirmationMismatchException e) {
			errors.add("error.activation.confirmationmismatch");
			map.addAttribute("userid", id.toString());
			map.addAttribute("errors", errors);
			FieldError fieldError = new FieldError("user", "confirmationCode", "error.activation.confirmationmismatch");
			result.addError(fieldError);
			logger.error("User with id " + id
					+ "has entered in an incorrect email confirmation code.");
			return "users/confirmemail";
		}

		redirectAttributes.addFlashAttribute("message",
				"label.message.confirmsuccessful");
		logger.info("Successfully confirmed the email for user with id " + id);
		return "redirect:/login";
	}

	@RequestMapping(value = "/users/{id}/changepassword", method=RequestMethod.GET)
	public String changePassword(@PathVariable Integer id,
			Principal principal, ModelMap map) {
		logger.info("Direction user to the change password page");
		map.addAttribute("user", new User());
		return "users/changepwd";

	}

	@RequestMapping(value = "/users/{id}/updatepassword", method=RequestMethod.POST)
	public String updatePassword(@ModelAttribute("user") @Valid User user,
			BindingResult result, @PathVariable Integer id, ModelMap map,
			Principal principal, RedirectAttributes redirectAttributes) {
		logger.info("Changing password for user with id " + id);
		List<String> errors = new ArrayList<String>();

		AuthorizedUser currUser = getCurrentUser(principal);
		if (principal == null) {
			logger.error("Unauthorized attempt by a non registered user to change users password with id "
					+ id);
			throw new UnauthorizedException();
		} else if (!correctUser(id, principal)) {
			logger.error("Unauthorized attempt by user with id "
					+ currUser.getId() + " to change user's password with id "
					+ id);
			throw new UnauthorizedException();
		} else {
			User oldUser = userService.getUserInfo(id);
			if (result.hasErrors()) {
				for (FieldError fieldError : result.getFieldErrors()) {
					boolean added = false;
					if (fieldError.getField().contains("password")) {
						for (String code : fieldError.getCodes()) {
							if (code.contains("NotEmpty.user")) {
								errors.add(code);
								added = true;
								break;
							}
						}
						if (added == false) {
							errors.add(fieldError.getCode());
						}
					}
				}
			}
			if(!user.getPasswordOld().equals("") && !user.getPasswordOld().equals(oldUser.getPasswordOld())) {
				FieldError passwordError = new FieldError("user", "passwordOld", "error.password.wrongold");
				result.addError(passwordError);
				errors.add("error.password.wrongold");
			}
			if (!errors.isEmpty()) {
				map.addAttribute("errors", errors);
				logger.error("There were errors when changing the password of the user with id " + id
						+ ". Redirecting the user to the change password page.");
				return "users/changepwd";
			} else {
				userService.changePassword(oldUser, user);
				oldUser.setPassword("");
				oldUser.setPasswordConfirm("");
				oldUser.setConfirmationCode("");
				oldUser.setStoredConfirmationCode("");
				oldUser.setPasswordOld("");
				redirectAttributes.addFlashAttribute("user", oldUser);
				redirectAttributes.addFlashAttribute("message",
						"label.message.user.changepasswordsuccessful");
				logger.info("Successfully updated user's password with id "
						+ id);
				return "redirect:/users/" + oldUser.getUid();
			}
		}
	}
	
	@RequestMapping(value="/forgotpassword", method=RequestMethod.GET)
	public String forgotPassword(ModelMap map) {
		logger.info("Directing the user to the forgot password page");
		map.addAttribute("user", new User());
		return "users/forgotpassword";
	}
	
	@RequestMapping(value="/forgotpassword/sendemail", method=RequestMethod.POST)
	public String forgotPasswordSendEmail(@ModelAttribute ("user") User user, BindingResult result,
			ModelMap map, HttpServletRequest request) {
		logger.info("Changing the user's password to a random one and sending an email to their email");
		List<String> errors = new ArrayList<String> ();
		if(user.getEmailAddress() == null || user.getEmailAddress().equals("")) {
			logger.error("User entered in an empty email address during password recovery");
			FieldError fieldError = new FieldError("user", "emailAddress", "NotEmpty.user.emailAddress");
			result.addError(fieldError);
			errors.add("NotEmpty.user.emailAddress");
			map.addAttribute("errors", errors);
			return "users/forgotpassword";
		}
		User storedUser = userService.getUserByEmail(user.getEmailAddress());
		if(storedUser == null) {
			logger.error("User entered in an incorrect email address during password recovery");
			FieldError fieldError = new FieldError("user", "emailAddress", "error.email.wrongemail");
			result.addError(fieldError);
			errors.add("error.email.wrongemail");
			map.addAttribute("errors", errors);
			return "users/forgotpassword";
		} else {
			userService.resetPassword(storedUser);
			String baseUrl = request
					.getRequestURL()
					.toString()
					.substring(0, request.getRequestURL().toString().indexOf("user"));

			String sendingURL = baseUrl + "/login";

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("photolib@notreply.com");
			message.setTo(user.getEmailAddress());
			message.setSubject("Photo Library Password Reset");
			message.setText("You have requested to reset your password. Please go to the link below "
					+ "and use the following password to login to your account: \n"
					+ "Password: "
					+ user.getPassword()
					+ "\n"
					+ "Once you login, you will be prompted to change your password to a new one before "
					+ "you can continue with any further action."
					+ "Link: "
					+ sendingURL);

			mailSender.send(message);
			
			logger.info("The user has successfully started password recovery with email " + user.getEmailAddress());
			return "users/forgotpasswordsendemail";
		}
	}

	private String buildConfirmationCode(User user) {
		int code = user.getEmailAddress().hashCode() + new Date().hashCode()
				+ user.hashCode() + java.util.UUID.randomUUID().hashCode();

		String confirmationCode = java.util.UUID.randomUUID().toString() + code;

		return confirmationCode;
	}

	private boolean correctUser(Integer id, Principal principal) {
		AuthorizedUser user = getCurrentUser(principal);
		boolean correct = false;
		if (id == user.getId()) {
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

}
