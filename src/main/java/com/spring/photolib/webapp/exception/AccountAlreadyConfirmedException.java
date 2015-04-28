package com.spring.photolib.webapp.exception;

public class AccountAlreadyConfirmedException extends Exception {
	
	public AccountAlreadyConfirmedException() {
		super();
	}
	
	public AccountAlreadyConfirmedException(String msg) {
		super(msg);
	}

}
