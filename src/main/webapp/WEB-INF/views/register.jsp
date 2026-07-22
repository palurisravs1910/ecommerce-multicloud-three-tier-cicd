<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Register - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0"><i class="bi bi-person-plus"></i> Create Account</h4>
                </div>
                <div class="card-body p-4">

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible">
                            <i class="bi bi-exclamation-triangle"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/auth" novalidate>
                        <input type="hidden" name="action" value="register">

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Full Name</label>
                            <input type="text" name="name" class="form-control"
                                   placeholder="John Doe" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Email Address</label>
                            <input type="email" name="email" class="form-control"
                                   placeholder="you@example.com" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Password
                                <small class="text-muted">(min. 6 characters)</small>
                            </label>
                            <input type="password" name="password" class="form-control"
                                   placeholder="Create a password" required minlength="6">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Phone Number</label>
                            <input type="tel" name="phone" class="form-control"
                                   placeholder="+1 234 567 8900">
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold">Delivery Address</label>
                            <textarea name="address" class="form-control" rows="3"
                                      placeholder="Street, City, State, ZIP"></textarea>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-dark btn-lg">
                                <i class="bi bi-person-check"></i> Create Account
                            </button>
                        </div>
                    </form>

                    <hr>
                    <p class="text-center mb-0">
                        Already have an account?
                        <a href="${pageContext.request.contextPath}/auth?action=login" class="fw-semibold">
                            Login here
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
