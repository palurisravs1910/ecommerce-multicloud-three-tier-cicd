<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="My Orders - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <h2 class="fw-bold mb-4"><i class="bi bi-bag-check"></i> My Orders</h2>

    <c:if test="${paymentSuccess == 'true'}">
        <div class="alert alert-success alert-dismissible">
            <i class="bi bi-check-circle-fill"></i>
            Payment successful! Your order has been confirmed.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${paymentFailed == 'true'}">
        <div class="alert alert-danger alert-dismissible">
            <i class="bi bi-x-circle-fill"></i>
            Payment failed. Please try again from order details.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="text-center py-5">
                <i class="bi bi-bag-x display-1 text-muted"></i>
                <p class="mt-3 fs-5 text-muted">You haven't placed any orders yet.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-dark px-5">
                    Shop Now
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-dark">
                            <tr>
                                <th>Order #</th>
                                <th>Date</th>
                                <th>Items</th>
                                <th class="text-end">Total</th>
                                <th class="text-center">Status</th>
                                <th class="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td class="fw-semibold">#${order.id}</td>
                                    <td>
                                        <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy"/>
                                    </td>
                                    <td class="text-muted">
                                        ${order.items.size()} item(s)
                                    </td>
                                    <td class="text-end fw-semibold text-success">
                                        $<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${order.status == 'CONFIRMED'}">
                                                <span class="badge bg-success">${order.status}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'PENDING'}">
                                                <span class="badge bg-warning text-dark">${order.status}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'SHIPPED'}">
                                                <span class="badge bg-info">${order.status}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'DELIVERED'}">
                                                <span class="badge bg-primary">${order.status}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'CANCELLED'}">
                                                <span class="badge bg-danger">${order.status}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${order.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/orders?orderId=${order.id}"
                                           class="btn btn-sm btn-outline-dark">
                                            <i class="bi bi-eye"></i> View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="common/footer.jsp" %>
