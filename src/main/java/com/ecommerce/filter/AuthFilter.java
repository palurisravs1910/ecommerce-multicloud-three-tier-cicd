package com.ecommerce.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig config) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean loggedIn = session != null && session.getAttribute("loggedUser") != null;

        if (!loggedIn) {
            resp.sendRedirect(req.getContextPath() + "/auth?action=login");
            return;
        }

        // Admin-only path guard
        String path = req.getServletPath();
        if (path.startsWith("/admin")) {
            String role = (String) session.getAttribute("userRole");
            if (!"ADMIN".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
