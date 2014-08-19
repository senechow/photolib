<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- Static navbar -->
<div class="bs-component">
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

			<!--  
			<ul class="nav navbar-nav">
				<li><a href="/PhotoLibrary">Home</a></li>
				<li><a href="<c:url value="/photo.html"/>">View Photos</a></li>
			</ul>
			</div>
		-->
			<div class="collapse navbar-collapse navbar-responsive-collapse">
				<ul class="nav navbar-nav navbar-left">
					<li><div class="col-sm-12 col-md-12 col-lg-12">
							<div class="row">
								<c:url var="searchUrl" value="/search" />
								<form class="navbar-form" role="search" action="${searchUrl}">
									<div class="form-group">
										<input type="text" class="form-control" placeholder="Search"
											name="searchQuery" id="basic-search">
										<button class="btn btn-default" type="submit">
											<span class="glyphicon glyphicon-search"></span>
										</button>
									</div>
								</form>
							</div>
							<a href="<c:url value="/advancedsearch"/>">Advanced Search</a>
						</div></li>

				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="<c:url value="/"/>">Photos</a></li>
					<li><a href="<c:url value="/album"/>">Albums</a></li>
					<li><sec:authorize var="isLoggedIn" access="isAuthenticated()" />
						<c:choose>
							<c:when test="${isLoggedIn}">
								<sec:authentication property="principal.id" var="accountId" />
								<sec:authentication property="principal.userName"
									var="accountUserName" />
									
								<sec:authorize access="hasRole('ROLE_ADMIN')"> 
										<li><a href="<c:url value="/admin/dashboard"/>">Admin Dashboard</a></li>
										<li class="dropdown"><a href="#" class="dropdown-toggle"
											data-toggle="dropdown">${accountUserName}<b class="caret"></b></a>
											<ul class="dropdown-menu">
												<li><a href="<c:url value="/users/${accountId}" />">My
														Account</a></li>
												<li><a
													href="<c:url value="/users/${accountId}/photos"/>">My
														Photos</a></li>
												<li><a
													href="<c:url value="/users/${accountId}/albums"/>">My
														Albums</a></li>
												<li><a
													href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
												<!--  
												<li class="divider"></li>
												<li class="dropdown-header">Nav header</li>
												<li><a href="#">Separated link</a></li>
												<li><a href="#">One more separated link</a></li>
												-->
											</ul></li>
									</sec:authorize>
									<sec:authorize access="hasRole('ROLE_REGISTERED_USER')"> 
										<li class="dropdown"><a href="#" class="dropdown-toggle"
											data-toggle="dropdown">${accountUserName}<b class="caret"></b></a>
											<ul class="dropdown-menu">
												<li><a href="<c:url value="/users/${accountId}" />">My
														Account</a></li>
												<li><a
													href="<c:url value="/users/${accountId}/photos"/>">My
														Photos</a></li>
												<li><a
													href="<c:url value="/users/${accountId}/albums"/>">My
														Albums</a></li>
												<li><a
													href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
												<!--  
												<li class="divider"></li>
												<li class="dropdown-header">Nav header</li>
												<li><a href="#">Separated link</a></li>
												<li><a href="#">One more separated link</a></li>
												-->
											</ul></li>
									</sec:authorize>
							</c:when>
							<c:otherwise>
								<li><a href="<c:url value="/login"/>">Sign In</a></li>
								<li><a href="<c:url value="/users/new"/>">Sign Up</a></li>
							</c:otherwise>
						</c:choose></li>
				</ul>
			</div>


			<!--/.nav-collapse -->
		</div>
	</div>
	<!--/.container-fluid -->
</div>