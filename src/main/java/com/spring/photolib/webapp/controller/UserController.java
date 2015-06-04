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
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.photolib.webapp.domain.Album;
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
import com.spring.photolib.webapp.util.AlbumHelper;
import com.spring.photolib.webapp.util.PhotoHelper;
import com.spring.photolib.webapp.util.SortHelper;
import com.spring.photolib.webapp.util.SortTypes;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailSender mailSender;
	private PhotoHelper photoHelper = new PhotoHelper();
	private SortHelper sortHelper = new SortHelper();
	private AlbumHelper albumHelper = new AlbumHelper();
	private Logger logger = Logger.getLogger(UserController.class);
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Autowired
	@Qualifier("userValidator")
	private Validator validator;

	private static final String[] USER_FLAG_REASONS = { "Inappropriate name",
			"Spamming", "Posting inappropriate material",
			"Other (Please specify in description)" };

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
					if (code.contains("NotEmpty.user") || code.contains("Size.user")) {
						errors.add(code);
						added = true;
						break;
					}
				}
				if (added == false) {
					errors.add(objError.getCode());
				}
			}

		}
		if (user.getEmailAddress() != null
				&& !user.getEmailAddress().equals("")
				&& !errors.contains("Email")) {
			User userWithSameEmail = userService.getUserByEmail(user
					.getEmailAddress());
			if (userWithSameEmail != null) {
				emailAlreadyExistsError(
						errors,
						result,
						"Attempt by a new user to create an account with an email another account is using");
			}
		}
		if (!errors.isEmpty()) {
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
	public String viewUser(@PathVariable Integer id, ModelMap map,
			Principal principal, HttpServletRequest request) {
		logger.info("Viewing user account for user with id " + id);
		User user = userService.getUserInfo(id);
		eraseViewUsersPasswordsAndConfCodes(user);
		PagedListHolder<Photo> photoList = new PagedListHolder<Photo>(
				userService.getUserPhotos(id, principal),
				new MutableSortDefinition(sortHelper.getDefaultSortPhoto(),
						true, true));
		photoHelper.addPhotosAndNewSearchToMap(photoList, request, map,
				SortTypes.MOST_RECENT.toString());
		PagedListHolder<Album> albumList = new PagedListHolder<Album>(
				userService.getUserAlbums(id, principal),
				new MutableSortDefinition(sortHelper.getDefaultSortPhoto(),
						true, true));
		albumHelper.addAlbumsAndNewSearchToMap(albumList, request, map,
				SortTypes.MOST_RECENT.toString());
		map.addAttribute("user", user);
		if (principal != null && getCurrentUser(principal).getId().equals(id)) {
			map.addAttribute("emptyPhotoMsg", "label.emptyusersphoto");
			map.addAttribute("emptyAlbumMsg", "label.emptyusersalbum");
			map.addAttribute("userMostRecentPhotoTitle",
					"label.title.usersmostrecentphotos");
			map.addAttribute("userMostRecentAlbumTitle",
					"label.title.usersmostrecentalbums");
			map.addAttribute("myAccountTitle", "label.title.myaccount");
		}
		logger.info("Successfully retrieved user account for user with id "
				+ id);
		return "users/viewuser";
	}

	@RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
	public String editUser(@PathVariable Integer id, ModelMap map,
			Principal principal) {
		logger.info("Directing user to the edit user account page for user with id "
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
			eraseViewUsersPasswordsAndConfCodes(user);
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
			if (user.getEmailAddress() != null
					&& !user.getEmailAddress().equals("")
					&& !errors.contains("Email")) {
				User userWithSameEmail = userService.getUserByEmail(user
						.getEmailAddress());
				if (userWithSameEmail != null
						&& !userWithSameEmail.getUid().equals(id)) {
					emailAlreadyExistsError(errors, result,
							"Attempt by user with id " + id
									+ " to change email to an existing one");
				}
			}
			if (!errors.isEmpty()) {
				user.setUid(id);
				eraseViewUsersPasswordsAndConfCodes(user);
				map.addAttribute("user", user);
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
	public String usersPhotos(@PathVariable Integer id, ModelMap map,
			HttpServletRequest request, Principal principal) {
		logger.info("Viewing photos created by user with id " + id);
		PagedListHolder<Photo> photoList = new PagedListHolder<Photo>(
				userService.getUserPhotos(id, principal),
				new MutableSortDefinition(sortHelper.getDefaultSortPhoto(),
						true, true));
		photoHelper.addPhotosAndNewSearchToMap(photoList, request, map,
				SortTypes.MOST_RECENT.toString());
		map.addAttribute("user", userService.getUserInfo(id));
		if (principal != null && getCurrentUser(principal).getId().equals(id)) {
			map.addAttribute("userPhotoTitle", "label.title.usersphoto");
		}
		logger.info("Successfully retrieved photos created by user with id "
				+ id);
		return "photos/photo";
	}

	@RequestMapping(value = "/users/{id}/photos/sort", method = RequestMethod.GET)
	public String sortUsersPhotos(@ModelAttribute("photoSearch") Search search,
			BindingResult result, @PathVariable Integer id, ModelMap map,
			HttpServletRequest request, Principal principal) {

		logger.info("Sorting all photos created by user " + id);
		PagedListHolder<Photo> photoList = new PagedListHolder<Photo>(
				userService.getUserPhotos(id, principal),
				sortHelper.getSortDefinition(search.getSortType()));
		photoHelper.addPhotosAndExistingSearchToMap(photoList, request, map,
				search);
		map.addAttribute("user", userService.getUserInfo(id));
		if (principal != null && getCurrentUser(principal).getId().equals(id)) {
			map.addAttribute("userPhotoTitle", "label.title.usersphoto");
		}
		logger.info("Successfully sorted all photos created by user " + id);
		return "photos/photo";
	}

	@RequestMapping(value = "/users/{id}/morephotos", method = RequestMethod.GET)
	public String getMoreUsersPhotos(
			@PathVariable Integer id,
			ModelMap map,
			@RequestParam(value = "sortType", defaultValue = "Most Recent", required = false) String sortType,
			HttpServletRequest request) {
		logger.info("Getting more photos created by user " + id);
		PagedListHolder<Photo> photoList = (PagedListHolder) request
				.getSession().getAttribute("session_photolist");
		photoHelper.addMorePhotosAndNewSearchToMap(photoList, map, sortType);
		logger.info("Successfully add more photos created by user " + id);
		return "photos/_photo";

	}

	@RequestMapping(value = "/users/{id}/albums", method = RequestMethod.GET)
	public String usersAlbumTabShownFirst(@PathVariable Integer id,
			ModelMap map, Principal principal, HttpServletRequest request) {
		logger.info("Retrieving all albums created by user with id " + id);
		PagedListHolder<Album> albumList = new PagedListHolder<Album>(
				userService.getUserAlbums(id, principal),
				new MutableSortDefinition(sortHelper.getDefaultSortPhoto(),
						true, true));
		albumHelper.addAlbumsAndNewSearchToMap(albumList, request, map,
				SortTypes.MOST_RECENT.toString());
		map.addAttribute("user", userService.getUserInfo(id));
		if (principal != null && getCurrentUser(principal).getId().equals(id)) {
			map.addAttribute("userAlbumTitle", "label.title.usersalbum");
		}
		logger.info("Successfully retrieved albums created by user with id "
				+ id);
		return "albums/album";

	}

	@RequestMapping(value = "/users/{id}/albums/sort", method = RequestMethod.GET)
	public String sortUsersAlbums(@ModelAttribute("albumSearch") Search search,
			BindingResult result, @PathVariable Integer id, ModelMap map,
			HttpServletRequest request, Principal principal) {

		logger.info("Sorting all albums created by user " + id);
		PagedListHolder<Album> albumList = new PagedListHolder<Album>(
				userService.getUserAlbums(id, principal),
				sortHelper.getSortDefinition(search.getSortType()));
		albumHelper.addAlbumsAndExistingSearchToMap(albumList, request, map,
				search);
		map.addAttribute("user", userService.getUserInfo(id));
		if (principal != null && getCurrentUser(principal).getId().equals(id)) {
			map.addAttribute("userAlbumTitle", "label.title.usersalbum");
		}
		logger.info("Successfully sorted all albums created by user " + id);
		return "albums/album";
	}

	@RequestMapping(value = "/users/{id}/morealbums", method = RequestMethod.GET)
	public String getMoreUsersAlbums(
			@PathVariable Integer id,
			ModelMap map,
			@RequestParam(value = "sortType", defaultValue = "Most Recent", required = false) String sortType,
			HttpServletRequest request) {
		logger.info("Retrieving more albums created by user with id " + id);
		PagedListHolder<Album> albumList = (PagedListHolder) request
				.getSession().getAttribute("session_albumlist");
		albumHelper.addMoreAlbumsAndNewSearchToMap(albumList, map, sortType);
		logger.info("Completed retrieval of more albums created by user id "
				+ id);
		return "albums/_album";
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
			eraseViewUsersPasswordsAndConfCodes(user);
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
			FieldError fieldError = new FieldError("user", "confirmationCode",
					null, false,
					new String[] { "error.empty.confirmationcode" },
					new Object[] { new DefaultMessageSourceResolvable(
							new String[] { "error.empty.confirmationcode",
									"empty.confirmationcode",
									"confirmationcode", "confirmationcode" },
							"confirmationcode") }, "");
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
			FieldError fieldError = new FieldError(
					"user",
					"confirmationCode",
					null,
					false,
					new String[] { "error.activation.alreadyactivated" },
					new Object[] { new DefaultMessageSourceResolvable(
							new String[] { "error.activation.alreadyactivated",
									"activation.alreadyactivated",
									"alreadyactivated" }, "confirmationcode") },
					"");
			result.addError(fieldError);
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
			FieldError fieldError = new FieldError("user", "confirmationCode",
					null, false,
					new String[] { "error.activation.confirmationmismatch" },
					new Object[] { new DefaultMessageSourceResolvable(
							new String[] {
									"error.activation.confirmationmismatch",
									"activation.confirmationmismatch",
									"confirmationmismatch" },
							"confirmationcode") }, "");
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

	@RequestMapping(value = "/users/{id}/changepassword", method = RequestMethod.GET)
	public String changePassword(@PathVariable Integer id, Principal principal,
			ModelMap map) {
		logger.info("Direction user to the change password page");
		map.addAttribute("user", new User());
		return "users/changepwd";

	}

	@RequestMapping(value = "/users/{id}/updatepassword", method = RequestMethod.POST)
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
			if (!user.getPasswordOld().equals("")
					&& !user.getPasswordOld().equals(oldUser.getPasswordOld())) {
				FieldError passwordError = new FieldError("user",
						"passwordOld", null, false,
						new String[] { "error.password.wrongold" },
						new Object[] { new DefaultMessageSourceResolvable(
								new String[] { "error.password.wrongold",
										"password.wrongold", "wrongold" },
								"password") }, "");
				result.addError(passwordError);
				errors.add("error.password.wrongold");
			}
			if (!errors.isEmpty()) {
				map.addAttribute("errors", errors);
				logger.error("There were errors when changing the password of the user with id "
						+ id
						+ ". Redirecting the user to the change password page.");
				return "users/changepwd";
			} else {
				userService.changePassword(oldUser, user);
				eraseViewUsersPasswordsAndConfCodes(oldUser);
				redirectAttributes.addFlashAttribute("user", oldUser);
				redirectAttributes.addFlashAttribute("message",
						"label.message.user.changepasswordsuccessful");
				logger.info("Successfully updated user's password with id "
						+ id);
				return "redirect:/users/" + oldUser.getUid();
			}
		}
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public String forgotPassword(ModelMap map) {
		logger.info("Directing the user to the forgot password page");
		map.addAttribute("user", new User());
		return "users/forgotpassword";
	}

	@RequestMapping(value = "/forgotpassword/sendemail", method = RequestMethod.POST)
	public String forgotPasswordSendEmail(@ModelAttribute("user") User user,
			BindingResult result, ModelMap map, HttpServletRequest request) {
		logger.info("Changing the user's password to a random one and sending an email to their email");
		List<String> errors = new ArrayList<String>();
		if (user == null || user.getEmailAddress() == null
				|| user.getEmailAddress().equals("")) {
			logger.error("User entered in an empty email address during password recovery");
			FieldError fieldError = new FieldError("user", "emailAddress",
					null, false, new String[] { "NotEmpty.user.emailAddress" },
					new Object[] { new DefaultMessageSourceResolvable(
							new String[] { "NotEmpty.user.emailAddress",
									"user.emailAddress", "emailAddress" },
							"emailAddress") }, "");
			result.addError(fieldError);
			errors.add("NotEmpty.user.emailAddress");
			map.addAttribute("errors", errors);
			map.addAttribute("user", new User());
			return "users/forgotpassword";
		} else if (!user.getEmailAddress().matches(EMAIL_PATTERN)) {
			logger.error("User entered in an email address of incorrect format during password recovery");
			FieldError fieldError = new FieldError("user", "emailAddress",
					null, false, new String[] { "Email" },
					new Object[] { new DefaultMessageSourceResolvable(
							new String[] { "Email",
									"Email.user.emailAddress", "user.emailAddress" },
							"emailAddress") }, "");
			result.addError(fieldError);
			errors.add("Email");
			map.addAttribute("errors", errors);
			map.addAttribute("user", new User());
			return "users/forgotpassword";
		} else {
			User storedUser = userService
					.getUserByEmail(user.getEmailAddress());
			if (storedUser == null) {
				logger.error("User entered in an incorrect email address during password recovery");
				FieldError fieldError = new FieldError("user", "emailAddress",
						null, false, new String[] { "error.email.wrongemail" },
						new Object[] { new DefaultMessageSourceResolvable(
								new String[] { "error.email.wrongemail",
										"email.wrongemail", "wrongemail" },
								"emailAddress") }, "");
				result.addError(fieldError);
				errors.add("error.email.wrongemail");
				map.addAttribute("errors", errors);
				map.addAttribute("user", new User());
				return "users/forgotpassword";
			} else {
				userService.resetPassword(storedUser);
				String baseUrl = request
						.getRequestURL()
						.toString()
						.substring(
								0,
								request.getRequestURL().toString()
										.indexOf("user"));

				String sendingURL = baseUrl + "/login";

				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom("photolib@noreply.com");
				message.setTo(user.getEmailAddress());
				message.setSubject("Photo Library Password Reset");
				message.setText("You have requested to reset your password. Please go to the link below "
						+ "and use the following password to login to your account: \n"
						+ "Password: "
						+ user.getPassword()
						+ "\n"
						+ "Once you login, you will be prompted to change your password to a new one before "
						+ "you can continue with any further action."
						+ "Link: " + sendingURL);

				mailSender.send(message);

				logger.info("The user has successfully started password recovery with email "
						+ user.getEmailAddress());
				return "users/forgotpasswordsendemail";
			}
		}
	}

	private void emailAlreadyExistsError(List<String> errors,
			BindingResult result, String logErrorMsg) {
		logger.error(logErrorMsg);
		String emailAlreadyExistsError = "error.user.emailAddress.emailalreadyexists";
		FieldError fieldError = new FieldError("user", "emailAddress", null,
				false,
				new String[] { "error.user.emailAddress.emailalreadyexists" },
				new Object[] { new DefaultMessageSourceResolvable(new String[] {
						"error.user.emailAddress.emailalreadyexists",
						"error.user.emailAddress", "user.emailAddress",
						"emailAddress" }, "emailAddress") }, "");
		result.addError(fieldError);
		errors.add(emailAlreadyExistsError);
	}

	private void eraseViewUsersPasswordsAndConfCodes(User user) {
		user.setPassword("");
		user.setPasswordConfirm("");
		user.setConfirmationCode("");
		user.setStoredConfirmationCode("");
		user.setPasswordOld("");
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
