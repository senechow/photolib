<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Photo</title>
</head>
<body>



	<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
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

	<div class="row">
		<h2 style="text-align: center">${photo.name}</h2>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<img
				src="${pageContext.request.contextPath}/photo/${photo.pid}/image"
				class="img-responsive center">
		</div>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<h4>
				Created by: <a href="<c:url value="/users/${photo.user.uid}"/>">${photo.user.userName}</a>
			</h4>
			<h4>
				Date created:
				<fmt:formatDate pattern="yyyy/MM/dd" value="${photo.creationDate}" />
			</h4>
		</div>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<h4>
				Rating:
				<c:choose>
					<c:when test="${photo.rating.numRatings == 0 }">
						0 / 5 (Based on ${photo.rating.numRatings} ratings)
					</c:when>
					<c:otherwise>
						<fmt:formatNumber
							value=" ${photo.rating.rating / photo.rating.numRatings}"
							pattern="0.00" /> / 5 (Based on ${photo.rating.numRatings} ratings)
				</c:otherwise>
				</c:choose>
			</h4>
		</div>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<sec:authorize var="isLoggedIn" access="isAuthenticated()" />
			<c:if test="${isLoggedIn}">
				<sec:authentication property="principal.id" var="accountId" />

				<c:if test="${accountId != photo.user.uid}">
					<h4>Rate the Photo:</h4>
					<c:url var="ratePhotoUrl" value="/photo/${photo.pid}/rate" />
					<form action="${ratePhotoUrl}">
						<select name="rating" onchange="this.form.submit()">
							<option selected="selected">Select a value</option>
							<option value="5">5</option>
							<option value="4">4</option>
							<option value="3">3</option>
							<option value="2">2</option>
							<option value="1">1</option>
						</select>
					</form>
				</c:if>
			</c:if>
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
			<h3>Tags:</h3>
			<c:choose>
				<c:when test="${empty photo.tags}">
					<spring:message code="label.photonotags" />
				</c:when>
				<c:otherwise>
					<c:forEach items="${photo.tags}" var="tag">
						<a href="<c:url value="/tags/${tag.tid}"/>" class="tag-box">${tag.name}</a>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<sec:authorize var="isLoggedIn" access="isAuthenticated()" />
			<c:if test="${isLoggedIn}">
				<sec:authentication property="principal.id" var="accountId" />
				<sec:authentication property="principal.role.role" var="accountRole" />
				<c:set var="admin_role" value="admin"/>
				<c:choose>
					<c:when test="${accountId == photo.user.uid || accountRole eq admin_role}">
						<form:form
							action="${pageContext.request.contextPath}/photo/${photo.pid}/edit"
							method="get" commandName="photo">
							<input type="submit" value="Edit" class="btn btn-lg btn-success"
								style="margin: 8px 0px 8px 0px;">
						</form:form>

						<form:form
							action="${pageContext.request.contextPath}/photo/${photo.pid}"
							method="delete" commandName="photo">

							<button type=button value="Delete" class="btn btn-lg btn-danger"
								style="margin: 8px 0px 8px 0px;" data-toggle="modal"
								data-target="#confirmDelete" data-title="Delete Photo"
								data-message="Are you sure you want to delete this photo?">
								Delete</button>

							<c:import url="/WEB-INF/views/shared/delete_confirm.jsp" />
						</form:form>

					</c:when>
					<c:otherwise>
						<form:form
							action="${pageContext.request.contextPath}/photo/${photo.pid}/flag"
							method="get" commandName="flag">
							<input type="submit" value="Flag Photo" class="btn btn-lg btn-danger"
								style="margin: 8px 0px 8px 0px;">
						</form:form>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</div>


</body>
</html>