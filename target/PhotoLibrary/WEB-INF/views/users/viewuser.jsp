<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View User</title>
</head>
<body>


	<h2 style="text-align: center">My Account</h2>

	<div class="col-lg-2 col-md-3 col-sm-4 col-xs-10 center">

		<div class="row">
			<h4>
				First Name:<small> <sec:authentication
						property="principal.firstName" />
				</small>
			</h4>
		</div>

		<div class="row">
			<h4>
				Last Name: <small> <sec:authentication
						property="principal.lastName" />
				</small>
			</h4>
		</div>

		<div class="row">
			<h4>
				EmailAddress: <small> <sec:authentication
						property="principal.emailAddress" />
				</small>
			</h4>
		</div>

	</div>


</body>
</html>