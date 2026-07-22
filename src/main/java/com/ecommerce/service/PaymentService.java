package com.ecommerce.service;

import com.ecommerce.dao.OrderDAO;
import com.ecommerce.dao.PaymentDAO;
import com.ecommerce.model.Payment;
import com.ecommerce.util.AppConfig;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final OrderDAO   orderDAO   = new OrderDAO();

    public PaymentService() {
        Stripe.apiKey = AppConfig.getStripeSecretKey();
    }

    /**
     * Create a Stripe PaymentIntent and persist a PENDING payment record.
     * Returns the Payment object (with clientSecret) or null on failure.
     */
    public Payment createPaymentIntent(int orderId, BigDecimal amount, String currency) {
        try {
            // Stripe expects amount in smallest currency unit (cents for USD)
            long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(currency != null ? currency : "usd")
                    .setDescription("Order #" + orderId)
                    .putMetadata("order_id", String.valueOf(orderId))
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // Persist payment record
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setStripePaymentId(intent.getId());
            payment.setStripeClientSecret(intent.getClientSecret());
            payment.setAmount(amount);
            payment.setCurrency(currency != null ? currency : "usd");
            payment.setStatus("PENDING");

            boolean saved = paymentDAO.insertPayment(payment);
            if (!saved) {
                logger.error("Failed to persist payment record for orderId={}", orderId);
                return null;
            }

            logger.info("PaymentIntent created: {}, orderId={}", intent.getId(), orderId);
            return payment;

        } catch (StripeException e) {
            logger.error("Stripe error creating PaymentIntent for orderId={}", orderId, e);
            return null;
        }
    }

    /**
     * Confirm payment success — called after Stripe confirms on the frontend.
     * Updates payment and order status.
     */
    public boolean confirmPayment(int orderId) {
        boolean paymentUpdated = paymentDAO.updatePaymentStatus(orderId, "SUCCESS");
        boolean orderUpdated   = orderDAO.updateOrderStatus(orderId, "CONFIRMED");
        if (paymentUpdated && orderUpdated) {
            logger.info("Payment confirmed for orderId={}", orderId);
            return true;
        }
        logger.error("Payment confirmation failed for orderId={}", orderId);
        return false;
    }

    /**
     * Mark payment as failed and keep order as PENDING.
     */
    public boolean failPayment(int orderId) {
        return paymentDAO.updatePaymentStatus(orderId, "FAILED");
    }

    /**
     * Get payment details by order ID.
     */
    public Payment getPaymentByOrderId(int orderId) {
        return paymentDAO.getPaymentByOrderId(orderId);
    }

    /**
     * Returns the publishable key to pass to the frontend (Stripe.js).
     */
    public String getPublishableKey() {
        return AppConfig.getStripePublishableKey();
    }
}
