<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Confirm Email</title>
</head>
<body>

	<h1 style="text-align: center">User Email Confirmation</h1>

	<h4 style="text-align: center">Please enter in your confirmation
		code.</h4>

	<div class="col-lg-5 col-md-7 col-sm-9 col-xs-11 center">
		<c:import url="/WEB-INF/views/shared/errors.jsp" />

		<div class="row">
			<p>* means a required field</p>
		</div>

		<form:form role="form" method="POST" commandName="user"
			action="${pageContext.request.contextPath}/users/${userid}/confirmemail/confirm">
			<div class="row">
				<t:input path="confirmationCode" label="Confirmation Code:"
					required="true"></t:input>
			</div>

			<div class="form-group">
				<div class="row">
					<input type="submit" value="Enter" class="btn btn-lg btn-success">
				</div>
			</div>
		</form:form>
	</div>

</body>
</html>