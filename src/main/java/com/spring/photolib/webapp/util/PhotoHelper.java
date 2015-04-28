package com.spring.photolib.webapp.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.ui.ModelMap;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;

public class PhotoHelper {

	public void addPhotosAndNewSearchToMap(PagedListHolder<Photo> photoList,
			HttpServletRequest request, ModelMap map, String sortType) {
		addPhotosToMap(photoList, request, map);
		addNewSearchToMap(sortType, map);
	}

	public void addPhotosAndExistingSearchToMap(
			PagedListHolder<Photo> photoList, HttpServletRequest request,
			ModelMap map, Search search) {
		addPhotosToMap(photoList, request, map);
		addExistingSearchToMap(search, map);
	}

	public void addMorePhotosAndNewSearchToMap(
			PagedListHolder<Photo> photoList, ModelMap map, String sortType) {
		addMorePhotosToMap(photoList, map);
		addNewSearchToMap(sortType, map);
	}
	
	public void addMorePhotosAndExistingSearchToMap(
			PagedListHolder<Photo> photoList, ModelMap map, Search search) {
		addMorePhotosToMap(photoList, map);
		addExistingSearchToMap(search, map);
	}

	private void addPhotosToMap(PagedListHolder<Photo> photoList,
			HttpServletRequest request, ModelMap map) {

		photoList.resort();
		photoList.setPageSize(6);
		map.addAttribute("photoList", photoList.getPageList());
		request.getSession().setAttribute("session_photolist", photoList);
	}

	private void addNewSearchToMap(String sortType, ModelMap map) {
		Search search = new Search();
		search.setSortType(sortType);
		map.addAttribute("photoSearch", search);
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
	}

	private void addExistingSearchToMap(Search search, ModelMap map) {
		map.addAttribute("photoSearch", search);
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
	}

	private void addMorePhotosToMap(PagedListHolder<Photo> photoList,
			ModelMap map) {
		if (photoList != null && !photoList.isLastPage()) {
			photoList.nextPage();
			map.addAttribute("photoList", photoList.getPageList());
		}
	}

}
