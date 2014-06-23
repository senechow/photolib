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
<title>View Photo</title>
</head>
<body>

	<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
		<c:import url="/WEB-INF/views/shared/messages.jsp" />
	</div>

		<div class="row">
			<h2 style="text-align: center">${photo.name}</h2>
		</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 image center">
			<img
				src="${pageContext.request.contextPath}/photo/${photo.pid}/image"
				class="img-responsive">
		</div>
	</div>


	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">

			<h3>Description:</h3>
			<p>${photo.description}</p>
		</div>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<sec:authorize var="isLoggedIn" access="isAuthenticated()" />
			<c:if test="${isLoggedIn}">
				<sec:authentication property="principal.id" var="accountId" />

				<c:if test="${accountId == photo.user.uid}">
					<form:form action="${pageContext.request.contextPath}/photo/${photo.pid}/edit"
						method="get" commandName="photo">
						<input type="submit" value="Edit" class="btn btn-lg btn-success"
							style="margin: 8px 0px 8px 0px;">
					</form:form>

					<form:form action="${pageContext.request.contextPath}/photo/${photo.pid}"
						method="delete" commandName="photo">

						<button type=button value="delete" class="btn btn-lg btn-danger"
							style="margin: 8px 0px 8px 0px;" data-toggle="modal"
							data-target="#confirmDelete" data-title="Delete Photo"
							data-message="Are you sure you want to delete this photo?">
							Delete</button>

						<c:import url="/WEB-INF/views/shared/delete_confirm.jsp" />
					</form:form>

				</c:if>
			</c:if>
		</div>
	</div>


</body>
</html>