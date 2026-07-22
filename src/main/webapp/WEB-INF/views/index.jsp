<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="ShopEasy - Home" />
<%@ include file="common/header.jsp" %>

<!-- Hero Banner -->
<section class="hero-banner text-white text-center py-5"
         style="background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);">
    <div class="container py-4">
        <h1 class="display-4 fw-bold mb-3">Welcome to ShopEasy</h1>
        <p class="lead mb-4">Discover thousands of products at unbeatable prices.</p>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-warning btn-lg px-5">
            <i class="bi bi-grid-3x3-gap"></i> Shop Now
        </a>
    </div>
</section>

<!-- Categories -->
<section class="py-5 bg-light">
    <div class="container">
        <h2 class="text-center fw-bold mb-4">Shop by Category</h2>
        <div class="row row-cols-2 row-cols-md-5 g-3">
            <c:forEach var="cat" items="${categories}">
                <div class="col text-center">
                    <a href="${pageContext.request.contextPath}/products?category=${cat.id}"
                       class="text-decoration-none">
                        <div class="card border-0 shadow-sm h-100 p-3 category-card">
                            <div class="display-5 mb-2">
                                <c:choose>
                                    <c:when test="${cat.name == 'Electronics'}"><i class="bi bi-laptop text-primary"></i></c:when>
                                    <c:when test="${cat.name == 'Clothing'}"><i class="bi bi-bag-heart text-danger"></i></c:when>
                                    <c:when test="${cat.name == 'Books'}"><i class="bi bi-book text-success"></i></c:when>
                                    <c:when test="${cat.name == 'Home & Kitchen'}"><i class="bi bi-house text-warning"></i></c:when>
                                    <c:when test="${cat.name == 'Sports'}"><i class="bi bi-trophy text-info"></i></c:when>
                                    <c:otherwise><i class="bi bi-tag text-secondary"></i></c:otherwise>
                                </c:choose>
                            </div>
                            <p class="fw-semibold text-dark mb-0">${cat.name}</p>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Featured Products -->
<section class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold">Featured Products</h2>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-dark">
                View All <i class="bi bi-arrow-right"></i>
            </a>
        </div>
        <div class="row row-cols-1 row-cols-md-4 g-4">
            <c:forEach var="product" items="${featuredProducts}">
                <div class="col">
                    <div class="card h-100 shadow-sm product-card">
                        <img src="${not empty product.imageUrl ? pageContext.request.contextPath.concat(product.imageUrl) : pageContext.request.contextPath.concat('/images/placeholder.jpg')}"
                             class="card-img-top product-img" alt="${product.name}"
                             onerror="this.src='${pageContext.request.contextPath}/images/placeholder.jpg'">
                        <div class="card-body d-flex flex-column">
                            <p class="text-muted small mb-1">${product.categoryName}</p>
                            <h6 class="card-title fw-semibold">${product.name}</h6>
                            <div class="mt-auto">
                                <p class="fw-bold text-success fs-5 mb-2">
                                    $<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                                </p>
                                <c:choose>
                                    <c:when test="${product.inStock}">
                                        <div class="d-grid gap-2">
                                            <a href="${pageContext.request.contextPath}/products?action=detail&id=${product.id}"
                                               class="btn btn-outline-dark btn-sm">View Details</a>
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
                                        <span class="badge bg-danger">Out of Stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<%@ include file="common/footer.jsp" %>
