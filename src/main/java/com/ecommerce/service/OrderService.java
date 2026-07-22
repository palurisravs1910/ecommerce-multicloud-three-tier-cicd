package com.ecommerce.service;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.OrderDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderDAO   orderDAO   = new OrderDAO();
    private final CartDAO    cartDAO    = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Create an order from user's cart.
     * Transactional: create order → insert items → reduce stock → clear cart.
     * Returns order ID on success, -1 on failure.
     */
    public int createOrderFromCart(int userId, String shippingAddress,
                                   List<CartItem> cartItems, BigDecimal totalAmount) {
        if (cartItems == null || cartItems.isEmpty()) return -1;
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) return -1;

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Create Order
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setShippingAddress(shippingAddress);
            int orderId = orderDAO.createOrder(order, conn);

            // 2. Insert Order Items
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem ci : cartItems) {
                OrderItem oi = new OrderItem(orderId, ci.getProductId(),
                                             ci.getQuantity(), ci.getProductPrice());
                orderItems.add(oi);
            }
            orderDAO.insertOrderItems(orderItems, conn);

            // 3. Reduce Product Stock
            for (CartItem ci : cartItems) {
                boolean reduced = productDAO.reduceStock(ci.getProductId(), ci.getQuantity(), conn);
                if (!reduced) {
                    throw new SQLException("Insufficient stock for product ID " + ci.getProductId());
                }
            }

            // 4. Clear Cart
            cartDAO.clearCart(userId, conn);

            conn.commit();
            logger.info("Order created successfully: orderId={}, userId={}", orderId, userId);
            return orderId;

        } catch (SQLException e) {
            logger.error("Failed to create order for userId={}", userId, e);
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { logger.error("Rollback failed", ex); }
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.warn("Failed to close connection", e);
                }
            }
        }
    }

    /**
     * Get a single order by ID.
     */
    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    /**
     * Get all orders for a user.
     */
    public List<Order> getOrdersByUser(int userId) {
        return orderDAO.getOrdersByUser(userId);
    }

    /**
     * Get all orders (admin).
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    /**
     * Update order status (admin).
     */
    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }
}
