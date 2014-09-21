<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Advanced Search</title>
</head>
<body>

	<div class="col-lg-7 col-md-8 col-sm-9 col-xs-11 center">

		<div class="row">
			<h2>Advanced Search</h2>
		</div>

		<c:import url="/WEB-INF/views/shared/errors.jsp" />

		<c:url var="advSearchUrl" value="/advancedsearch/search" />
		<form:form method="get" action="${advSearchUrl}"
			commandName="photoSearch">

			<div class="row">
				<t:input path="name" label="Name of photo: " />
			</div>

			<div class="row">
				<t:input path="description"
					label="The photo contains the description: " />
			</div>

			<div class="row">
				<t:input path="createdSince" label="The photo was created before: "
					id="datepicker" />
			</div>

			<div class="row">
				<div class="form-group">
					<form:label path="tags">The photo contains at least one of the following tags: </form:label>
					<form:input path="tags" id="tagsid" class="form-control" />
				</div>
			</div>

			<div class="row">
				<div class="form-group">
					<form:label class="control-label" path="sortType">Sort By: </form:label>
					<div class="select-style">
						<form:select path="sortType"
							items="${sortingSelections}" />
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group">

					<input type="submit" value="Search" class="btn btn-lg btn-primary">
				</div>
			</div>


		</form:form>
	</div>


</body>
</html>