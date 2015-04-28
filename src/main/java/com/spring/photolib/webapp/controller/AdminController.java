package com.spring.photolib.webapp.controller;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.exception.UnauthorizedException;
import com.spring.photolib.webapp.service.FlagService;
import com.spring.photolib.webapp.service.UserService;

@Controller
public class AdminController {

	@Autowired
	private FlagService flagService;
	@Autowired
	private UserService userService;
	private Logger logger = Logger.getLogger(AdminController.class);

	@RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
	public String adminDashboard(ModelMap map, Principal principal) {
		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to access the admin dashboard";
		String registedUserLogMsg = "Unauthorized attempt by a registed user to access the admin dashboard with id ";
		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
		map.addAttribute("topTenPhotoFlags", flagService.listFlaggedMostRecentTenPhotos());
		map.addAttribute("topTenAlbumFlags", flagService.listFlaggedMostRecentTenAlbums());
		map.addAttribute("topTenUsersFlags", flagService.listFlaggedMostRecentTenUsers());
		map.addAttribute("photoFlags", flagService.listFlaggedPhotos());
		map.addAttribute("albumFlags", flagService.listFlaggedAlbums());
		map.addAttribute("userFlags", flagService.listFlaggedUsers());
		map.addAttribute("bannedUsersFlags", flagService.listBannedUsers());
		logger.info("Successfully navigated to admin dashboard");
		return "admin/admindashboard";
	}

//	@RequestMapping(value = "/admin/dashboard/flaggedphotos", method = RequestMethod.GET)
//	public String getFlaggedPhotos(ModelMap map, Principal principal) {
//		logger.info("Showing more flagged photos");
//		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to access the flagged photos section of the admin dashboard";
//		String registedUserLogMsg = "Unauthorized attempt by a registed user to access the flagged photos section of the admin dashboard with id ";
//		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
//		map.addAttribute("photoFlags", flagService.listFlaggedPhotos());
//		logger.info("Finished showing more flagged photos");
//		return "admin/admindashboard";
//	}
//
//	@RequestMapping(value = "/admin/dashboard/flaggedalbums", method = RequestMethod.GET)
//	public String getFlaggedAlbums(ModelMap map, Principal principal) {
//		logger.info("Showing more flagged albums");
//		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to access the flagged albums section of the admin dashboard";
//		String registedUserLogMsg = "Unauthorized attempt by a registed user to access the flagged albums section of the admin dashboard with id ";
//		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
//		map.addAttribute("albumFlags", flagService.listFlaggedAlbums());
//		logger.info("Finished showing more flagged albums");
//		return "admin/admindashboard";
//	}
//
//	@RequestMapping(value = "/admin/dashboard/flaggedusers", method = RequestMethod.GET)
//	public String getFlaggedUsers(ModelMap map, Principal principal) {
//		logger.info("Showing more flagged users");
//		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to access the flagged users section of the admin dashboard";
//		String registedUserLogMsg = "Unauthorized attempt by a registed user to access the flagged users section of the admin dashboard with id ";
//		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
//		map.addAttribute("userFlags", flagService.listFlaggedUsers());
//		logger.info("Finished showing more flagged users");
//		return "admin/admindashboard";
//	}
//	
//	@RequestMapping(value = "/admin/dashboard/bannedusers", method = RequestMethod.GET)
//	public String getBannedUsers(ModelMap map, Principal principal) {
//		logger.info("Showing more banned users");
//		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to access the banned users section of the admin dashboard";
//		String registedUserLogMsg = "Unauthorized attempt by a registed user to access the banned users section of the admin dashboard with id ";
//		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
//		map.addAttribute("bannedUsersFlags", flagService.listBannedUsers());
//		logger.info("Finished showing more banned users");
//		return "admin/admindashboard";
//	}
	
	@RequestMapping(value="/admin/{id}/banuser", method = RequestMethod.POST)
	public String banUser(@PathVariable Integer id, Principal principal,
			RedirectAttributes redirectAttributes) {
		logger.info("Banning user with id " + id);
		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to ban a user with id " + id;
		String registedUserLogMsg = "Unauthorized attempt to ban user with id " + id + " by a registered user with the id ";
		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
		AuthorizedUser authUser = getCurrentUser(principal);
		userService.banUser(id);
		redirectAttributes.addFlashAttribute("message", "label.message.ban.bansuccessful");
		logger.info("Successfully banned user with id " + id);
		return "redirect:/admin/dashboard";
	}
	
