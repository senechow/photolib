<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Sign Up</title>
</head>
<body>

	<h1 style="text-align: center">User Account Creation</h1>

	<h3 style="text-align: center">Please create your account</h3>

	<div class="col-lg-5 col-md-7 col-sm-9 col-xs-11 center">

		<c:import url="/WEB-INF/views/shared/errors.jsp" />

		<div class="row">
			<p>* means a required field</p>
		</div>

		<form:form id="signUpForm" role="form" method="POST" commandName="user"
			action="${pageContext.request.contextPath}/users/new/create">

			<div class="row">
				<div class="row">
					<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<form:label class="control-label" path="firstName">First Name:</form:label>
							<form:input id="signUpfirstName" path="firstName" class="form-control input-md" />
							<form:errors path="firstName" cssClass="error" />
						</div>
					</div>
					<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<form:label path="lastName">Last Name:</form:label>
							<form:input id="signUpLastName" path="lastName" class="form-control input-md" />
							<form:errors path="lastName" cssClass="error" />
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<t:input path="userName" label="User Name:" required="true" id="signUpUserName"/>
			</div>


			<div class="row">
				<t:input path="emailAddress" label="Email Address:" required="true" id="signUpEmailAddress"/>
			</div>

			<div class="row">
				<t:input-password path="password" label="Password:" required="true" id="signUpPassword"/>
			</div>


			<div class="row">
				<t:input-password path="passwordConfirm" label="Confirm Password"
					required="true" id="signUpPasswordConfirm"/>
			</div>
			
			<input type="hidden" name="passwordOld" value=" ">


			<div class="form-group">
				<div class="row">
					<input type="submit" value="Enter" class="btn btn-lg btn-primary">
				</div>
			</div>

		</form:form>
	</div>
</body>
</html>