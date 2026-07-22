package com.ecommerce.dao;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    /**
     * Get all products with their category names.
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id ORDER BY p.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all products", e);
        }
        return products;
    }

    /**
     * Get product by ID.
     */
    public Product getProductById(int id) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error fetching product by id: {}", id, e);
        }
        return null;
    }

    /**
     * Get products by category.
     */
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id WHERE p.category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching products by category: {}", categoryId, e);
        }
        return products;
    }

    /**
     * Search products by keyword (name or description).
     */
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.name LIKE ? OR p.description LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error searching products with keyword: {}", keyword, e);
        }
        return products;
    }

    /**
     * Insert a new product (admin).
     */
    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO products (category_id, name, description, price, stock, image_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getImageUrl());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error inserting product: {}", product.getName(), e);
            return false;
        }
    }

    /**
     * Update product details (admin).
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET category_id=?, name=?, description=?, price=?, stock=?, image_url=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getImageUrl());
            ps.setInt(7, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating product id: {}", product.getId(), e);
            return false;
        }
    }

    /**
     * Delete a product (admin).
     */
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting product id: {}", id, e);
            return false;
        }
    }

    /**
     * Reduce stock after a successful order.
     */
    public boolean reduceStock(int productId, int quantity, Connection conn) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Get all categories.
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getInt("id"));
                cat.setName(rs.getString("name"));
                cat.setDescription(rs.getString("description"));
                cat.setCreatedAt(rs.getTimestamp("created_at"));
                categories.add(cat);
            }
        } catch (SQLException e) {
            logger.error("Error fetching categories", e);
        }
        return categories;
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}
