<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${! empty albumFlags}">
		<c:set var="albumcount" value="1" scope="page" />
		<div class="table-responsive">
			<table class="table">
				<tr>
					<th>#</th>
					<th>Album Name</th>
					<th>Reason</th>
					<th>Description</th>
					<th>Link</th>
					<th>Delete Flag</th>
				</tr>

				<c:forEach items="${albumFlags}" var="albumFlag">
					<tr>
						<td>${albumcount}</td>
						<td>${albumFlag.album.name}</td>
						<td>${albumFlag.reason}</td>
						<c:choose>
							<c:when test="${empty albumFlag.description}">
								<td>No description available.</td>
							</c:when>
							<c:otherwise>
								<td>${albumFlag.description}</td>
							</c:otherwise>
						</c:choose>
						<td><form:form
								action="${pageContext.request.contextPath}/album/${albumFlag.album.aid}"
								method="get" commandName="album">
								<input type="submit" value="Details" class="btn btn-success">
							</form:form></td>
						<td><form:form
								action="${pageContext.request.contextPath}/admin/flag/${albumFlag.fid}/delete"
								method="delete" commandName="albumFlag">
								<input type="submit" value="Delete" class="btn btn-danger">
							</form:form></td>
					</tr>
					<c:set var="albumcount" value="${albumcount + 1}" scope="page" />
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:otherwise>

		<p>There are currently no albums that have been flagged.</p>

	</c:otherwise>
</c:choose>