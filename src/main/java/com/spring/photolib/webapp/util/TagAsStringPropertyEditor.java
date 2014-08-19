package com.spring.photolib.webapp.util;

import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.spring.photolib.webapp.domain.Tag;

public class TagAsStringPropertyEditor extends PropertyEditorSupport{
	
	@Override
	public void setAsText(String text) {
		Set<String> tagAsStringSet = new HashSet<String>();
		String unformatedTagNames = text.replaceAll("\\s", "");
		if(unformatedTagNames.equals("")) {
			return;
		}
		boolean endOfString = false;
		while(!endOfString){
			String tagString;
			if(unformatedTagNames.contains(",")) {
				tagString = (unformatedTagNames.substring(0, unformatedTagNames.indexOf(",")));
				unformatedTagNames = unformatedTagNames.substring(unformatedTagNames.indexOf(",") + 1);
				
			}
			else {
				tagString = unformatedTagNames;
				endOfString = true;
			}
			tagAsStringSet.add(tagString);
		}
		this.setValue(tagAsStringSet);
	}
	
	@Override
	public String getAsText() {
		Set<String> tagStringSet = (Set<String>)this.getValue();
		String text = "";
		if(tagStringSet != null && !tagStringSet.isEmpty()) {
			for(String tagString : tagStringSet) {
				text.concat(tagString + ",");
			}
			text = text.substring(0, text.length());
		}
		
		return text;
	}


}
