package com.spring.photolib.webapp.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.service.UserService;

@Controller
public class SessionController {
	
	@Autowired
	private UserService userService;
	
	private Logger logger = Logger.getLogger(SessionController.class);
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String userLogin(ModelMap map) {
		logger.info("Directing to log in page");
		map.addAttribute("user", new User());
		return "users/login";
	}
	
	@RequestMapping(value="/login-failed", method=RequestMethod.GET)
	public String invalidLogin(ModelMap map) {
		logger.error("Login failed due to errors.");
		map.addAttribute("user", new User());
		map.addAttribute("hasError", true);
		return "users/login";
	}
	
	@RequestMapping(value="/login-successful", method=RequestMethod.GET)
	public String successfulLogin() {
		logger.info("User has successfully logged in");
		return "redirect:/";
	}
	
	@RequestMapping(value="logout", method=RequestMethod.GET)
	public String logout( RedirectAttributes redirectAttributes) {
		logger.info("User has successfully logged out");
		redirectAttributes.addFlashAttribute("message", "label.message.logout");
		return "redirect:/";
	}
	
}
