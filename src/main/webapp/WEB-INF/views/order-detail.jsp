<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="Order #${order.id} - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold">Order #${order.id}</h2>
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Back to Orders
        </a>
    </div>

    <div class="row g-4">
        <!-- Order Items -->
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="bi bi-box-seam"></i> Order Items
                </div>
                <div class="card-body p-0">
                    <table class="table align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Product</th>
                                <th class="text-center">Qty</th>
                                <th class="text-end">Unit Price</th>
                                <th class="text-end">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td class="fw-semibold">${item.productName}</td>
                                    <td class="text-center">${item.quantity}</td>
                                    <td class="text-end">
                                        $<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/>
                                    </td>
                                    <td class="text-end fw-semibold text-success">
                                        $<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot class="table-light">
                            <tr>
                                <td colspan="3" class="text-end fw-bold">Total</td>
                                <td class="text-end fw-bold text-success fs-5">
                                    $<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>

        <!-- Order Info -->
        <div class="col-lg-4">
            <div class="card shadow-sm mb-3">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="bi bi-info-circle"></i> Order Details
                </div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-5">Order ID</dt>
                        <dd class="col-7">#${order.id}</dd>

                        <dt class="col-5">Date</dt>
                        <dd class="col-7">
                            <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, HH:mm"/>
                        </dd>

                        <dt class="col-5">Status</dt>
                        <dd class="col-7">
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
                                <c:otherwise>
                                    <span class="badge bg-secondary">${order.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </dd>

                        <dt class="col-5">Ship To</dt>
                        <dd class="col-7">${order.shippingAddress}</dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
