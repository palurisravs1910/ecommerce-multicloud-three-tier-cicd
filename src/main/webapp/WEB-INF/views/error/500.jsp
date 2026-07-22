<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="500 - Server Error" />
<%@ include file="../common/header.jsp" %>
<div class="container text-center py-5">
    <h1 class="display-1 text-muted">500</h1>
    <h3 class="mb-3">Internal Server Error</h3>
    <p class="text-muted">Something went wrong on our end. Please try again later.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-dark px-5">Go Home</a>
</div>
<%@ include file="../common/footer.jsp" %>
