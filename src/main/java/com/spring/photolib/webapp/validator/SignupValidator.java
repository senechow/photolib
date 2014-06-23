package com.spring.photolib.webapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.spring.photolib.webapp.domain.User;

@Component
public class SignupValidator implements Validator {

	public boolean supports(Class clazz) {
		return User.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors) {
		User user = (User) obj;
		
		if(!user.getPassword().equals(user.getPasswordConfirm())) {
			errors.rejectValue("password", "error.password.mismatch", "password.mismatch");
		}

	}

}
