<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Flag Album</title>
</head>
<body>

	<h1 style="text-align: center">Flag Album</h1>

	<div class="col-lg-5 col-md-7 col-sm-9 col-xs-11 center">

		<c:import url="/WEB-INF/views/shared/errors.jsp" />

		<div class="row">
			<p>* means a required field</p>
		</div>

		<form:form role="form" method="POST" commandName="flag"
			action="${pageContext.request.contextPath}/album/${album.aid}/confirmflag"
			cssClass="flagForm" id="flagAlbumForm">

			<c:import url="/WEB-INF/views/flags/_flag_form.jsp" />

		</form:form>

	</div>


</body>
</html>