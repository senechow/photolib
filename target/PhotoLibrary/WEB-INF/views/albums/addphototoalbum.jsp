<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Photos to Album</title>
</head>
<body>

	<div class="col-lg-7 col-md-8 col-sm-9 col-xs-11 center">

		<div class="row">
			<h2>Add Photos to Album</h2>
		</div>

		<form:form
			action="${pageContext.request.contextPath}/album/${album.aid}"
			method="get" commandName="album">

			<div class="form-group">
				<div class="row">
					<input type="submit" value="Back" class="btn btn-lg btn-primary">
				</div>
			</div>
		</form:form>

		<form:form method="post"
			action="${pageContext.request.contextPath}/album/${album.aid}/addphoto/update"
			commandName="album">

			<div class="form-group">
				<div class="row">
					<input type="submit" value="Add" class="btn btn-lg btn-success">
				</div>
			</div>

			<c:import url="/WEB-INF/views/shared/errors.jsp" />

			<div class="col-lg-12 col-md-12 col-sm-11 col-xs-10 center">

				<c:forEach items="${photoList}" var="photo" varStatus="status">
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="row">
							<div class="box-drop-shadow ">
								<t:checkbox value="${photo.pid}" path="photos"
									collection="${album.photos}"></t:checkbox>
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
							</div>
						</div>
					</div>
				</c:forEach>
			</div>


			<%-- <form:checkboxes path="photos" items="${photoList}" itemValue="pid" cssClass="row" element="div" /> --%>
		</form:form>
	</div>
</body>
</html>