	@RequestMapping(value="/admin/{id}/unbanuser", method = RequestMethod.POST)
	public String unbanUser(@PathVariable Integer id, RedirectAttributes redirectAttributes, Principal principal) {
		logger.info("Unbanning user with id " + id);
		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to unban a user with id " + id;
		String registedUserLogMsg = "Unauthorized attempt to unban user with id " + id + " by a registered user with the id ";
		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
		userService.unbanUser(id);
		redirectAttributes.addFlashAttribute("message", "label.message.unban.unbansuccessful");
		logger.info("Successfully unbanned user with id " + id);
		return "redirect:/admin/dashboard";
	}
	
	@RequestMapping(value="/admin/flag/{fid}/delete", method = RequestMethod.DELETE)
	public String deletePhotoFlag(@PathVariable Integer fid, RedirectAttributes redirectAttributes, Principal principal) {
		logger.info("Deleting flag with " + fid);
		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to delete a flag with id " + fid;
		String registedUserLogMsg = "Unauthorized attempt to delete a flag with id " + fid + " by a registered user with the id ";
		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
		flagService.deleteFlag(fid);
		redirectAttributes.addFlashAttribute("message", "label.message.flag.deletesuccessful");
		logger.info("Successfully deleted flag with id " + fid);
		return "redirect:/admin/dashboard";
	}
	
	/*
	@RequestMapping(value="/admin/photoflag/{fid}/delete", method = RequestMethod.DELETE)
	public String deletePhotoFlag(@PathVariable Integer fid, RedirectAttributes redirectAttributes, Principal principal) {
		logger.info("Deleting photo flag with " + fid);
		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to delete a photo flag with id " + fid;
		String registedUserLogMsg = "Unauthorized attempt to delete a photo flag with id " + fid + " by a registered user with the id ";
		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
		flagService.deletePhotoFlag(fid);
		redirectAttributes.addFlashAttribute("message", "label.message.photoflag.deletesuccessful");
		logger.info("Successfully deleted photo flag with id " + fid);
		return "redirect:/admin/dashboard";
	}
	
	@RequestMapping(value="/admin/albumflag/{fid}/delete", method = RequestMethod.DELETE)
	public String deleteAlbumFlag(@PathVariable Integer fid, RedirectAttributes redirectAttributes, Principal principal) {
		logger.info("Deleting album flag with " + fid);
		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to delete an album flag with id " + fid;
		String registedUserLogMsg = "Unauthorized attempt to delete an album flag with id " + fid + " by a registered user with the id ";
		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
		flagService.deleteFlag(fid);
		redirectAttributes.addFlashAttribute("message", "label.message.albumflag.deletesuccessful");
		logger.info("Successfully deleted album flag with id " + fid);
		return "redirect:/admin/dashboard";
	}
	
	@RequestMapping(value="/admin/userflag/{fid}/delete", method = RequestMethod.DELETE)
	public String deleteUserFlag(@PathVariable Integer fid, RedirectAttributes redirectAttributes, Principal principal) {
		logger.info("Deleting flag with " + fid);
		String unregisteredUserLogMsg = "Unauthorized attempt by an non registered user to delete a user flag with id " + fid;
		String registedUserLogMsg = "Unauthorized attempt to delete a user flag with id " + fid + " by a registered user with the id ";
		checkIfAdmin(principal, unregisteredUserLogMsg,registedUserLogMsg );
		flagService.deleteUserFlag(fid);
		redirectAttributes.addFlashAttribute("message", "label.message.userflag.deletesuccessful");
		logger.info("Successfully deleted user flag with id " + fid);
		return "redirect:/admin/dashboard";
	}
	*/
	
	private void checkIfAdmin(Principal principal, String nonRegisteredUserMsg, String registeredUserMsg) {
		if(principal == null) {
			logger.error(nonRegisteredUserMsg);
			throw new UnauthorizedException();
		}
		AuthorizedUser authUser = getCurrentUser(principal);
		if (!authUser.getRole().getRole().equals("admin")) {
			logger.error(registeredUserMsg + authUser.getId());
			throw new UnauthorizedException();
		}
	}

	private AuthorizedUser getCurrentUser(Principal principal) {
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		return user;
	}

}
