$('#adminTabs a').click(function(e) {
	e.preventDefault();
	$(this).tab('show');
});

$('#adminTabs li:eq(1) a').tab('show');
$('#adminTabs li:eq(2) a').tab('show');
$('#adminTabs li:eq(3) a').tab('show');
$('#adminTabs li:eq(4) a').tab('show');
$('#adminTabs a:first').tab('show');

//$('#Flagged-Photo-Tabs').click(function(e) {
//	httpGet("/PhotoLibrary/admin/dashboard/flaggedphotos");
//});
//
//$('#Flagged-Album-Tabs').click(function(e) {
//	httpGet("/PhotoLibrary/admin/dashboard/flaggedalbums");
//});
//
//$('#Flagged-User-Tabs').click(function(e) {
//	httpGet("/PhotoLibrary/admin/dashboard/flaggedusers");
//});
//
//$('#Banned-User-Tabs').click(function(e) {
//	httpGet("/PhotoLibrary/admin/dashboard/bannedusers");
//});

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
