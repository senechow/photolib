<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View User</title>
</head>
<body>
	<c:choose>
		<c:when test="${! empty myAccountTitle}">
			<h2 style="text-align: center">
				<spring:message code="${myAccountTitle}" />
			</h2>
		</c:when>
		<c:otherwise>
			<h2 style="text-align: center">
				<spring:message code="label.title.otherusersaccount" />
			</h2>
		</c:otherwise>
	</c:choose>

	<div class="col-lg-2 col-md-3 col-sm-4 col-xs-10 center">
		<c:import url="/WEB-INF/views/shared/messages.jsp" />
	</div>

	<c:if test="${! empty errors}">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<div class="bg-danger">
				<h4>Error!</h4>
				<c:forEach items="${errors}" var="error">
					<ul>
						<li class="bg-danger"><spring:message code="${error}" /></li>
					</ul>
				</c:forEach>
			</div>
		</div>
	</c:if>

	<div class="col-lg-2 col-md-3 col-sm-4 col-xs-10 center">

		<div class="row">
			<h4>
				User Name:<small> ${user.userName} </small>
			</h4>
		</div>

		<div class="row">
			<h4>
				First Name:<small> ${user.firstName} </small>
			</h4>
		</div>

		<div class="row">
			<h4>
				Last Name: <small> ${user.lastName} </small>
			</h4>
		</div>

		<div class="row">
			<h4>
				EmailAddress: <small> ${user.emailAddress} </small>
			</h4>
		</div>

		<div class="row">

			<sec:authorize var="isLoggedIn" access="isAuthenticated()" />
			<c:if test="${isLoggedIn}">
				<sec:authentication property="principal.id" var="accountId" />
				<sec:authentication property="principal.role.role" var="accountRole" />
				<c:set var="admin_role" value="admin" />
				<c:choose>
					<c:when test="${accountRole eq admin_role}">
						<form:form
							action="${pageContext.request.contextPath}/users/${user.uid}/edit"
							method="get" commandName="user">
							<input type="submit" value="Edit User Info"
								class="btn btn-lg btn-success" style="margin: 8px 0px 8px 0px;">
						</form:form>
					</c:when>
					<c:when test="${accountId == user.uid}">
						<form:form
							action="${pageContext.request.contextPath}/users/${user.uid}/edit"
							method="get" commandName="user">
							<input type="submit" value="Edit User Info"
								class="btn btn-lg btn-success" style="margin: 8px 0px 8px 0px;">
						</form:form>

						<form:form
							action="${pageContext.request.contextPath}/users/${user.uid}/changepassword"
							method="get" commandName="user">
							<input type="submit" value="Change Password"
								class="btn btn-lg btn-success" style="margin: 8px 0px 8px 0px;">
						</form:form>

					</c:when>
					<c:otherwise>
						<form:form
							action="${pageContext.request.contextPath}/users/${user.uid}/flag"
							method="get" commandName="flag">
							<input type="submit" value="Flag User"
								class="btn btn-lg btn-danger" style="margin: 8px 0px 8px 0px;">
						</form:form>
					</c:otherwise>
				</c:choose>
			</c:if>

		</div>

	</div>


</body>
</html>