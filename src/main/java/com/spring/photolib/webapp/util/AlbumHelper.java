package com.spring.photolib.webapp.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.ui.ModelMap;

import com.spring.photolib.webapp.domain.Album;
import com.spring.photolib.webapp.domain.Search;

public class AlbumHelper {
	
	public void addAlbumsAndNewSearchToMap(PagedListHolder<Album> albumList,
			HttpServletRequest request, ModelMap map, String sortType) {
		addAlbumsToMap(albumList, request, map);
		addNewSearchToMap(sortType, map);
	}

	public void addAlbumsAndExistingSearchToMap(
			PagedListHolder<Album> albumList, HttpServletRequest request,
			ModelMap map, Search search) {
		addAlbumsToMap(albumList, request, map);
		addExistingSearchToMap(search, map);
	}

	public void addMoreAlbumsAndNewSearchToMap(
			PagedListHolder<Album> albumList, ModelMap map, String sortType) {
		addMoreAlbumsToMap(albumList, map);
		addNewSearchToMap(sortType, map);
	}
	
	public void addMoreAlbumsAndExistingSearchToMap(
			PagedListHolder<Album> albumList, ModelMap map, Search search) {
		addMoreAlbumsToMap(albumList, map);
		addExistingSearchToMap(search, map);
	}

	private void addAlbumsToMap(PagedListHolder<Album> albumList,
			HttpServletRequest request, ModelMap map) {

		albumList.resort();
		albumList.setPageSize(6);
		map.addAttribute("albumList", albumList.getPageList());
		request.getSession().setAttribute("session_albumlist", albumList);
	}

	private void addNewSearchToMap(String sortType, ModelMap map) {
		Search search = new Search();
		search.setSortType(sortType);
		map.addAttribute("albumSearch", search);
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
	}

	private void addExistingSearchToMap(Search search, ModelMap map) {
		map.addAttribute("albumSearch", search);
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
	}

	private void addMoreAlbumsToMap(PagedListHolder<Album> albumList,
			ModelMap map) {
		if (albumList != null && !albumList.isLastPage()) {
			albumList.nextPage();
			map.addAttribute("albumList", albumList.getPageList());
		}
	}

}
