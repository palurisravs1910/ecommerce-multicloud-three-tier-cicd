package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order implements Serializable {

    private int id;
    private int userId;
    private String userName;
    private BigDecimal totalAmount;
    private String status;  // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    private String shippingAddress;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<OrderItem> items;

    public Order() {}

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public int getUserId()                          { return userId; }
    public void setUserId(int userId)               { this.userId = userId; }

    public String getUserName()                     { return userName; }
    public void setUserName(String userName)        { this.userName = userName; }

    public BigDecimal getTotalAmount()              { return totalAmount; }
    public void setTotalAmount(BigDecimal amount)   { this.totalAmount = amount; }

    public String getStatus()                       { return status; }
    public void setStatus(String status)            { this.status = status; }

    public String getShippingAddress()              { return shippingAddress; }
    public void setShippingAddress(String addr)     { this.shippingAddress = addr; }

    public Timestamp getCreatedAt()                 { return createdAt; }
    public void setCreatedAt(Timestamp t)           { this.createdAt = t; }

    public Timestamp getUpdatedAt()                 { return updatedAt; }
    public void setUpdatedAt(Timestamp t)           { this.updatedAt = t; }

    public List<OrderItem> getItems()               { return items; }
    public void setItems(List<OrderItem> items)     { this.items = items; }

    @Override
    public String toString() {
        return "Order{id=" + id + ", userId=" + userId + ", total=" + totalAmount + ", status='" + status + "'}";
    }
}
