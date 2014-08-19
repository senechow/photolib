package com.spring.photolib.webapp.exception;

public class PhotoAlreadyRatedException extends Exception {

	public PhotoAlreadyRatedException() {
		super();
	}
	
	public PhotoAlreadyRatedException(String msg) {
		super(msg);
	}
}
