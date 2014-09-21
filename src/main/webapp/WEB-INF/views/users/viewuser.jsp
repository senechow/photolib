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
			<h1 style="text-align: center">
				<spring:message code="${myAccountTitle}" />
			</h1>
		</c:when>
		<c:otherwise>
			<h1 style="text-align: center">${user.userName}'s Account</h1>
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
	
	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">

		<div class="row">
			<h2>Account Details</h2>
		</div>
	</div>

	<div class="box-drop-shadow-text col-lg-11 col-md-10 col-sm-9 col-xs-9 center">

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
							class="btn btn-primary" style="margin: 8px 0px 8px 0px;">
					</form:form>
				</c:when>
				<c:when test="${accountId == user.uid}">
					<form:form
						action="${pageContext.request.contextPath}/users/${user.uid}/edit"
						method="get" commandName="user">
						<input type="submit" value="Edit User Info"
							class="btn btn-primary" style="margin: 8px 0px 8px 0px;">
					</form:form>

					<form:form
						action="${pageContext.request.contextPath}/users/${user.uid}/changepassword"
						method="get" commandName="user">
						<input type="submit" value="Change Password"
							class="btn btn-success" style="margin: 8px 0px 8px 0px;">
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



		<h4>
			User Name:<small> ${user.userName} </small>
		</h4>



		<h4>
			First Name:<small> ${user.firstName} </small>
		</h4>

		<h4>
			Last Name: <small> ${user.lastName} </small>
		</h4>

		<h4>
			Email Address: <small> ${user.emailAddress} </small>
		</h4>


	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty userMostRecentPhotoTitle}">
					<h2>
						<spring:message code="${userMostRecentPhotoTitle}" />
					</h2>
				</c:when>
				<c:otherwise>
					<h2>${user.userName}'s Most Recent Photos</h2>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="col-lg-10 col-md-9 col-sm-8 col-xs-8 center">
		<c:if test="${empty photoList}">
			<c:choose>
				<c:when test="${!empty emptyPhotoMsg}">
					<p>
						<spring:message code="${emptyPhotoMsg}" />
					</p>
				</c:when>
				<c:otherwise>
					<p>
						<spring:message code="label.emptypublicphoto" />
					</p>
				</c:otherwise>
			</c:choose>
		</c:if>
	</div>


	<div class="col-lg-10 col-md-9 col-sm-8 col-xs-8 center">
		<div id="photo-masonry-container">
			<c:import url="/WEB-INF/views/photos/_photo.jsp"></c:import>
		</div>
		<form:form action="${pageContext.request.contextPath}/users/${user.uid}/photos"
			method="get" commandName="user">
			<input type="submit" value="View All Photos"
				class="btn btn-info" style="margin: 8px 0px 8px 0px;">
		</form:form>
	</div>
	
	

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty userMostRecentAlbumTitle}">
					<h2>
						<spring:message code="${userMostRecentAlbumTitle}" />
					</h2>
				</c:when>
				<c:otherwise>
					<h2>${user.userName}'s Most Recemt Albums</h2>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="col-lg-10 col-md-9 col-sm-8 col-xs-7 center">
		<c:if test="${empty albumList}">
			<c:choose>
				<c:when test="${!empty emptyAlbumMsg}">
					<p>
						<spring:message code="${emptyAlbumMsg}" />
					</p>
				</c:when>
				<c:otherwise>
					<p>
						<spring:message code="label.emptypublicalbum" />
					</p>
				</c:otherwise>
			</c:choose>
		</c:if>
	</div>

	<div class="col-lg-10 col-md-9 col-sm-8 col-xs-8 center">
		<div id="album-masonry-container">
			<c:import url="/WEB-INF/views/albums/_album.jsp"></c:import>
		</div>
		
		<form:form action="${pageContext.request.contextPath}/users/${user.uid}/albums"
			method="get" commandName="user">
			<input type="submit" value="View All Albums"
				class="btn btn-info" style="margin: 8px 0px 8px 0px;">
		</form:form>
	
	</div>
	
	

	<script type="text/javascript">
	$(document).ready(function() {
		
		var $container = $('#photo-masonry-container');
		var $container2 = $('#album-masonry-container');
		$(".item").imagesLoaded(function() {
			$container.imagesLoaded(function() {
				
				$(".item").addClass('loaded');
				$container.masonry({
					itemSelector : '.item',
					isAnimated : true,
					columnWidth : containerWidth(),
					isFitWidth: true
				});
			$container2.imagesLoaded(function() {
					
				$(".item").addClass('loaded');
				$container2.masonry({
						itemSelector : '.item',
						isAnimated : true,
						columnWidth : containerWidth(),
						isFitWidth: true
					});
					
				});	
			});
		});	

	});
	
	function containerWidth() {
		var width = $(window).width();
    	var col = 200;
    	if(width < 1200 && width >= 980) {
    	  col = 160;
  	  	}
   		else if(width < 980 && width >= 768) {
      	col = 124;
    	}
   		 return col;
	}
</script>

</body>
</html>

