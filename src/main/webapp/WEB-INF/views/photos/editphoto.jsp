<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Photo</title>
</head>
<body>

	<div class="col-lg-7 col-md-8 col-sm-9 col-xs-11 center">

		<div class="row">
			<h2>Edit Photo</h2>
		</div>

		<form:form method="post" action="${pageContext.request.contextPath}/photo/${photo.pid}"
			commandName="photo" enctype="multipart/form-data" id="editPhotoForm" cssClass="photoForm">
			<form:errors path="*" cssClass="error" />

			<c:import url="/WEB-INF/views/photos/_photo_form.jsp" />

		</form:form>



		<form:form action="${pageContext.request.contextPath}/photo/${photo.pid}" method="get" >
			<div class="row">
				<input type="submit" value="Back" class="btn btn-lg btn-success">
			</div>
		</form:form>


	</div>
</body>
</html>