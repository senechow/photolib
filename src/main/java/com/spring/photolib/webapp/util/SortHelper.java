package com.spring.photolib.webapp.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.support.MutableSortDefinition;

import com.spring.photolib.webapp.domain.Photo;

public class SortHelper {

	public SortHelper() {
	}
	
	public String getDefaultSortPhoto() {
		return "creationDate";
	}
	
	public MutableSortDefinition getSortDefinition(String sortType) {
		
		MutableSortDefinition sortDefinition;
		String property;
		if (sortType.equals("Alphabetical")) {
			 property = "name";
			 sortDefinition = new MutableSortDefinition(property, true, true);
		} else if (sortType.equals("Most Recent")) {
			property = "creationDate";
			 sortDefinition = new MutableSortDefinition(property, true, false);
		} else if (sortType.equals("Top Rated")) {
			 property = "rating.rating";
			 sortDefinition = new MutableSortDefinition(property, true, false);
		} else {
			property = "viewCount";
			 sortDefinition = new MutableSortDefinition(property, true, false);
		}
		
		return sortDefinition;
		
	}

	@Deprecated
	public String setOrderBy(String sortType) {
		String orderBy = null;
		if (sortType.equals("Alphabetical")) {
			orderBy = "order by p.name asc";

		} else if (sortType.equals("Most Recent")) {
			orderBy = "order by p.creationDate desc";
		}

		else if (sortType.equals("Top Rated")) {
			orderBy = "order by p.rating.rating desc";
		}
		return orderBy;
	}


}
