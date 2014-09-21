<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${!empty albumList}">
	<c:forEach items="${albumList}" var="album">
		<div class="box-drop-shadow item">
			<form:form
				action="${pageContext.request.contextPath}/album/${album.aid}"
				method="get" commandName="album">
				<c:choose>
					<c:when test="${album.albumSize > 0}">
						<input type="image"
							src="${pageContext.request.contextPath}/album/${album.aid}/image"
							class="img-responsive img-rounded image-clip">

					</c:when>
					<c:otherwise>
						<input type="image"
							src="${pageContext.request.contextPath}/resources/images/no-image-available.bmp"
							class="img-responsive img-rounded image-clip">
					</c:otherwise>
				</c:choose>
			</form:form>
			<c:choose>
				<c:when test="${fn:length(album.name) > 20}">
					<c:set var="shortenedAlbumName"
						value="${fn:substring(album.name,0,20)}..." />
					<h4>
						<a href="<c:url value="/album/${album.aid}"/>">${shortenedAlbumName}</a>
					</h4>
				</c:when>
				<c:otherwise>
					<h4>
						<a href="<c:url value="/album/${album.aid}"/>">${album.name}</a>
					</h4>
				</c:otherwise>
			</c:choose>

			<h5>Views: ${album.viewCount}</h5>
			<h5># of Photos: ${album.albumSize}</h5>
			
			<div class="center col-lg-7 col-md-7 col-sm-8 col-xs-12">
				<div class="five-star-rating" data-score="${album.rating.rating}"></div>
			</div>

		</div>
	</c:forEach>
	
</c:if>

<script src="<c:url value= "/resources/javascripts/rating.js"/>"></script>