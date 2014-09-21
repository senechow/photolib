package com.spring.photolib.webapp.exception;

public class AlreadyRatedException extends Exception {

	public AlreadyRatedException() {
		super();
	}
	
	public AlreadyRatedException(String msg) {
		super(msg);
	}
}
