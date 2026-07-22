package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {

    private int id;
    private int userId;
    private int productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImageUrl;
    private int quantity;

    public CartItem() {}

    public CartItem(int userId, int productId, int quantity) {
        this.userId    = userId;
        this.productId = productId;
        this.quantity  = quantity;
    }

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public int getUserId()                          { return userId; }
    public void setUserId(int userId)               { this.userId = userId; }

    public int getProductId()                       { return productId; }
    public void setProductId(int productId)         { this.productId = productId; }

    public String getProductName()                  { return productName; }
    public void setProductName(String name)         { this.productName = name; }

    public BigDecimal getProductPrice()             { return productPrice; }
    public void setProductPrice(BigDecimal price)   { this.productPrice = price; }

    public String getProductImageUrl()              { return productImageUrl; }
    public void setProductImageUrl(String url)      { this.productImageUrl = url; }

    public int getQuantity()                        { return quantity; }
    public void setQuantity(int quantity)           { this.quantity = quantity; }

    public BigDecimal getSubtotal() {
        if (productPrice == null) return BigDecimal.ZERO;
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
