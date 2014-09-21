<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Login</title>
</head>
<body>

	<h1 style="text-align: center">Welcome! Please sign in.</h1>


	<div class="col-lg-5 col-md-7 col-sm-9 col-xs-11 center">

		<c:import url="/WEB-INF/views/shared/messages.jsp" />

		<c:if test="${hasError == true}">
			<div class="row bg-danger">
				<h4>The following error is preventing the form from being
					submitted:</h4>
				<ul>
					<li><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" /></li>
				</ul>
			</div>
		</c:if>

		<form class="form-signin" method="post"
			action="<c:url value='j_spring_security_check'/>">

			<div class="form-group">
				<div class="row">
					<label>Email Address:</label> <input type="text"
						class="form-control input-md" name="j_username" />
				</div>
			</div>

			<div class="form-group">
				<div class="row">

					<label>Password:</label> <input type="password"
						class="form-control" name="j_password" />

				</div>
			</div>

			<div class="form-group">
				<div class="row">
					<input type="submit" value="Login" class="btn btn-lg btn-primary">
				</div>
			</div>

		</form>
<!--  
		<div class="row">
			<a href="<c:url value="/forgotpassword"/>">Forgot your password?</a>
		</div>
-->
		<div class="row">
			<a href="<c:url value="/users/new"/>">Don't have an account?
				Create one.</a>
		</div>
	</div>
</body>
</html>