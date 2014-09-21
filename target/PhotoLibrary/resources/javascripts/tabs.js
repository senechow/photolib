$('#adminTabs a').click(function(e) {
	e.preventDefault();
	$(this).tab('show');
});

$('#adminTabs li:eq(1) a').tab('show');
$('#adminTabs li:eq(2) a').tab('show');
$('#adminTabs li:eq(3) a').tab('show');
$('#adminTabs li:eq(4) a').tab('show');
$('#adminTabs a:first').tab('show');

$('#flagged-photo-container').simplePagination({
	items_per_page : 6,
	number_of_visible_page_numbers : 6,
	first_content : '<<',
	previous_content : '<',
	next_content : '>',
	last_content : '>>'
});

$('#flagged-album-container').simplePagination({
	items_per_page : 6,
	number_of_visible_page_numbers : 6,
	first_content : '<<',
	previous_content : '<',
	next_content : '>',
	last_content : '>>'
});

$('#flagged-users-container').simplePagination({
	items_per_page : 6,
	number_of_visible_page_numbers : 6,
	first_content : '<<',
	previous_content : '<',
	next_content : '>',
	last_content : '>>'
});

$('#banned-users-container').simplePagination({
	items_per_page : 6,
	number_of_visible_page_numbers : 6,
	first_content : '<<',
	previous_content : '<',
	next_content : '>',
	last_content : '>>'
});

function httpGet(theUrl) {
	var xmlHttp = null;

	xmlHttp = new XMLHttpRequest();
	xmlHttp.open("GET", theUrl, false);
	xmlHttp.send(null);
	return xmlHttp.responseText;
}
