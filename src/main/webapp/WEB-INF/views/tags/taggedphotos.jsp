<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tagged Photos</title>
</head>
<body>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<h3>Photos tagged with ${tag.name}</h3>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:if test="${empty photoList}">
				<p>
					<spring:message code="label.emptytag" />
				</p>
			</c:if>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:url var="sortUrl" value="/tags/${tag.tid}/sort" />
			<form:form method="get" action="${sortUrl}" commandName="photoSearch">
				<div class="col-lg-2 col-md-3 col-sm-4 form-group">
					<form:label path="sortType">Sort By: </form:label>
					<form:select class="form-control" path="sortType"
						items="${sortingSelections}" onchange="this.form.submit()">
					</form:select>
				</div>
			</form:form>
		</div>
	</div>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
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
		
		var  loading = false
	
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
				var tagId = "${tag.tid}";
				var location = rootUrl + "/tags/" + tagId + "/morephotos?sortType=${photoSearch.sortType}";
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

	<!-- 
	<div class="col-lg-11 col-md-9 col-sm-6 col-xs-9 center">
	
		<c:if test="${!empty tag.photos}">
			<c:forEach items="${tag.photos}" var="photo">
				<div class="col-lg-4 col-md-6 col-sm-12 col-xs-12">
					<div class="row">
						<div class="box-drop-shadow ">
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
				</div>
			</c:forEach>
		</c:if>
	</div>
-->

</body>
</html>