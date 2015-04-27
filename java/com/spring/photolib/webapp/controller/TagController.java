package com.spring.photolib.webapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Search;
import com.spring.photolib.webapp.domain.Tag;
import com.spring.photolib.webapp.service.TagService;
import com.spring.photolib.webapp.util.PhotoHelper;
import com.spring.photolib.webapp.util.SortHelper;
import com.spring.photolib.webapp.util.SortTypes;

@Controller
public class TagController {

	@Autowired
	private TagService tagService;

	private Logger logger = Logger.getLogger(TagController.class);
	private SortHelper sortHelper = new SortHelper();
	private PhotoHelper photoHelper = new PhotoHelper();
	
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public @ResponseBody
	List<Tag> getSuggestedTags(@RequestParam String term) {
		logger.info("Querying for tag with " + term);
		return tagService.getSuggestedTags(term);
	}

	@RequestMapping(value = "tags/{tid}", method = RequestMethod.GET)
	public String clickOnTag(@PathVariable Integer tid, ModelMap map, HttpServletRequest request) {
		logger.info("Getting tag and its photos with id " + tid);
		Tag tag = tagService.getTagAndPhotosById(tid);
		PagedListHolder<Photo> photoList = new PagedListHolder<Photo>(new ArrayList(tag.getPhotos()),
				new MutableSortDefinition(sortHelper.getDefaultSortPhoto(), true, true));
		photoHelper.addPhotosAndNewSearchToMap(photoList, request, map, SortTypes.MOST_RECENT.toString());
		map.addAttribute("tag", tag);
		logger.info("Finished getting tag and its photos with id " + tid);
		return "tags/taggedphotos";
	}

	@RequestMapping(value = "tags/{tid}/morephotos", method = RequestMethod.GET)
	public String getMorePhotosFromTag(@PathVariable Integer tid, 
			@RequestParam(value = "sortType", defaultValue = "Most Recent", required = false) String sortType,
			ModelMap map, HttpServletRequest request) {
		logger.info("Getting more photos for tag with id " + tid);
		PagedListHolder <Photo> photoList = (PagedListHolder ) request.getSession()
				.getAttribute("session_photolist");
		photoHelper.addMorePhotosAndNewSearchToMap(photoList, map, sortType);
		logger.info("More photos have been added for tag with id " + tid);
		return "photos/_photo";
	}
	
	@RequestMapping(value="tags/{tid}/sort", method = RequestMethod.GET)
	public String sortPhotosInTag (@ModelAttribute("photoSearch") Search search,
			BindingResult result, ModelMap map, Principal principal, HttpServletRequest request, 
			@PathVariable Integer tid) {
		logger.info("Sorting photos for tag with id " + tid);
		logger.info("Finished sorting photos for tag with id " + tid);
		Tag tag = tagService.getTagAndPhotosById(tid);
		PagedListHolder<Photo> photoList = new PagedListHolder<Photo>(
			new ArrayList(tag.getPhotos()) , sortHelper.getSortDefinition(search.getSortType()));
		photoHelper.addPhotosAndExistingSearchToMap(photoList, request, map, search);
		map.addAttribute("emptyPhotoMsg", "label.emptysearchphoto");
		map.addAttribute("tag", tag);
		return "tags/taggedphotos"; 
	}

}
