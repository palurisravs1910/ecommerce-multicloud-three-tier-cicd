<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="403 - Access Denied" />
<%@ include file="../common/header.jsp" %>
<div class="container text-center py-5">
    <h1 class="display-1 text-muted">403</h1>
    <h3 class="mb-3">Access Denied</h3>
    <p class="text-muted">You don't have permission to view this page.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-dark px-5">Go Home</a>
</div>
<%@ include file="../common/footer.jsp" %>
