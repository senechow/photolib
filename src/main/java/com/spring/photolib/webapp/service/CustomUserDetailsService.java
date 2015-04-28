package com.spring.photolib.webapp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.spring.photolib.webapp.domain.AuthorizedUser;
import com.spring.photolib.webapp.domain.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.photolib.webapp.dao.UserDao;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDAO;

	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {

		User domainUser = userDAO.getUserByEmail(email);

		boolean enabled;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked;

		if(domainUser.getActivated() && domainUser.getConfirmationCode().equals(domainUser.getStoredConfirmationCode())) {
			enabled = true;
		} else{
			enabled = false;
		}
		
		if(domainUser.getBanned()) {
			accountNonLocked = false;
		} else {
			accountNonLocked = true;
		}
		
		AuthorizedUser appUser = new AuthorizedUser(
				domainUser.getEmailAddress(), domainUser.getPassword(),
				enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, getAuthorities(domainUser.getRole().getRid()));

		appUser.setId(domainUser.getUid());
		appUser.setEmailAddress(domainUser.getEmailAddress());
		appUser.setCreationDate(domainUser.getCreationDate());
		appUser.setFirstName(domainUser.getFirstName());
		appUser.setLastName(domainUser.getLastName());
		appUser.setPhotos(domainUser.getPhotos());
		appUser.setRole(domainUser.getRole());
		appUser.setAlbums(domainUser.getAlbums());
		appUser.setUserName(domainUser.getUserName());
		appUser.setBanned(domainUser.getBanned());

		return appUser;
	}

	public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}

	public List<String> getRoles(Integer role) {

		List<String> roles = new ArrayList<String>();

		if (role.intValue() == 1) {
			roles.add("ROLE_ADMIN");

		} else if (role.intValue() == 2) {
			roles.add("ROLE_REGISTERED_USER");
		}
		
		return roles;
	}

	public static List<GrantedAuthority> getGrantedAuthorities(
			List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
