<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Albums</title>
</head>
<body>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty userAlbumTitle}">
					<h1>
						<spring:message code="${userAlbumTitle}" />
					</h1>
				</c:when>
				<c:otherwise>
					<h1>
						<spring:message code="label.title.album" />
					</h1>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<c:import url="/WEB-INF/views/shared/messages.jsp" />

		<div class="row">
			<form:form action="${pageContext.request.contextPath}/album/new"
				method="get">
				<input type="submit" value="Create New Album"
					class="btn btn-lg btn-primary">
			</form:form>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
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
	</div>
	
	<div class="col-lg-11 col-md-9 col-sm-6 col-xs-9 center">

		<c:if test="${!empty albumList}">
			<c:forEach items="${albumList}" var="album">
				<div class="col-lg-4 col-md-6 col-sm-12 col-xs-12">
					<div class="row">
						<div class="box-drop-shadow ">
						
<!-- 							<img  -->
<%-- 								src="${pageContext.request.contextPath}/album/${album.aid}/image" --%>
<!-- 								 class="img-responsive img-rounded image-clip"> -->
							<c:choose>
								<c:when test="${fn:length(album.name) > 12}">
									<c:set var="shortenedAlbumName"
										value="${fn:substring(album.name,0,12)}..." />
									<h4>${shortenedAlbumName}</h4>
								</c:when>
								<c:otherwise>
									<h4>${album.name}</h4>
								</c:otherwise>
							</c:choose>
							<form:form
								action="${pageContext.request.contextPath}/album/${album.aid}"
								method="get" commandName="album">
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