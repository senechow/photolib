package com.spring.photolib.webapp.util;

import java.util.ArrayList;
import java.util.List;

public enum SortTypes {
	
	MOST_RECENT("Most Recent"),
	ALPHABETICAL("Alphabetical"),
	TOP_RATED("Top Rated"),
	VIEW_COUNT("View Count");
	
	private String value;
	
	private SortTypes(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
	
	public static List<String> getSortTypesAsStrings() {
		List<String> sortTypesAsString = new ArrayList<String>();
		for(SortTypes sortType : values()) {
			sortTypesAsString.add(sortType.toString());
		}
		
		return sortTypesAsString;
	}

}
