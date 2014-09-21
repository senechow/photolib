$(document).ready(function() {

	function split(val) {
		return val.split(/,\s*/);
	}

	function extractLast(term) {
		return split(term).pop();
	}
	
	function getRootUrl() {
		return window.location.origin = window.location.protocol+"//"+window.location.host;
		// + "/" + window.location.pathname.split('/')[1];
		// For running locally on eclipse
	}
	
	var rootUrl = getRootUrl();
	$("#tagsid").tagit({
		
		autocomplete : {
			source : function(request, response) {
//				$.getJSON("/PhotoLibrary/tags", {
				$.getJSON(rootUrl + "/tags", {
					term : extractLast(request.term)
				}, function(result) {
					response($.map(result, function(tag) {
						return {
							value : tag.name,
							data : tag.id
						};
					}));
				});
			}
		}
	});

});
