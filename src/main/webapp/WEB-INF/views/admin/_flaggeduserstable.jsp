<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${! empty userFlags}">
		<c:set var="usercount" value="1" scope="page" />
		<div class="table-responsive">
			<table class="table">
				<tr>
					<th>#</th>
					<th>User Name</th>
					<th>Reason</th>
					<th>Description</th>
					<th>Link</th>
					<th>Ban</th>
					<th>Delete Flag</th>
				</tr>

				<c:forEach items="${userFlags}" var="userFlag">
					<tr>
						<td>${usercount}</td>
						<td>${userFlag.user.emailAddress}</td>
						<td>${userFlag.reason}</td>
						<c:choose>
							<c:when test="${empty userFlag.description}">
								<td>No description available.</td>
							</c:when>
							<c:otherwise>
								<td>${userFlag.description}</td>
							</c:otherwise>
						</c:choose>
						<td><form:form
								action="${pageContext.request.contextPath}/users/${userFlag.user.uid}"
								method="get" commandName="user">
								<input type="submit" value="Details" class="btn btn-success">
							</form:form></td>
						<td><form:form
								action="${pageContext.request.contextPath}/admin/${userFlag.user.uid}/banuser"
								method="post" commandName="user">
								<input type="submit" value="Ban" class="btn btn-warning">
							</form:form></td>
						<td><form:form
								action="${pageContext.request.contextPath}/admin/flag/${userFlag.fid}/delete"
								method="delete" commandName="userFlag">
								<input type="submit" value="Delete" class="btn btn-danger">
							</form:form></td>
					</tr>
					<c:set var="usercount" value="${usercount + 1}" scope="page" />
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:otherwise>

		<p>There are currently no users that have been flagged.</p>

	</c:otherwise>
</c:choose>