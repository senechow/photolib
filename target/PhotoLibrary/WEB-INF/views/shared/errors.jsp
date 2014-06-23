<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${! empty errors}">
	<div class="bg-danger">
		<h4>The following errors are preventing the form from being
			submitted:</h4>
		<c:forEach items="${errors}" var="error">
			<ul>
				<li class="bg-danger"><spring:message code="${error}" /></li>
			</ul>
		</c:forEach>
	</div>
</c:if>
    