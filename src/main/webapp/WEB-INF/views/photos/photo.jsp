<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<body>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty userPhotoTitle}">
					<h1>
						<spring:message code="${userPhotoTitle}" />
					</h1>
				</c:when>
				<c:otherwise>
					<h1>
						<spring:message code="label.title.photo" />
					</h1>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<c:import url="/WEB-INF/views/shared/messages.jsp" />

		<div class="row">
			<c:url var="newPhotoURL" value="/photo/new"/>
			<form:form action="${pageContext.request.contextPath}/photo/new" method="get">
				<input type="submit" value="Create New Photo"
					class="btn btn-lg btn-primary">
			</form:form>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:if test="${empty photoList}">
				<c:choose>
					<c:when test="${!empty userPhotoEmptyMsg}">
						<p>
							<spring:message code="${userPhotoEmptyMsg}" />
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
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">

		<c:if test="${!empty photoList}">
			<c:forEach items="${photoList}" var="photo">
				<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12">
					<div class="row">
						<div class="box-drop-shadow">
							<img
								src="${pageContext.request.contextPath}/photo/${photo.pid}/image"
								 class="img-responsive img-rounded" >
							<c:choose>
								<c:when test="${fn:length(photo.name) > 20}">
									<c:set var="shortenedPhotoName"
										value="${fn:substring(photo.name,0,20)}..." />
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