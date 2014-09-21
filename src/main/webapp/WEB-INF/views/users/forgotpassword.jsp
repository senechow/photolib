<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forgot Password</title>
</head>
<body>

	<h1 style="text-align: center">Forgot Password</h1>

	<h3 style="text-align: center">Please enter in your email address</h3>

	<div class="col-lg-5 col-md-7 col-sm-9 col-xs-11 center">
		<c:import url="/WEB-INF/views/shared/errors.jsp" />
	</div>

	<div class="col-lg-5 col-md-7 col-sm-9 col-xs-11 center">

		<form:form role="form" method="post" commandName="user"
			action="${pageContext.request.contextPath}/forgotpassword/sendemail">
			<div class="form-group">
				<div class="row">
					<t:input path="emailAddress" label="Email Address:" required="true" />
				</div>
			</div>

			<div class="form-group">
				<div class="row">
					<input type="submit" value="Submit" class="btn btn-lg btn-primary">
				</div>
			</div>

		</form:form>

	</div>

</body>
</html>