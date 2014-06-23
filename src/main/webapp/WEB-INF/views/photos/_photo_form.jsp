<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:import url="/WEB-INF/views/shared/errors.jsp" />

<!--  
<div class="form-group">
	<div class="row">
		<form:label path="imageFile"> Image: </form:label>
		<input type="file" name="imageFile" id="imageFile">
		<form:errors path="imageFile" cssClass="error" />
	</div>
</div>
-->

<div class="row">
	<p>* means a required field</p>
</div>

<div class="row">
	<t:input-file path="imageFile" label="Image: " required="true" />

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
		<label>Visibility: *</label>
		<div class="radio">
			<form:label path="isPublic">Public</form:label>
			<form:radiobutton path="isPublic" value="true" />
			<form:errors path="isPublic" cssClass="error" />
		</div>

		<div class="radio">

			<form:label path="isPublic"> Private</form:label>
			<form:radiobutton path="isPublic" value="false" />
			<form:errors path="isPublic" cssClass="error" />
		</div>
	</div>
</div>

<div class="form-group">
	<div class="row col-lg-2 col-md-2 col-sm-3">
		<div class="row">
			<input type="submit" value="Enter" class="btn btn-lg btn-success">
		</div>
	</div>
</div>