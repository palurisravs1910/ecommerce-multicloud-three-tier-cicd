package com.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product implements Serializable {

    private int id;
    private int categoryId;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Product() {}

    public Product(int categoryId, String name, String description,
                   BigDecimal price, int stock, String imageUrl) {
        this.categoryId  = categoryId;
        this.name        = name;
        this.description = description;
        this.price       = price;
        this.stock       = stock;
        this.imageUrl    = imageUrl;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public int getCategoryId()                  { return categoryId; }
    public void setCategoryId(int categoryId)   { this.categoryId = categoryId; }

    public String getCategoryName()             { return categoryName; }
    public void setCategoryName(String name)    { this.categoryName = name; }

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public String getDescription()              { return description; }
    public void setDescription(String desc)     { this.description = desc; }

    public BigDecimal getPrice()                { return price; }
    public void setPrice(BigDecimal price)      { this.price = price; }

    public int getStock()                       { return stock; }
    public void setStock(int stock)             { this.stock = stock; }

    public String getImageUrl()                 { return imageUrl; }
    public void setImageUrl(String imageUrl)    { this.imageUrl = imageUrl; }

    public Timestamp getCreatedAt()             { return createdAt; }
    public void setCreatedAt(Timestamp t)       { this.createdAt = t; }

    public Timestamp getUpdatedAt()             { return updatedAt; }
    public void setUpdatedAt(Timestamp t)       { this.updatedAt = t; }

    public boolean isInStock() {
        return this.stock > 0;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}
