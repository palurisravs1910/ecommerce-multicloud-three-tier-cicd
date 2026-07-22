package com.ecommerce.dao;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);

    /**
     * Create a new order and return the generated order ID.
     * Uses the provided connection so this can be part of a transaction.
     */
    public int createOrder(Order order, Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, shipping_address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, "PENDING");
            ps.setString(4, order.getShippingAddress());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Failed to create order — no generated key returned.");
    }

    /**
     * Insert order items. Uses the provided connection for transaction support.
     */
    public void insertOrderItems(List<OrderItem> items, Connection conn) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (OrderItem item : items) {
                ps.setInt(1, item.getOrderId());
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getUnitPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /**
     * Get a single order by ID.
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, u.name AS user_name FROM orders o " +
                     "JOIN users u ON o.user_id = u.id WHERE o.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrderRow(rs);
                    order.setItems(getOrderItems(orderId));
                    return order;
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching order id={}", orderId, e);
        }
        return null;
    }

    /**
     * Get all orders for a specific user.
     */
    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name FROM orders o " +
                     "JOIN users u ON o.user_id = u.id WHERE o.user_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrderRow(rs);
                    order.setItems(getOrderItems(order.getId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching orders for userId={}", userId, e);
        }
        return orders;
    }

    /**
     * Get all orders (admin).
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name FROM orders o " +
                     "JOIN users u ON o.user_id = u.id ORDER BY o.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(mapOrderRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all orders", e);
        }
        return orders;
    }

    /**
     * Update order status (admin).
     */
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating status for order id={}", orderId, e);
            return false;
        }
    }

    /**
     * Get all items belonging to an order.
     */
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching items for order id={}", orderId, e);
        }
        return items;
    }

    private Order mapOrderRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setUserName(rs.getString("user_name"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }
}
