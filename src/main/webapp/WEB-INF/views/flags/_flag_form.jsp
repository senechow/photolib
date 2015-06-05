<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<div class="form-group">
	<div class="row">
		<form:label path="reason">Reason: *</form:label>
		<div class="select-style">
			<form:select path="reason" items="${flaggingReasons}" id="flagReason"/>
		</div>
	</div>
</div>

<div class="form-group">
	<div class="row">
		<form:label path="description" cssStyle="display:inline">Description:</form:label>
		<form:textarea path="description" class="form-control" rows="8" id="flagDescription"/>
		<form:errors path="description" cssClass="error" />
	</div>
</div>

<div class="form-group">
	<div class="row">
		<input type="submit" value="Enter" class="btn btn-lg btn-primary">
	</div>
</div>