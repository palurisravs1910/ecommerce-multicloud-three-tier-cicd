package com.ecommerce.dao;

import com.ecommerce.model.CartItem;
import com.ecommerce.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartDAO.class);

    /**
     * Add item to cart. If already exists, increase quantity.
     */
    public boolean addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT id, quantity FROM cart WHERE user_id=? AND product_id=?";
        String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE user_id=? AND product_id=?";

        try (Connection conn = DBConnection.getConnection()) {
            // Check if product already in cart
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setInt(1, userId);
                check.setInt(2, productId);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        // Already in cart — update quantity
                        try (PreparedStatement update = conn.prepareStatement(updateSql)) {
                            update.setInt(1, quantity);
                            update.setInt(2, userId);
                            update.setInt(3, productId);
                            return update.executeUpdate() > 0;
                        }
                    }
                }
            }
            // Not in cart — insert
            try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                insert.setInt(1, userId);
                insert.setInt(2, productId);
                insert.setInt(3, quantity);
                return insert.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error adding to cart userId={}, productId={}", userId, productId, e);
            return false;
        }
    }

    /**
     * Get all cart items for a user with product details.
     */
    public List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, " +
                     "p.name AS product_name, p.price AS product_price, p.image_url AS product_image_url " +
                     "FROM cart c JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductPrice(rs.getBigDecimal("product_price"));
                    item.setProductImageUrl(rs.getString("product_image_url"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching cart items for userId={}", userId, e);
        }
        return items;
    }

    /**
     * Update quantity of a specific cart item.
     */
    public boolean updateQuantity(int cartItemId, int userId, int quantity) {
        String sql = "UPDATE cart SET quantity=? WHERE id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating cart item id={}", cartItemId, e);
            return false;
        }
    }

    /**
     * Remove a single item from the cart.
     */
    public boolean removeFromCart(int cartItemId, int userId) {
        String sql = "DELETE FROM cart WHERE id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error removing cart item id={}", cartItemId, e);
            return false;
        }
    }

    /**
     * Clear entire cart for a user (after successful order).
     */
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("Error clearing cart for userId={}", userId, e);
            return false;
        }
    }

    /**
     * Clear cart using an existing connection (used inside order transaction).
     */
    public void clearCart(int userId, Connection conn) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Count items in cart for a user (for nav badge).
     */
    public int getCartCount(int userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error counting cart items for userId={}", userId, e);
        }
        return 0;
    }
}
