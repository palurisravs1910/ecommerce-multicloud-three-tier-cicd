package com.ecommerce.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Category implements Serializable {

    private int id;
    private String name;
    private String description;
    private Timestamp createdAt;

    public Category() {}

    public Category(String name, String description) {
        this.name        = name;
        this.description = description;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public String getDescription()              { return description; }
    public void setDescription(String desc)     { this.description = desc; }

    public Timestamp getCreatedAt()             { return createdAt; }
    public void setCreatedAt(Timestamp t)       { this.createdAt = t; }
}
