<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="Checkout - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <h2 class="fw-bold mb-4"><i class="bi bi-bag-check"></i> Checkout</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-triangle"></i> ${error}
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/checkout">
        <div class="row g-4">

            <!-- Shipping Details -->
            <div class="col-lg-7">
                <div class="card shadow-sm">
                    <div class="card-header bg-dark text-white fw-bold">
                        <i class="bi bi-geo-alt"></i> Shipping Information
                    </div>
                    <div class="card-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Full Name</label>
                            <input type="text" class="form-control" value="${user.name}" readonly>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Email</label>
                            <input type="email" class="form-control" value="${user.email}" readonly>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Phone</label>
                            <input type="tel" class="form-control" value="${user.phone}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Shipping Address <span class="text-danger">*</span></label>
                            <textarea name="shippingAddress" class="form-control" rows="4"
                                      placeholder="Enter your complete delivery address"
                                      required>${user.address}</textarea>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Order Summary -->
            <div class="col-lg-5">
                <div class="card shadow-sm">
                    <div class="card-header bg-dark text-white fw-bold">
                        <i class="bi bi-receipt"></i> Order Summary
                    </div>
                    <div class="card-body">
                        <c:forEach var="item" items="${cartItems}">
                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">${item.productName} x ${item.quantity}</span>
                                <span>$<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                            </div>
                        </c:forEach>
                        <hr>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Subtotal</span>
                            <span>$<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Shipping</span>
                            <span class="text-success">
                                <c:choose>
                                    <c:when test="${cartTotal >= 50}">Free</c:when>
                                    <c:otherwise>$5.99</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between fw-bold fs-5 mb-4">
                            <span>Total</span>
                            <span class="text-success">
                                $<fmt:formatNumber value="${cartTotal >= 50 ? cartTotal : cartTotal + 5.99}" pattern="#,##0.00"/>
                            </span>
                        </div>

                        <div class="d-flex gap-2 text-muted small mb-3 justify-content-center">
                            <i class="bi bi-credit-card"></i>
                            <i class="bi bi-paypal"></i>
                            <span>Secured by Stripe</span>
                        </div>

                        <div class="d-grid">
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="bi bi-shield-lock"></i> Place Order & Pay
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
