<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- Static navbar -->
<div class="navbar navbar-inverse" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<c:url value="/"/>">Photo Library</a>
		</div>
		
		<div class="navbar-collapse collapse">
		
		<!--  
			<ul class="nav navbar-nav">
				<li><a href="/PhotoLibrary">Home</a></li>
				<li><a href="<c:url value="/photo.html"/>">View Photos</a></li>
			</ul>
		-->

			<ul class="nav navbar-nav navbar-right">

				<sec:authorize var="isLoggedIn" access="isAuthenticated()" />

				<c:choose>
					<c:when test="${isLoggedIn}">
						<sec:authentication property="principal.id" var="accountId" />
						<sec:authentication property="principal.emailAddress" var="accountEmailAddress" />
						
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">${accountEmailAddress}<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="<c:url value="/users/${accountId}" />">My
										Account</a></li>
								<li><a href="<c:url value="/users/${accountId}/photos"/>">My Photos</a></li>
								<li><a href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
								<!--  
								<li class="divider"></li>
								<li class="dropdown-header">Nav header</li>
								<li><a href="#">Separated link</a></li>
								<li><a href="#">One more separated link</a></li>
								-->
							</ul></li>
					</c:when>
					<c:otherwise>
						<li><a href="<c:url value="/login"/>">Sign In</a></li>
						<li><a href="<c:url value="/users/new"/>">Sign Up</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
	<!--/.container-fluid -->
</div>