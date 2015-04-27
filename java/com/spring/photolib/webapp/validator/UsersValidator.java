package com.spring.photolib.webapp.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.spring.photolib.webapp.domain.User;
import com.spring.photolib.webapp.service.UserService;

@Component
public class UsersValidator implements Validator {

	@Autowired
	UserService userService;
	
	public boolean supports(Class clazz) {
		return User.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors) {
		User user = (User) obj;
		if (user.getPassword() != null && user.getPasswordConfirm() != null
				&& !user.getPassword().equals(user.getPasswordConfirm())) {
			errors.rejectValue("password", "error.password.mismatch",
					"password.mismatch");
			errors.rejectValue("passwordConfirm", "error.password.mismatch",
					"password.mismatch");
		}

	}

}
