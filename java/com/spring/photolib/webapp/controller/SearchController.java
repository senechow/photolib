package com.spring.photolib.webapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;
import com.spring.photolib.webapp.service.SearchService;
import com.spring.photolib.webapp.util.PhotoHelper;
import com.spring.photolib.webapp.util.SortHelper;
import com.spring.photolib.webapp.util.SortTypes;
import com.spring.photolib.webapp.util.TagAsStringPropertyEditor;
import com.spring.photolib.webapp.util.TagPropertyEditor;

@Controller
public class SearchController {

	@Autowired
	SearchService searchService;
	SortHelper sortHelper = new SortHelper();
	PhotoHelper photoHelper = new PhotoHelper();

	private Logger logger = Logger.getLogger(SearchController.class);

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchByName(@RequestParam("searchQuery") String searchQuery,
			Principal principal, ModelMap map, HttpServletRequest request) {
		logger.info("Searching for photos with query " + searchQuery);
		if (searchQuery.equals("")) {
			logger.error("Error, basic search cannot be done with an empty string!");
			String referer = request.getHeader("Referer");
			return "redirect:" + referer;
		}
		Search search = new Search();
		search.setSortType(SortTypes.MOST_RECENT.toString());
		map.addAttribute("emptyPhotoMsg", "label.emptysearchphoto");
		map.addAttribute("photoList",
				searchService.basicSearchPhotos(searchQuery, principal));
		map.addAttribute("search", search);
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
		logger.info("Search for photos is completed with query " + searchQuery);
		return "photos/photo";
	}

	@RequestMapping(value = "/advancedsearch", method = RequestMethod.GET)
	public String advancedSearchPage(ModelMap map) {
		logger.info("Navigating to the advanced search page");
		map.addAttribute("photoSearch", new Search());
		map.addAttribute("sortingSelections", SortTypes.getSortTypesAsStrings());
		return "search/advancedsearch";
	}

	@RequestMapping(value = "/advancedsearch/search", method = RequestMethod.GET)
	public String submitAdvancedSearch(
			@ModelAttribute("photoSearch") Search search,
			BindingResult result,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			Principal principal, ModelMap map, HttpServletRequest request) {

		logger.info("Performing advanced search with query " + search.getName());
		PagedListHolder<Photo> photoList = new PagedListHolder<Photo>(
				searchService.advancedSearchPhotos(search, principal),
				sortHelper.getSortDefinition(search.getSortType()));
		photoHelper.addPhotosAndExistingSearchToMap(photoList, request, map, search);
		map.addAttribute("emptyPhotoMsg", "label.emptysearchphoto");
		logger.info("Advanced search completed with query " + search.getName());
		return "photos/advancedsearchphotos";
	}

	@RequestMapping(value = "/advancedsearch/search/morephotos", method = RequestMethod.GET)
	public String addPhotosAdvancedSearch(
			@ModelAttribute("photoSearch") Search search,
			BindingResult result,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			Principal principal, ModelMap map, HttpServletRequest request) {
		logger.info("Getting more photos for advanced search with name "
				+ search.getName());
		map.addAttribute("emptyPhotoMsg", "label.emptysearchphoto");
		PagedListHolder <Photo> photoList = (PagedListHolder) request.getSession()
				.getAttribute("session_photolist");
		photoHelper.addMorePhotosAndExistingSearchToMap(photoList, map, search);
		logger.info(""
				+ "Completed retrieving more photos for search with name "
				+ search.getName());
		return "photos/_photo";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Set.class, "tags",
				new TagAsStringPropertyEditor());
	}
}
