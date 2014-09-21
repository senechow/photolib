<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
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
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
			<h2>${album.name}</h2>
		</div>
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
		<div
			class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center box-drop-shadow-text">
			<h4>
				Created by: <a href="<c:url value="/users/${album.user.uid}"/>">${album.user.userName}</a>
			</h4>
			<h4>
				Last updated:
				<fmt:formatDate pattern="yyyy/MM/dd" value="${album.lastUpdateDate}" />
			</h4>
			<h4>
				Date created:
				<fmt:formatDate pattern="yyyy/MM/dd" value="${album.creationDate}" />
			</h4>
			<h4>View Count: ${album.viewCount}</h4>
			<h4>
				Rating:
				<c:choose>
					<c:when test="${album.rating.numRatings == 0 }">
						<div class="five-star-rating" data-score="${album.rating.rating}"
							style="display: inline"></div> 
						(Not rated)
					</c:when>
					<c:when test="${album.rating.numRatings == 1 }">
						<div class="five-star-rating" data-score="${album.rating.rating}"
							style="display: inline"></div> 
						(${album.rating.numRatings} rating)
					</c:when>
					<c:otherwise>
						<div class="five-star-rating" data-score="${album.rating.rating}"
							style="display: inline"></div>
						(${album.rating.numRatings} ratings)
				</c:otherwise>
				</c:choose>
			</h4>
		</div>
	</div>
	
	<sec:authorize var="isLoggedIn" access="isAuthenticated()" />
	<c:if test="${isLoggedIn}">
		<sec:authentication property="principal.id" var="accountId" />

		<c:if test="${accountId != album.user.uid}">
			<div class="row">
				<div
					class="box-drop-shadow-text col-lg-6 col-md-7 col-sm-7 col-xs-8 center ">
					<h3>Rate the Album:</h3>
					<c:url var="rateAlbumUrl" value="/album/${album.aid}/rate" />
					<form action="${rateAlbumUrl}">
						<select name="rating" onchange="this.form.submit()">
							<option selected="selected">Select a value</option>
							<option value="5">5</option>
							<option value="4">4</option>
							<option value="3">3</option>
							<option value="2">2</option>
							<option value="1">1</option>
						</select>
					</form>
				</div>
			</div>
		</c:if>
	</c:if>


	<div class="row">
		<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center box-drop-shadow-text">
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
									class="btn btn-lg btn-primary" style="margin: 8px 0px 8px 0px;">
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
							<input type="submit" value="Flag Album"
								class="btn btn-lg btn-danger" style="margin: 8px 0px 8px 0px;">
						</form:form>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</div>

	<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
		<h3>Photos:</h3>
	</div>
	<div class="col-lg-6 col-md-7 col-sm-7 col-xs-8 center">
		<c:choose>
			<c:when test="${empty photoList}">
				<p>
					<spring:message code="label.emptyalbum" />
				</p>
			</c:when>
			<c:otherwise>
				<div id="galleria">
					<c:forEach items="${photoList}" var="photo">
						<img title="Name: ${photo.name}"
							alt="Description: ${photo.description}"
							src="${pageContext.request.contextPath}/photo/${photo.pid}/image" />

					</c:forEach>
				</div>
				<script>
					// Initialize Galleria
					Galleria.run('#galleria');
				</script>
			</c:otherwise>
		</c:choose>
	</div>

</body>
</html>