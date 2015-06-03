$(document).ready(function() {
	$('#loginForm').validate({
		rules: {
			j_username: {
				required: true,
				email: true
			},
			j_password: "required"
		},
		messages: {
			j_username: {
				required: "Email address is a required field",
				email: "Email must be of format sample@gmail.com"
			},
			j_password: "Password is a required field"
		},
		 submitHandler: function(form) {
	           form.submit();
	    }
	});
	
});