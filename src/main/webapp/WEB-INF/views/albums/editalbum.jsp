<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Album</title>
</head>
<body>

	<div class="col-lg-7 col-md-8 col-sm-9 col-xs-11 center">

		<div class="row">
			<h2>Edit Album</h2>
		</div>

		<form:form method="post"
			action="${pageContext.request.contextPath}/album/${album.aid}"
			commandName="album">
			<form:errors path="*" cssClass="error" />

			<c:import url="/WEB-INF/views/albums/_album_form.jsp" />

		</form:form>



		<form:form
			action="${pageContext.request.contextPath}/album/${album.aid}"
			method="get">
			<div class="row">
				<input type="submit" value="Back" class="btn btn-lg btn-primary">
			</div>
		</form:form>
	</div>
</body>
</html>