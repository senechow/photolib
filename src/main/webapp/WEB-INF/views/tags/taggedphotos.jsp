<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tagged Photos</title>
</head>
<body>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<h3>Photos tagged with ${tag.name}</h3>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:if test="${empty tag.photos}">
				<p>
					<spring:message code="label.emptytag" />
				</p>
			</c:if>
		</div>
	</div>

	<div class="col-lg-11 col-md-9 col-sm-6 col-xs-9 center">

		<c:if test="${!empty tag.photos}">
			<c:forEach items="${tag.photos}" var="photo">
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