package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        String orderId = req.getParameter("orderId");

        if (orderId != null) {
            // Order detail view
            Order order = orderService.getOrderById(Integer.parseInt(orderId));
            if (order == null || order.getUserId() != user.getId() && !user.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/views/order-detail.jsp").forward(req, resp);
        } else {
            // Order history list
            List<Order> orders = orderService.getOrdersByUser(user.getId());
            req.setAttribute("orders", orders);
            req.setAttribute("paymentSuccess", req.getParameter("success"));
            req.setAttribute("paymentFailed",  req.getParameter("failed"));
            req.getRequestDispatcher("/WEB-INF/views/order-history.jsp").forward(req, resp);
        }
    }
}
