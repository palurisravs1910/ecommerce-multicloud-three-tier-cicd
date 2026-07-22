package com.ecommerce.service;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productDAO.getAllProducts();
        }
        return productDAO.searchProducts(keyword.trim());
    }

    public List<Category> getAllCategories() {
        return productDAO.getAllCategories();
    }

    public boolean addProduct(Product product) {
        return productDAO.insertProduct(product);
    }

    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }
}
