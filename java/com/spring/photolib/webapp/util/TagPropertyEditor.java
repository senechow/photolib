package com.spring.photolib.webapp.util;

import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.spring.photolib.webapp.domain.Tag;

public class TagPropertyEditor extends PropertyEditorSupport{
	
	@Override
	public void setAsText(String text) {
		Set<Tag> tagSet = new HashSet<Tag>();
		String unformatedTagNames = text.replaceAll("\\s", "");
		if(unformatedTagNames.equals("")) {
			return;
		}
		boolean endOfString = false;
		while(!endOfString){
			Tag tag = new Tag();
			if(unformatedTagNames.contains(",")) {
				tag.setName(unformatedTagNames.substring(0, unformatedTagNames.indexOf(",")));
				unformatedTagNames = unformatedTagNames.substring(unformatedTagNames.indexOf(",") + 1);
				
			}
			else {
				tag.setName(unformatedTagNames);
				endOfString = true;
			}
			tagSet.add(tag);
		}
		this.setValue(tagSet);
	}
	
	@Override
	public String getAsText() {
		Set<Tag> tagSet = (Set<Tag>)this.getValue();
		String text = "";
		if(tagSet != null && !tagSet.isEmpty()) {
			for(Tag tag : tagSet) {
				text = text.concat(tag.getName() + ",");
			}
			text = text.substring(0, text.length() - 1);
		}
		
		return text;
	}


}
