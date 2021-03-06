<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:import url="/WEB-INF/views/shared/errors.jsp" />

<div class="row">
	<p>* means a required field</p>
</div>


<div class="row">
	<div class="row">
		<div class="col-lg-5 col-md-7 col-sm-8 col-xs-10">
			<t:input path="name" label="Name: " required="true"></t:input>
		</div>
	</div>
</div>

<div class="form-group">
	<div class="row">
		<form:label path="description" cssStyle="display:inline">Description:</form:label>
		<form:textarea path="description" class="form-control" rows="8" />
		<form:errors path="description" cssClass="error" />
	</div>
</div>



<div class="form-group">
	<div class="row">
		<label class="control-label">Privacy Settings: *</label>
	</div>
	<div class="radio">
		<form:radiobutton path="isPublic" value="true" label="Public" />
		<form:errors path="isPublic" cssClass="error" />
	</div>
	<div class="radio">
		<form:radiobutton path="isPublic" value="false" label="Private" />
		<form:errors path="isPublic" cssClass="error" />
	</div>
</div>


<div class="form-group">
	<div class="row col-lg-2 col-md-2 col-sm-3">
		<div class="row">
			<input type="submit" value="Enter" class="btn btn-lg btn-primary">
		</div>
	</div>
</div>