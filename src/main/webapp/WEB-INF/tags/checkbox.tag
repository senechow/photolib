<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@attribute name="path" required="true" type="java.lang.String"%>
<%@attribute name="value" required="true" type="java.lang.String"%>
<%@attribute name="collection" required="true"
	type="java.util.Collection"%>
<%@attribute name="cssClass" required="false" type="java.lang.String"%>
<%@attribute name="id" required="false" type="java.lang.String"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:set var="objIdString" value="${value}" />
<c:set var="inList" value="false" />
<fmt:parseNumber var="objId" type="number" value="${objIdString}" integerOnly="true" />
<spring:bind path="${path}">
	<div class="form-group ${status.error ? 'has-error' : '' }">
		<label class="control-label" for="${path}">${label} </label>
		<c:forEach items="${collection}" var="item">
			
			<c:if test="${item.pid eq objId}">
				<c:set var="inList" value="true" />
			</c:if>
		</c:forEach>
		<c:choose>
			<c:when test="${inList == false}">
				<input type="checkbox" name="${path}" id="${empty id ? '' : id}"
					class="form-control" value="${value}"/>
			</c:when>
			<c:otherwise>
				<input type="checkbox" name="${path}" id="${empty id ? '' : id}"
					class="form-control" value="${value}" checked="checked"/>
			</c:otherwise>
		</c:choose>

		<c:if test="${status.error}">
			<c:forEach items="${status.errorMessages}" var="error">
				<span class="help-block control-label">${error}</span>
			</c:forEach>
		</c:if>
	</div>

</spring:bind>