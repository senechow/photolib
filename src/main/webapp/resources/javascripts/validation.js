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
	
	$('#signUpForm').validate({
		rules: {
			userName: "required",
			emailAddress: {
				required: true,
				email: true
			},
			password: {
				required: true,
				minlength: 6
			},
			passwordConfirm: {
				required: true,
				equalTo: "#signUpPassword",
				minlength: 6
			}
		},
		messages: {
			userName: "User name is a required field",
			emailAddress: {
				required: "Email Address is a required field",
				email: "Email Address must of the of the format sample@gmail.com"
			},
			password: {
				required: "Password is a required field",
				minlength: "Password must be at least 6 characters long"
			},
			passwordConfirm: {
				required: "Password confirm is a required field",
				equalTo: "Password confirm must be the same as the password entered above",
				minlength: "Password confirm must be at least 6 characters long"
			}
		},
		submitHandler: function (form){
			form.submit();
		}
	});
	
});