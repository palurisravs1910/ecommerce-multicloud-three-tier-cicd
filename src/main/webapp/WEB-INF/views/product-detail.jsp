<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="${product.name} - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
            <li class="breadcrumb-item active">${product.name}</li>
        </ol>
    </nav>

    <div class="row g-5">
        <!-- Product Image -->
        <div class="col-md-5">
            <img src="${not empty product.imageUrl ? pageContext.request.contextPath.concat(product.imageUrl) : pageContext.request.contextPath.concat('/images/placeholder.jpg')}"
                 class="img-fluid rounded shadow" alt="${product.name}"
                 onerror="this.src='${pageContext.request.contextPath}/images/placeholder.jpg'"
                 style="width:100%; max-height:450px; object-fit:cover;">
        </div>

        <!-- Product Details -->
        <div class="col-md-7">
            <span class="badge bg-secondary mb-2">${product.categoryName}</span>
            <h2 class="fw-bold">${product.name}</h2>
            <h3 class="text-success fw-bold my-3">
                $<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
            </h3>
            <p class="text-muted">${product.description}</p>

            <c:choose>
                <c:when test="${product.inStock}">
                    <p class="text-success"><i class="bi bi-check-circle-fill"></i>
                        In Stock (${product.stock} available)
                    </p>

                    <c:choose>
                        <c:when test="${not empty sessionScope.loggedUser}">
                            <form method="post" action="${pageContext.request.contextPath}/cart" class="d-flex gap-3 align-items-center mt-3">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.id}">
                                <div style="width:100px;">
                                    <input type="number" name="quantity" value="1" min="1"
                                           max="${product.stock}" class="form-control text-center">
                                </div>
                                <button type="submit" class="btn btn-dark btn-lg px-4">
                                    <i class="bi bi-cart-plus"></i> Add to Cart
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/auth?action=login"
                               class="btn btn-dark btn-lg px-4 mt-3">
                                <i class="bi bi-box-arrow-in-right"></i> Login to Buy
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <p class="text-danger fw-semibold">
                        <i class="bi bi-x-circle-fill"></i> Out of Stock
                    </p>
                </c:otherwise>
            </c:choose>

            <hr class="my-4">
            <div class="d-flex gap-4 text-muted small">
                <span><i class="bi bi-truck"></i> Free shipping over $50</span>
                <span><i class="bi bi-arrow-return-left"></i> 30-day returns</span>
                <span><i class="bi bi-shield-check"></i> Secure checkout</span>
            </div>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
