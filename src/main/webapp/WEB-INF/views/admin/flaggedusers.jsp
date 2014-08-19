<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="flagged-users-container">
	<c:import url="/WEB-INF/views/admin/_flaggeduserstable.jsp" />

	<c:if test="${! empty userFlags}">
		<div class="my-navigation">
			<div class="simple-pagination-first"></div>
			<div class="simple-pagination-previous"></div>
			<div class="simple-pagination-page-numbers"></div>
			<div class="simple-pagination-next"></div>
			<div class="simple-pagination-last"></div>
		</div>
	</c:if>
</div>