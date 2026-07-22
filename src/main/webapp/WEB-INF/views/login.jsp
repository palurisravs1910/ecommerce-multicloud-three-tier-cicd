<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Login - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0"><i class="bi bi-person-circle"></i> Login</h4>
                </div>
                <div class="card-body p-4">

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible">
                            <i class="bi bi-exclamation-triangle"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success">
                            <i class="bi bi-check-circle"></i> ${success}
                        </div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/auth" novalidate>
                        <input type="hidden" name="action" value="login">

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Email Address</label>
                            <input type="email" name="email" class="form-control"
                                   placeholder="you@example.com" required autofocus>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold">Password</label>
                            <input type="password" name="password" class="form-control"
                                   placeholder="Enter your password" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-dark btn-lg">
                                <i class="bi bi-box-arrow-in-right"></i> Login
                            </button>
                        </div>
                    </form>

                    <hr>
                    <p class="text-center mb-0">
                        Don't have an account?
                        <a href="${pageContext.request.contextPath}/auth?action=register" class="fw-semibold">
                            Register here
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
