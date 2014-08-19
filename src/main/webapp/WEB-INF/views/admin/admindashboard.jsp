<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Dashboard</title>
</head>
<body>

	<div class="col-lg-7 col-md-8 col-sm-9 col-xs-11 center">

		<div class="row">
			<h2>Admin Dashboard</h2>
		</div>

		<c:import url="/WEB-INF/views/shared/messages.jsp" />

		<ul id="adminTabs" class="nav nav-tabs" role="tablist">
			<li class="active"><a href="#home" role="tab" data-toggle="tab">Most
					Recently Flagged</a></li>
			<li><a id="Flagged-Photo-Tabs" href="#flaggedphotos" role="tab" data-toggle="tab">Flagged
					Photos</a></li>
			<li><a id="Flagged-Album-Tabs" href="#flaggedalbums" role="tab" data-toggle="tab">Flagged
					Albums</a></li>
			<li><a id="Flagged-User-Tabs" href="#flaggedusers" role="tab" data-toggle="tab">Flagged
					Users</a></li>
			<li><a id="Banned-User-Tabs" href="#bannedusers" role="tab" data-toggle="tab">Banned
					Users</a></li>
		</ul>

		<div id="adminTabContent" class="tab-content">
			<div class="tab-pane fade in active" id="home">
				<c:import url="/WEB-INF/views/admin/mostrecentflags.jsp" />
			</div>
			<div class="tab-pane fade" id="flaggedphotos">
				<c:import url="/WEB-INF/views/admin/flaggedphotos.jsp" />
			</div>
			<div class="tab-pane active fade" id="flaggedalbums">
				<c:import url="/WEB-INF/views/admin/flaggedalbums.jsp" />
			</div>
			<div class="tab-pane active fade" id="flaggedusers">
				<c:import url="/WEB-INF/views/admin/flaggedusers.jsp" />
			</div>
			<div class="tab-pane active fade" id="bannedusers">
				<c:import url="/WEB-INF/views/admin/bannedusers.jsp" />
			</div>
		</div>

	</div>


</body>
</html>