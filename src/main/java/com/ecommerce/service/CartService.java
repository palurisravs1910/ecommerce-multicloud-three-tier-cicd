package com.ecommerce.service;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO       = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Add product to cart. Validates stock availability.
     */
    public String addToCart(int userId, int productId, int quantity) {
        if (quantity <= 0) return "Quantity must be at least 1.";
        Product product = productDAO.getProductById(productId);
        if (product == null)              return "Product not found.";
        if (product.getStock() < quantity) return "Not enough stock available.";
        boolean added = cartDAO.addToCart(userId, productId, quantity);
        return added ? null : "Failed to add item to cart.";
    }

    /**
     * Get all cart items for user.
     */
    public List<CartItem> getCartItems(int userId) {
        return cartDAO.getCartItems(userId);
    }

    /**
     * Update quantity of a cart item.
     */
    public String updateQuantity(int cartItemId, int userId, int quantity) {
        if (quantity <= 0) {
            return removeItem(cartItemId, userId) ? null : "Failed to remove item.";
        }
        return cartDAO.updateQuantity(cartItemId, userId, quantity) ? null : "Update failed.";
    }

    /**
     * Remove a single item from cart.
     */
    public boolean removeItem(int cartItemId, int userId) {
        return cartDAO.removeFromCart(cartItemId, userId);
    }

    /**
     * Clear entire cart.
     */
    public boolean clearCart(int userId) {
        return cartDAO.clearCart(userId);
    }

    /**
     * Calculate cart total.
     */
    public BigDecimal getCartTotal(List<CartItem> items) {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get cart item count (for nav badge).
     */
    public int getCartCount(int userId) {
        return cartDAO.getCartCount(userId);
    }
}
