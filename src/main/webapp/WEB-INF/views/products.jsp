<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="Products - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <div class="row">

        <!-- Sidebar: Categories -->
        <div class="col-md-3 mb-4">
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="bi bi-funnel"></i> Categories
                </div>
                <div class="list-group list-group-flush">
                    <a href="${pageContext.request.contextPath}/products"
                       class="list-group-item list-group-item-action ${empty selectedCategory ? 'active' : ''}">
                        All Products
                    </a>
                    <c:forEach var="cat" items="${categories}">
                        <a href="${pageContext.request.contextPath}/products?category=${cat.id}"
                           class="list-group-item list-group-item-action ${selectedCategory == cat.id ? 'active' : ''}">
                            ${cat.name}
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Product Grid -->
        <div class="col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="fw-bold mb-0">
                    <c:choose>
                        <c:when test="${not empty searchKeyword}">
                            Results for: "<em>${searchKeyword}</em>"
                        </c:when>
                        <c:otherwise>All Products</c:otherwise>
                    </c:choose>
                </h4>
                <span class="text-muted">${products.size()} item(s) found</span>
            </div>

            <c:choose>
                <c:when test="${empty products}">
                    <div class="text-center py-5">
                        <i class="bi bi-search display-1 text-muted"></i>
                        <p class="mt-3 text-muted fs-5">No products found.</p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-dark">
                            Browse All
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row row-cols-1 row-cols-md-3 g-4">
                        <c:forEach var="product" items="${products}">
                            <div class="col">
                                <div class="card h-100 shadow-sm product-card">
                                    <img src="${not empty product.imageUrl ? pageContext.request.contextPath.concat(product.imageUrl) : pageContext.request.contextPath.concat('/images/placeholder.jpg')}"
                                         class="card-img-top product-img" alt="${product.name}"
                                         onerror="this.src='${pageContext.request.contextPath}/images/placeholder.jpg'">
                                    <div class="card-body d-flex flex-column">
                                        <p class="text-muted small mb-1">${product.categoryName}</p>
                                        <h6 class="card-title fw-semibold">${product.name}</h6>
                                        <p class="card-text text-muted small">${product.description}</p>
                                        <div class="mt-auto">
                                            <p class="fw-bold text-success fs-5 mb-2">
                                                $<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                                            </p>
                                            <c:choose>
                                                <c:when test="${product.inStock}">
                                                    <div class="d-grid gap-2">
                                                        <a href="${pageContext.request.contextPath}/products?action=detail&id=${product.id}"
                                                           class="btn btn-outline-dark btn-sm">
                                                            <i class="bi bi-eye"></i> View Details
                                                        </a>
                                                        <form method="post" action="${pageContext.request.contextPath}/cart">
                                                            <input type="hidden" name="action" value="add">
                                                            <input type="hidden" name="productId" value="${product.id}">
                                                            <input type="hidden" name="quantity" value="1">
                                                            <button class="btn btn-dark btn-sm w-100">
                                                                <i class="bi bi-cart-plus"></i> Add to Cart
                                                            </button>
                                                        </form>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger fs-6">Out of Stock</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
