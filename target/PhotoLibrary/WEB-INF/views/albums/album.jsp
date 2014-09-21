<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Albums</title>
</head>
<body>

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty userAlbumTitle}">
					<h1>
						<spring:message code="${userAlbumTitle}" />
					</h1>
				</c:when>
				<c:when test="${! empty user}">
					<h1>${user.userName}'s Albums</h1>
				</c:when>
				<c:otherwise>
					<h1>
						<spring:message code="label.title.album" />
					</h1>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	
	

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<c:import url="/WEB-INF/views/shared/messages.jsp" />

	
			<form:form action="${pageContext.request.contextPath}/album/new"
				method="get">
				<input type="submit" value="Create New Album"
					class="btn btn-lg btn-primary">
			</form:form>
		</div>
	

	<div class="col-lg-11 col-md-10 col-sm-9 col-xs-9 center">
		<div class="row">
			<c:choose>
				<c:when test="${! empty user}">
					<c:url var="userAlbumSortUrl"
						value="/users/${user.uid}/albums/sort" />
					<form:form method="get" action="${userAlbumSortUrl}"
						commandName="albumSearch">
						<div class="col-lg-2 col-md-3 col-sm-4 form-group">
							<form:label path="sortType">Sort By: </form:label>
							<div class="select-style">
								<form:select id="select" path="sortType"
									items="${sortingSelections}" onchange="this.form.submit()">
								</form:select>
							</div>
						</div>
					</form:form>
				</c:when>
				<c:otherwise>
					<c:url var="albumSortUrl" value="/album/sort" />
					<form:form method="get" action="${albumSortUrl}"
						commandName="albumSearch">
						<div class="col-lg-2 col-md-3 col-sm-4 form-group">
							<form:label path="sortType">Sort By: </form:label>
							<div class="select-style">
								<form:select path="sortType"
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
		
			<c:if test="${empty albumList}">
				<c:choose>
					<c:when test="${!empty emptyAlbumMsg}">
						<p>
							<spring:message code="${emptyAlbumMsg}" />
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<spring:message code="label.emptypublicalbum" />
						</p>
					</c:otherwise>
				</c:choose>
			</c:if>
		
	</div>

	<div class="col-lg-11 col-md-9 col-sm-6 col-xs-9 center">
		<div id="masonry-container">
			<c:import url="/WEB-INF/views/albums/_album.jsp"></c:import>
		</div>
	</div>

	<script type="text/javascript">
	
	var rootUrl = "${pageContext.request.contextPath}";
	
	(function(){
		
		var  loading = false,
			onUsersAlbumPage = "${user}";
	
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
				if(onUsersAlbumPage) {
					var userId = "${user.uid}";
					location = rootUrl + "/users/" + userId + "/morealbums?sortType=${albumSearch.sortType}";
				}
				else {
					location = rootUrl + "/morealbums?sortType=${albumSearch.sortType}";
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