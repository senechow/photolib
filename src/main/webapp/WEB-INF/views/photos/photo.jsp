<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<html>
<body>
	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty userPhotoTitle}">
					<h1>
						<spring:message code="${userPhotoTitle}" />
					</h1>
				</c:when>
				<c:when test="${! empty user}">
					<h1>${user.userName}'s Photos</h1>
				</c:when>
				<c:otherwise>
					<h1>
						<spring:message code="label.title.photo" />
					</h1>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<c:import url="/WEB-INF/views/shared/messages.jsp" />
	</div>
	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">

		<form:form action="${pageContext.request.contextPath}/photo/new"
			method="get">
			<input id="newPhotoBtn" type="submit" value="Create New Photo"
				class="btn btn-lg btn-primary">
		</form:form>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty user}">
					<c:url var="userSortUrl" value="/users/${user.uid}/photos/sort" />
					<form:form method="get" action="${userSortUrl}"
						commandName="photoSearch">
						<div class="col-lg-2 col-md-3 col-sm-4 form-group">
							<form:label path="sortType">Sort By: </form:label>
							<div class="select-style">
								<form:select class="form-control" id="select" path="sortType"
									items="${sortingSelections}" onchange="this.form.submit()">
								</form:select>
							</div>
						</div>
					</form:form>
				</c:when>
				<c:otherwise>
					<c:url var="sortUrl" value="/photo/sort" />
					<form:form method="get" action="${sortUrl}"
						commandName="photoSearch">
						<div class="col-lg-2 col-md-3 col-sm-4 form-group">
							<form:label path="sortType">Sort By: </form:label>
							<div class="select-style">
								<form:select  path="sortType"
									items="${sortingSelections}" onchange="this.form.submit()">
								</form:select>
							</div>
						</div>
					</form:form>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
			<c:if test="${empty photoList}">
				<c:choose>
					<c:when test="${!empty emptyPhotoMsg}">
						<p>
							<spring:message code="${emptyPhotoMsg}" />
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<spring:message code="label.emptypublicphoto" />
						</p>
					</c:otherwise>
				</c:choose>
			</c:if>
	</div>

	<div class="col-lg-12 col-md-10 col-sm-9 col-xs-9 center">
		<div id="masonry-container">
			<c:import url="/WEB-INF/views/photos/_photo.jsp"></c:import>
		</div>
	</div>
	<div class="col-lg-1 col-md-2 col-sm-3 col-xs-3 center">
		<div id="LoadingImage" style="display: none;">
			<img
				src="${pageContext.servletContext.contextPath}/resources/images/loading.gif" />
		</div>
	</div>

	<script type="text/javascript">
	
	var rootUrl = "${pageContext.request.contextPath}";
	
	(function(){
		
		var  loading = false,
		onUsersPhotoPage = "${user}";
	
	function nearBottomOfPage() {
		return $(window).scrollTop() > $(document).height() - $(window).height() - 150;
	}
	
		 $(document).ajaxStart(function () {
			 $("#LoadingImage").show();
	        });

	        $(document).ajaxStop(function () {
	        	$("#LoadingImage").hide();
	        });
	
		$(window).scroll(function() {
			
			if(loading) {
				return;
			}
			
			if(nearBottomOfPage()) {
				loading=true;
				var location;
				if(onUsersPhotoPage) {
					var userId = "${user.uid}";
					location = rootUrl + "/users/" + userId + "/morephotos?sortType=${photoSearch.sortType}";
				}
				else {
					location = rootUrl + "/morephotos?sortType=${photoSearch.sortType}";
				}
				$.ajax({
					url: location,
					type: 'get',
					dataType: 'html',
					success: 
						function(data) {
						if(data.trim()) {
							loading=false;
							var $boxes= data;
							$("#masonry-container").append($boxes).masonry('appended', $boxes);
							$(".item").imagesLoaded(function() {
								$("#masonry-container").masonry('reloadItems');
								$("#masonry-container").masonry('layout');
								$(".item").addClass('loaded');
						});
						}
					}
				});
				
			}
		});
		
	
	}());

	$(document).ready(function() {
		
		var $container = $('#masonry-container');
		$(".item").imagesLoaded(function() {
			$container.imagesLoaded(function() {
				
				$(".item").addClass('loaded');
				$container.masonry({
					itemSelector : '.item',
					isAnimated : true,
					columnWidth : containerWidth(),
					isFitWidth: true
				});
				
			});
		});
	});

	function containerWidth() {
		 var width = $(window).width();
	     var col = 200;
	     if(width < 1200 && width >= 980) {
	       col = 160;
	     }
	     else if(width < 980 && width >= 768) {
	       col = 124;
	     }
	     return col;
	}
	</script>

</body>
</html>