<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${! empty photoFlags}">
		<c:set var="photocount" value="1" scope="page" />
		<div class="table-responsive">
			<table class="table">
				<tr>
					<th>#</th>
					<th>Photo Name</th>
					<th>Reason</th>
					<th>Description</th>
					<th>Link</th>
					<th>Delete Flag</th>
				</tr>

				<c:forEach items="${photoFlags}" var="photoFlag">
					<tr>
						<td>${photocount}</td>
						<td>${photoFlag.photo.name}</td>
						<td>${photoFlag.reason}</td>
						<c:choose>
							<c:when test="${empty photoFlag.description}">
								<td>No description available.</td>
							</c:when>
							<c:otherwise>
								<td>${photoFlag.description}</td>
							</c:otherwise>
						</c:choose>
						<td><form:form
								action="${pageContext.request.contextPath}/photo/${photoFlag.photo.pid}"
								method="get" commandName="photo">
								<input type="submit" value="Details" class="btn btn-success">
							</form:form></td>
						<td><form:form
								action="${pageContext.request.contextPath}/admin/flag/${photoFlag.fid}/delete"
								method="delete" commandName="photoFlag">
								<input type="submit" value="Delete" class="btn btn-danger">
							</form:form></td>
					</tr>
					<c:set var="photocount" value="${photocount + 1}" scope="page" />
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:otherwise>

		<p>There are currently no photos that have been flagged.</p>

	</c:otherwise>
</c:choose>