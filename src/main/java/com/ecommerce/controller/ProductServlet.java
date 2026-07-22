package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action     = req.getParameter("action");
        String categoryId = req.getParameter("category");
        String keyword    = req.getParameter("search");
        String productId  = req.getParameter("id");

        if ("detail".equals(action) && productId != null) {
            // Product detail page
            Product product = productService.getProductById(Integer.parseInt(productId));
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            req.setAttribute("product", product);
            req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, resp);

        } else {
            // Product listing
            List<Product> products;
            if (keyword != null && !keyword.trim().isEmpty()) {
                products = productService.searchProducts(keyword);
                req.setAttribute("searchKeyword", keyword);
            } else if (categoryId != null && !categoryId.trim().isEmpty()) {
                products = productService.getProductsByCategory(Integer.parseInt(categoryId));
                req.setAttribute("selectedCategory", Integer.parseInt(categoryId));
            } else {
                products = productService.getAllProducts();
            }
            req.setAttribute("products", products);
            req.setAttribute("categories", productService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
        }
    }
}
