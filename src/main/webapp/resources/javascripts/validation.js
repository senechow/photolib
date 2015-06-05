$(document).ready(function() {
	
	var userNameRequiredMsg = "User name is a required field";
	var emailAddressRequiredMsg = "Email address is a required field";
	var emailAddressIncorrectFormatMsg = "Email must be of format sample@gmail.com";
	var passwordRequiredMsg = "Password is a required field";
	var passwordTooShortMSg = "Password must be at least 6 characters long";
	var confirmPasswordRequiredMsg = "Password confirm is a required field";
	var confirmPasswordNotSameAsPasswordMsg  = "Password confirm must be the same as the password entered above";
	var confirmPasswordTooShortMsg = "Password confirm must be at least 6 characters long";
	var nameRequiredMsg = "Name is a required field";
	var nameTooLongMsg = "Name cannot be greater than 50 characters";
	var descriptionTooLongMsg = "Description cannot be greater than 255 characters";
	
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
				required: emailAddressRequiredMsg,
				email: emailAddressIncorrectFormatMsg
			},
			j_password: passwordRequiredMsg
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
			userName: userNameRequiredMsg,
			emailAddress: {
				required: emailAddressRequiredMsg,
				email: emailAddressIncorrectFormatMsg
			},
			password: {
				required: passwordRequiredMsg,
				minlength: passwordTooShortMSg
			},
			passwordConfirm: {
				required: confirmPasswordRequiredMsg,
				equalTo: confirmPasswordNotSameAsPasswordMsg,
				minlength: confirmPasswordTooShortMsg
			}
		},
		submitHandler: function (form){
			form.submit();
		}
	});
	
	$('.photoForm').validate({
		rules: {
			imageFile: "required",
			name: {
				required: true,
				maxlength: 50
			},
			description: {
				maxlength: 255
			},
			isPublic: "required"
		},
		messages: {
			imageFile: "Image is a required field",
			name: {
				required: nameRequiredMsg,
				maxlength: nameTooLongMsg
			},
			description: {
				maxlength: descriptionTooLongMsg
			},
			isPublic: "Please select a privacy option"
		},
		submitHandler: function (form){
			form.submit();
		}
	});
	
	$('.albumForm').validate({
		rules: {
			name: {
				required: true,
				maxlength: 50
			},
			description: {
				maxlength: 255
			},
			isPublic: true
		},
		messages: {
			name: {
				required: nameRequiredMsg,
				maxlength: nameTooLongMsg
			},
			description: {
				maxlength: descriptionTooLongMsg
			},
			isPublic: "Please select a privacy option"
		},
		submitHandler: function (form) {
			form.submit();
		}
	});
	
	$('#confirmEmailForm').validate({
		rules: {
			confirmationCode: "required"
		},
		messages: {
			confirmationCode: "Confirmation code is a required field"
		},
		submitHandler: function (form) {
			form.submit();
		}
	});
	
	$('.flagForm').validate({
		rules: {
			reason: "required",
			description: {
				maxlength: 255
			}
		},
		messages: {
			reason: "Please select a reason for flagging",
			description: {
				maxlength: descriptionTooLongMsg
			},
		},
		submitHanlder: function(form) {
			form.submit();
		}
	});
	
	$('#changePasswordForm').validate({
		rules: {
			passwordOld: {
				required: true,
				minlength: 6
			},
			password: {
				required: true,
				minlength: 6
			},
			passwordConfirm: {
				required: true,
				minlength: 6,
				equalTo: "#changePasswordNewPassword"
			}
		},
		messages: {
			passwordOld: {
				required: "Old password is a required field",
				minlength: "Old password must be at least 6 characters long"
			},
			password: {
				required: passwordRequiredMsg,
				minlength: passwordTooShortMSg
			},
			passwordConfirm: {
				required: confirmPasswordRequiredMsg,
				minlength: confirmPasswordTooShortMsg,
				equalTo: confirmPasswordNotSameAsPasswordMsg
			}
		},
		submitHandler: function(form) {
			form.submit();
		}
	});
	
	$('#editUserForm').validate({
		rules: {
			userName: "required",
			emailAddress: {
				required: true,
				email: true
			}
		},
		messages: {
			userName: userNameRequiredMsg,
			emailAddress: {
				required: emailAddressRequiredMsg,
				email: emailAddressIncorrectFormatMsg
			}
		},
		submitHandler: function(form) {
			form.submit();
		}
	});
	
});