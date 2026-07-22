package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.PaymentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();

    /**
     * Handles payment result callbacks from the frontend (Stripe.js).
     * The frontend POSTs after the PaymentIntent is confirmed.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        String status  = req.getParameter("status");   // "success" or "failed"
        String orderIdStr = req.getParameter("orderId");

        if (orderIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        int orderId = Integer.parseInt(orderIdStr);

        if ("success".equalsIgnoreCase(status)) {
            paymentService.confirmPayment(orderId);
            resp.sendRedirect(req.getContextPath() + "/orders?success=true&orderId=" + orderId);
        } else {
            paymentService.failPayment(orderId);
            resp.sendRedirect(req.getContextPath() + "/orders?failed=true&orderId=" + orderId);
        }
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
