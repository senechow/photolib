<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<c:if test="${! empty photoList}">
	<c:forEach items="${photoList}" var="photo">
		<div class="box-drop-shadow  item">
			<div class="row col-lg-4">
				<img
					src="${pageContext.request.contextPath}/photo/${photo.pid}/image"
					class="img-responsive img-rounded image-clip">
				<c:choose>
					<c:when test="${fn:length(photo.name) > 12}">
						<c:set var="shortenedPhotoName"
							value="${fn:substring(photo.name,0,12)}..." />
						<h4>${shortenedPhotoName}</h4>
					</c:when>
					<c:otherwise>
						<h4>${photo.name}</h4>
					</c:otherwise>
				</c:choose>
				<form:form
					action="${pageContext.request.contextPath}/photo/${photo.pid}"
					method="get" commandName="photo">
					<input type="submit" value="Details" class="btn btn-success">
				</form:form>
			</div>
		</div>
	</c:forEach>
</c:if>