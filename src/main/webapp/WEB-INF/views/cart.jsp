<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="Cart - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <h2 class="fw-bold mb-4"><i class="bi bi-cart3"></i> Shopping Cart</h2>

    <c:if test="${not empty sessionScope.cartError}">
        <div class="alert alert-warning">
            <i class="bi bi-exclamation-triangle"></i> ${sessionScope.cartError}
        </div>
        <c:remove var="cartError" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty cartItems}">
            <div class="text-center py-5">
                <i class="bi bi-cart-x display-1 text-muted"></i>
                <p class="mt-3 fs-5 text-muted">Your cart is empty.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-dark px-5">
                    Start Shopping
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <!-- Cart Items -->
                <div class="col-lg-8">
                    <div class="card shadow-sm">
                        <div class="card-body p-0">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Product</th>
                                        <th class="text-center">Price</th>
                                        <th class="text-center">Quantity</th>
                                        <th class="text-center">Subtotal</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center gap-3">
                                                    <img src="${not empty item.productImageUrl ? pageContext.request.contextPath.concat(item.productImageUrl) : pageContext.request.contextPath.concat('/images/placeholder.jpg')}"
                                                         width="60" height="60" class="rounded"
                                                         style="object-fit:cover;" alt="${item.productName}"
                                                         onerror="this.src='${pageContext.request.contextPath}/images/placeholder.jpg'">
                                                    <span class="fw-semibold">${item.productName}</span>
                                                </div>
                                            </td>
                                            <td class="text-center">
                                                $<fmt:formatNumber value="${item.productPrice}" pattern="#,##0.00"/>
                                            </td>
                                            <td class="text-center" style="width:130px;">
                                                <form method="post" action="${pageContext.request.contextPath}/cart"
                                                      class="d-flex gap-1 justify-content-center">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="cartItemId" value="${item.id}">
                                                    <input type="number" name="quantity" value="${item.quantity}"
                                                           min="1" max="99" class="form-control form-control-sm text-center"
                                                           style="width:60px;"
                                                           onchange="this.form.submit()">
                                                </form>
                                            </td>
                                            <td class="text-center fw-semibold text-success">
                                                $<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                            </td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/cart">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="cartItemId" value="${item.id}">
                                                    <button class="btn btn-sm btn-outline-danger border-0">
                                                        <i class="bi bi-trash3"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Order Summary -->
                <div class="col-lg-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-dark text-white fw-bold">
                            Order Summary
                        </div>
                        <div class="card-body">
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
                            <div class="d-flex justify-content-between fw-bold fs-5">
                                <span>Total</span>
                                <span class="text-success">
                                    $<fmt:formatNumber value="${cartTotal >= 50 ? cartTotal : cartTotal + 5.99}" pattern="#,##0.00"/>
                                </span>
                            </div>
                            <div class="d-grid mt-4">
                                <a href="${pageContext.request.contextPath}/checkout"
                                   class="btn btn-success btn-lg">
                                    <i class="bi bi-lock-fill"></i> Proceed to Checkout
                                </a>
                            </div>
                            <div class="d-grid mt-2">
                                <a href="${pageContext.request.contextPath}/products"
                                   class="btn btn-outline-secondary">
                                    <i class="bi bi-arrow-left"></i> Continue Shopping
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="common/footer.jsp" %>
