package com.spring.photolib.webapp.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.photolib.webapp.domain.Tag;
import com.spring.photolib.webapp.service.TagService;

@Controller
public class TagController {
	
	@Autowired
	private TagService tagService;
	
	private Logger logger = Logger.getLogger(TagController.class);
	
	@RequestMapping(value="/tags", method=RequestMethod.GET)
	public @ResponseBody List<Tag> getSuggestedTags(@RequestParam String term) {
		logger.info("Querying for tag with " + term);
		return tagService.getSuggestedTags(term);
	}
	
	@RequestMapping(value="tags/{tid}", method=RequestMethod.GET)
	public String clickOnTag(@PathVariable Integer tid, ModelMap map) {
		logger.info("Getting tag with photos with id " + tid);
		map.addAttribute("tag", tagService.getTagAndPhotosById(tid));
		return "tags/taggedphotos";
	}

}
