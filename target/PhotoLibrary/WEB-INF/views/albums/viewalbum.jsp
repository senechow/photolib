<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Album</title>
</head>
<body>

	<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
		<c:import url="/WEB-INF/views/shared/messages.jsp" />
	</div>

	<div class="row">
		<h2 style="text-align: center">${album.name}</h2>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<h4>Created by: <a href="<c:url value="/users/${album.user.uid}"/>">${album.user.userName}</a></h4>
			<h4>
				Last updated:
				<fmt:formatDate pattern="yyyy/MM/dd" value="${album.lastUpdateDate}" />
			</h4>
			<h4>
				Date created:
				<fmt:formatDate pattern="yyyy/MM/dd" value="${album.creationDate}" />
			</h4>
		</div>
	</div>


	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<h3>Description:</h3>
			<p>${album.description}</p>
		</div>
	</div>

	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<sec:authorize var="isLoggedIn" access="isAuthenticated()" />
			<c:if test="${isLoggedIn}">
				<sec:authentication property="principal.id" var="accountId" />
				<sec:authentication property="principal.role.role" var="accountRole" />
				<c:set var="admin_role" value="admin" />
				<c:choose>
					<c:when
						test="${accountId == album.user.uid || accountRole eq admin_role}">
						<div class="row">
							<form:form
								action="${pageContext.request.contextPath}/album/${album.aid}/edit"
								method="get" commandName="album">
								<input type="submit" value="Edit Info"
									class="btn btn-lg btn-success" style="margin: 8px 0px 8px 0px;">
							</form:form>

							<form:form
								action="${pageContext.request.contextPath}/album/${album.aid}/addphoto"
								method="get" commandName="album">
								<input type="submit" value="Add or Remove Photos"
									class="btn btn-lg btn-success" style="margin: 8px 0px 8px 0px;">
							</form:form>

							<form:form
								action="${pageContext.request.contextPath}/album/${album.aid}"
								method="delete" commandName="album">

								<button type=button value="delete" class="btn btn-lg btn-danger"
									style="margin: 8px 0px 8px 0px;" data-toggle="modal"
									data-target="#confirmDelete" data-title="Delete Album"
									data-message="Are you sure you want to delete this album?">
									Delete</button>

								<c:import url="/WEB-INF/views/shared/delete_confirm.jsp" />
							</form:form>
						</div>
					</c:when>
					<c:otherwise>
						<form:form
							action="${pageContext.request.contextPath}/album/${album.aid}/flag"
							method="get" commandName="flag">
							<input type="submit" value="Flag Album" class="btn btn-lg btn-danger"
								style="margin: 8px 0px 8px 0px;">
						</form:form>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</div>

	<div class="col-lg-6 col-md-9 col-sm-6 col-xs-9 center">
		<h3>Photos:</h3>
		<c:if test="${empty photoList}">
			<spring:message code="label.emptyalbum" />
		</c:if>
		<c:if test="${!empty photoList}">
			<c:forEach items="${photoList}" var="photo">
				<div class="col-lg-4 col-md-6 col-sm-12 col-xs-12">
					<div class="row">
						<div class="box-drop-shadow ">
							<img
								src="${pageContext.request.contextPath}/photo/${photo.pid}/image"
								class="img-responsive img-rounded image-clip">
							<c:choose>
								<c:when test="${fn:length(photo.name) > 12}">
									<c:set var="shortenedPhotoName"
										value="${fn:substring(photo.name,0,12)}..." />
									<h4>${shortenedPhotoName}</h4>
								</c:when>
								<c:otherwise>
									<h4>${photo.name}</h4>
								</c:otherwise>
							</c:choose>
							<form:form
								action="${pageContext.request.contextPath}/photo/${photo.pid}"
								method="get" commandName="photo">
								<input type="submit" value="Details" class="btn btn-success">
							</form:form>
						</div>
					</div>
				</div>

			</c:forEach>
		</c:if>

	</div>

</body>
</html>