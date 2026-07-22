package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable {

    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem() {}

    public OrderItem(int orderId, int productId, int quantity, BigDecimal unitPrice) {
        this.orderId   = orderId;
        this.productId = productId;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public int getOrderId()                         { return orderId; }
    public void setOrderId(int orderId)             { this.orderId = orderId; }

    public int getProductId()                       { return productId; }
    public void setProductId(int productId)         { this.productId = productId; }

    public String getProductName()                  { return productName; }
    public void setProductName(String name)         { this.productName = name; }

    public int getQuantity()                        { return quantity; }
    public void setQuantity(int quantity)           { this.quantity = quantity; }

    public BigDecimal getUnitPrice()                { return unitPrice; }
    public void setUnitPrice(BigDecimal price)      { this.unitPrice = price; }

    public BigDecimal getSubtotal() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
