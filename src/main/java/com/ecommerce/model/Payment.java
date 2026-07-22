package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment implements Serializable {

    private int id;
    private int orderId;
    private String stripePaymentId;
    private String stripeClientSecret;
    private BigDecimal amount;
    private String currency;
    private String status;  // PENDING, SUCCESS, FAILED, REFUNDED
    private Timestamp paidAt;
    private Timestamp createdAt;

    public Payment() {}

    public int getId()                                  { return id; }
    public void setId(int id)                           { this.id = id; }

    public int getOrderId()                             { return orderId; }
    public void setOrderId(int orderId)                 { this.orderId = orderId; }

    public String getStripePaymentId()                  { return stripePaymentId; }
    public void setStripePaymentId(String id)           { this.stripePaymentId = id; }

    public String getStripeClientSecret()               { return stripeClientSecret; }
    public void setStripeClientSecret(String secret)    { this.stripeClientSecret = secret; }

    public BigDecimal getAmount()                       { return amount; }
    public void setAmount(BigDecimal amount)            { this.amount = amount; }

    public String getCurrency()                         { return currency; }
    public void setCurrency(String currency)            { this.currency = currency; }

    public String getStatus()                           { return status; }
    public void setStatus(String status)                { this.status = status; }

    public Timestamp getPaidAt()                        { return paidAt; }
    public void setPaidAt(Timestamp paidAt)             { this.paidAt = paidAt; }

    public Timestamp getCreatedAt()                     { return createdAt; }
    public void setCreatedAt(Timestamp t)               { this.createdAt = t; }
}
