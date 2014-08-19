<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="../../assets/ico/favicon.ico">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">

<!-- main css -->

<link
	href="<c:url value="/resources/stylesheets/cerulean-bootstrap.min.css" />"
	rel="stylesheet" />
<link href="<c:url value="/resources/stylesheets/main.css" />"
	rel="stylesheet" />
<link rel="stylesheet"
	href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" />
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/flick/jquery-ui.css">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/stylesheets/jquery.tagit.css" />" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script
	src="<c:url value= "/resources/javascripts/masonry.pkgd.min.js"/>"></script>
<script
	src="<c:url value= "/resources/javascripts/jquery.infinitescroll.min.js"/>"></script>
<script
	src="<c:url value= "/resources/javascripts/imagesloaded.pkgd.min.js"/>"></script>
<script src="<c:url value= "/resources/javascripts/jquery.sausage.js"/>"></script>
<title>Photo Library</title>
</head>
<body>
	<div id="wrap">
		<c:import url="/WEB-INF/views/layout/navbar.jsp" />

		<div class="container-fluid">

			<decorator:body />
		</div>
	</div>


	<!-- JQuery -->

	<!-- Latest compiled and minified JavaScript -->
	<script
		src="<c:url value= "/resources/javascripts/jquery-simple-pagination-plugin.js"/>"></script>
	<script
		src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script src="<c:url value= "/resources/javascripts/tag-it.js"/>"></script>
	<script src="<c:url value= "/resources/javascripts/autocomplete.js"/>"></script>

	<script src="<c:url value= "/resources/javascripts/datepicker.js"/>"></script>
	<script
		src="<c:url value= "/resources/javascripts/confirm-delete-modal.js"/>"></script>
	<script src="<c:url value= "/resources/javascripts/tabs.js"/>"></script>


</body>
</html>