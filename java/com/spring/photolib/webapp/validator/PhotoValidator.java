package com.spring.photolib.webapp.validator;

import org.springframework.web.multipart.MultipartFile;

import com.spring.photolib.webapp.domain.Photo;

public class PhotoValidator {
	
	private static final String[] ACCEPTED_CONTENT_TYPE = new String [] {
		"image/jpeg",
		"image/png",
		"image/gif"
	};
	
	public boolean hasEmptyName(Photo photo) {
		boolean emptyName = false;
		if(photo.getName().equals("") || photo.getName() == null) {
			emptyName = true;
		}
		return emptyName;
	}
	
	
	public boolean hasEmptyImage(MultipartFile image) {
		
		boolean empty = false;
		
		if(image.getSize() == 0) {
			empty = true;
		}
		
		return empty;
		
	}
	
	public boolean hasAcceptedImageType(MultipartFile image) {
		boolean isAccepted = false;
		
		for(String acceptedType : ACCEPTED_CONTENT_TYPE) {
			if(image.getContentType().equalsIgnoreCase(acceptedType)) {
				isAccepted = true;
				break;
			}
		}
		
		return isAccepted;
	}

}
