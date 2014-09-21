/**
 * 
 */
 $(document).ready(function(){
	 
	 function getRootUrl() {
			return window.location.origin = window.location.protocol+"//"+window.location.host;
			// + "/" + window.location.pathname.split('/')[1];
			// For running locally on eclipse
		}
	 
	 var path = getRootUrl() + '/resources/images';
	 console.info(path);
	 $(".five-star-rating").raty({
		 score: function() {
			return $(this).attr('data-score'); 
		 },
		 readOnly: true,
		 path: path,
		 hints: ['Horrible', 'Bad', 'Average', 'Good', 'Gorgeous']
	 });
	 
});