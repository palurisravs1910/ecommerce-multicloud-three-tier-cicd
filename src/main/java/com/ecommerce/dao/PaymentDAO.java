package com.ecommerce.dao;

import com.ecommerce.model.Payment;
import com.ecommerce.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class PaymentDAO {

    private static final Logger logger = LoggerFactory.getLogger(PaymentDAO.class);

    /**
     * Insert a new payment record (PENDING state).
     */
    public boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, stripe_payment_id, stripe_client_secret, amount, currency, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getStripePaymentId());
            ps.setString(3, payment.getStripeClientSecret());
            ps.setBigDecimal(4, payment.getAmount());
            ps.setString(5, payment.getCurrency() != null ? payment.getCurrency() : "usd");
            ps.setString(6, payment.getStatus() != null ? payment.getStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error inserting payment for orderId={}", payment.getOrderId(), e);
            return false;
        }
    }

    /**
     * Get payment record by order ID.
     */
    public Payment getPaymentByOrderId(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error fetching payment for orderId={}", orderId, e);
        }
        return null;
    }

    /**
     * Get payment record by Stripe payment intent ID.
     */
    public Payment getPaymentByStripeId(String stripePaymentId) {
        String sql = "SELECT * FROM payments WHERE stripe_payment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stripePaymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error fetching payment for stripeId={}", stripePaymentId, e);
        }
        return null;
    }

    /**
     * Update payment status (SUCCESS / FAILED / REFUNDED).
     */
    public boolean updatePaymentStatus(int orderId, String status) {
        String sql = "UPDATE payments SET status=?, paid_at=? WHERE order_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, "SUCCESS".equals(status) ? new Timestamp(System.currentTimeMillis()) : null);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating payment status for orderId={}", orderId, e);
            return false;
        }
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getInt("id"));
        p.setOrderId(rs.getInt("order_id"));
        p.setStripePaymentId(rs.getString("stripe_payment_id"));
        p.setStripeClientSecret(rs.getString("stripe_client_secret"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setCurrency(rs.getString("currency"));
        p.setStatus(rs.getString("status"));
        p.setPaidAt(rs.getTimestamp("paid_at"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
