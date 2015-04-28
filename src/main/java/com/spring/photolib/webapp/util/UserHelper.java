package com.spring.photolib.webapp.util;

import java.security.Principal;

import org.hibernate.SessionFactory;
import org.springframework.security.core.Authentication;

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.User;

public class UserHelper {
	
	public UserHelper() {}
	
	public User getCurrentUser(Principal principal, SessionFactory sessionFactory) {
		AuthorizedUser user = (AuthorizedUser) ((Authentication) principal)
				.getPrincipal();
		return (User) sessionFactory.getCurrentSession().get(User.class,
				user.getId());
	}

}
