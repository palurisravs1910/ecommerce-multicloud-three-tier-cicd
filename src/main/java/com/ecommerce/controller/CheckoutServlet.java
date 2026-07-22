package com.ecommerce.controller;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.model.Payment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService    cartService    = new CartService();
    private final OrderService   orderService   = new OrderService();
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        List<CartItem> cartItems = cartService.getCartItems(user.getId());
        if (cartItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        BigDecimal total = cartService.getCartTotal(cartItems);
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", total);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        String shippingAddress = req.getParameter("shippingAddress");
        List<CartItem> cartItems = cartService.getCartItems(user.getId());

        if (cartItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        BigDecimal total = cartService.getCartTotal(cartItems);

        // Create the order (transactional)
        int orderId = orderService.createOrderFromCart(user.getId(), shippingAddress, cartItems, total);
        if (orderId < 0) {
            req.setAttribute("error", "Failed to place order. Please try again.");
            req.setAttribute("cartItems", cartItems);
            req.setAttribute("cartTotal", total);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
            return;
        }

        // Create Stripe PaymentIntent
        Payment payment = paymentService.createPaymentIntent(orderId, total, "usd");
        if (payment == null) {
            req.setAttribute("error", "Payment initialization failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
            return;
        }

        // Forward to payment page
        req.setAttribute("orderId", orderId);
        req.setAttribute("clientSecret", payment.getStripeClientSecret());
        req.setAttribute("publishableKey", paymentService.getPublishableKey());
        req.setAttribute("amount", total);
        req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
    }

    private User getLoggedUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth?action=login");
            return null;
        }
        return (User) session.getAttribute("loggedUser");
    }
}
