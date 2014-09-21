<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change Password</title>
</head>
<body>

	<h1 style="text-align: center">Change Password</h1>

	<div class="col-lg-5 col-md-7 col-sm-9 col-xs-11 center">

		<c:import url="/WEB-INF/views/shared/errors.jsp" />

		<div class="row">
			<p>* means a required field</p>
		</div>

		<sec:authentication property="principal.id" var="accountId" />

		<form:form role="form" method="POST" commandName="user"
			action="${pageContext.request.contextPath}/users/${accountId}/updatepassword">


			<div class="row">
				
 				<t:input-password path="passwordOld" label="Old Password:" 
  					required="true" />
			</div>


			<div class="row">
				<t:input-password path="password" label="New Password:" required="true" />
			</div>

			<div class="row">
				<t:input-password path="passwordConfirm" label="Confirm New Password:" required="true" />
			</div>
			
			<input type="hidden" name="emailAddress" value="${user.emailAddress}">
			
			<div class="form-group">
				<div class="row">
					<input type="submit" value="Enter" class="btn btn-lg btn-primary">
				</div>
			</div>
		</form:form>

		
		<form:form
			action="${pageContext.request.contextPath}/users/${accountId}"
			method="get">
			<div class="row">
				<input type="submit" value="Back" class="btn btn-lg btn-success">
			</div>
		</form:form>
	</div>

</body>
</html>