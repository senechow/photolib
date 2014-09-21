<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<c:if test="${! empty photoList}">
	<c:forEach items="${photoList}" var="photo">
		<div class="box-drop-shadow  item">
			
				<form:form
					action="${pageContext.request.contextPath}/photo/${photo.pid}"
					method="get" commandName="photo">
					<input type="image"
						src="${pageContext.request.contextPath}/photo/${photo.pid}/image"
						class="img-responsive img-rounded image-clip">
				</form:form>

				<c:choose>
					<c:when test="${fn:length(photo.name) > 20}">
						<c:set var="shortenedPhotoName"
							value="${fn:substring(photo.name,0,20)}..." />
						<h4 >
							<a href="<c:url value="/photo/${photo.pid}"/>">${shortenedPhotoName}</a>
						</h4>
					</c:when>
					<c:otherwise>
						<h4>
							<a href="<c:url value="/photo/${photo.pid}"/>">${photo.name}</a>
						</h4>
					</c:otherwise>
				</c:choose>

				<h5>Views: ${photo.viewCount}</h5>

				<div class="center col-lg-7 col-md-7 col-sm-8 col-xs-12">
					<div class="five-star-rating" data-score="${photo.rating.rating}"></div>
				</div>
			</div>
		
	</c:forEach>
</c:if>

<script src="<c:url value= "/resources/javascripts/rating.js"/>"></script>