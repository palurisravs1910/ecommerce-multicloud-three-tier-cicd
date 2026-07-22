package com.ecommerce.controller;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        List<CartItem> items = cartService.getCartItems(user.getId());
        BigDecimal total     = cartService.getCartTotal(items);

        req.setAttribute("cartItems", items);
        req.setAttribute("cartTotal", total);
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        String action = req.getParameter("action");

        switch (action != null ? action : "") {
            case "add":
                handleAdd(req, resp, user.getId());
                break;
            case "update":
                handleUpdate(req, resp, user.getId());
                break;
            case "remove":
                handleRemove(req, resp, user.getId());
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws IOException, ServletException {
        int productId = Integer.parseInt(req.getParameter("productId"));
        int quantity  = Integer.parseInt(req.getParameter("quantity") != null
                                        ? req.getParameter("quantity") : "1");

        String error = cartService.addToCart(userId, productId, quantity);
        if (error != null) {
            req.getSession().setAttribute("cartError", error);
        }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws IOException {
        int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));
        int quantity   = Integer.parseInt(req.getParameter("quantity"));
        cartService.updateQuantity(cartItemId, userId, quantity);
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws IOException {
        int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));
        cartService.removeItem(cartItemId, userId);
        resp.sendRedirect(req.getContextPath() + "/cart");
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
