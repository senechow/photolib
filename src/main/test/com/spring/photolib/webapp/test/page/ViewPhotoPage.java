package com.spring.photolib.webapp.test.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ViewPhotoPage{
	
	@FindBy(id = "newPhotoBtn")
	WebElement newPhotoButton;
	
	public void selectNewPhotoButton() {
		newPhotoButton.click();
	}

}
