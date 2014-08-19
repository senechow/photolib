<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div id="banned-users-container">
	<c:choose>
		<c:when test="${! empty bannedUsersFlags}">
			<c:set var="bannedUserCount" value="1" scope="page" />
			<div class="table-responsive">
				<table class="table">
					<tr>
						<th>#</th>
						<th>User Name</th>
						<th>Reason</th>
						<th>Description</th>
						<th>Link</th>
						<th>Unban</th>
					</tr>

					<c:forEach items="${bannedUsersFlags}" var="bannedUsersFlag">
						<tr>
							<td>${bannedUsercount}</td>
							<td>${bannedUsersFlag.user.emailAddress}</td>
							<td>${bannedUsersFlag.reason}</td>
							<c:choose>
								<c:when test="${empty bannedUsersFlag.description}">
									<td>No description available.</td>
								</c:when>
								<c:otherwise>
									<td>${bannedUsersFlag.description}</td>
								</c:otherwise>
							</c:choose>
							<td><form:form
									action="${pageContext.request.contextPath}/users/${bannedUsersFlag.user.uid}"
									method="get" commandName="user">
									<input type="submit" value="Details" class="btn btn-success">
								</form:form></td>
							<td><form:form
									action="${pageContext.request.contextPath}/admin/${bannedUsersFlag.user.uid}/unbanuser"
									method="post" commandName="user">
									<input type="submit" value="Unban" class="btn btn-warning">
								</form:form></td>
						</tr>
						<c:set var="bannedUserCount" value="${bannedUserCount + 1}"
							scope="page" />
					</c:forEach>
				</table>
			</div>
		</c:when>
		<c:otherwise>
			<p>There are currently no users that have been banned.</p>
		</c:otherwise>
	</c:choose>

	<c:if test="${! empty bannedUsersFlags}">
		<div class="my-navigation">
			<div class="simple-pagination-first"></div>
			<div class="simple-pagination-previous"></div>
			<div class="simple-pagination-page-numbers"></div>
			<div class="simple-pagination-next"></div>
			<div class="simple-pagination-last"></div>
		</div>
	</c:if>
</div>