<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'ShopEasy - E-Commerce'}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold fs-4" href="${pageContext.request.contextPath}/">
            <i class="bi bi-shop"></i> ShopEasy
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <form class="d-flex mx-auto w-50" action="${pageContext.request.contextPath}/products" method="get">
                <input class="form-control me-2" type="search" name="search"
                       placeholder="Search products..." value="${searchKeyword}">
                <button class="btn btn-warning" type="submit">
                    <i class="bi bi-search"></i>
                </button>
            </form>
            <ul class="navbar-nav ms-auto align-items-center">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">
                        <i class="bi bi-grid"></i> Products
                    </a>
                </li>
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/cart">
                                <i class="bi bi-cart3"></i> Cart
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/orders">
                                <i class="bi bi-bag"></i> Orders
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle"></i> ${sessionScope.userName}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <c:if test="${sessionScope.userRole == 'ADMIN'}">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">
                                        <i class="bi bi-speedometer2"></i> Admin Panel
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth?action=logout">
                                    <i class="bi bi-box-arrow-right"></i> Logout
                                </a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=login">
                                <i class="bi bi-box-arrow-in-right"></i> Login
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-warning btn-sm ms-2"
                               href="${pageContext.request.contextPath}/auth?action=register">
                                Register
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
