<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="row">
	<h4>Most Recently Flagged Photos</h4>
	<c:choose>
		<c:when test="${! empty topTenPhotoFlags}">
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

					<c:forEach items="${topTenPhotoFlags}" var="photoFlag">
						<tr id="row_${photocount}">
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
</div>

<div class="row">
	<h4>Most Recently Flagged Albums</h4>
	<c:choose>
		<c:when test="${! empty topTenAlbumFlags}">
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

					<c:forEach items="${topTenAlbumFlags}" var="albumFlag">
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
</div>

<div class="row">
	<h4>Most Recently Flagged Users</h4>
	<c:choose>
		<c:when test="${! empty topTenUsersFlags}">
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
						<th>Delete</th>
					</tr>

					<c:forEach items="${topTenUsersFlags}" var="userFlag">
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
</div>