package com.ecommerce.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {

    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;  // CUSTOMER or ADMIN
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {}

    public User(String name, String email, String password, String phone, String address) {
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.phone    = phone;
        this.address  = address;
        this.role     = "CUSTOMER";
    }

    // Getters and Setters
    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getPassword()           { return password; }
    public void setPassword(String pw)    { this.password = pw; }

    public String getPhone()              { return phone; }
    public void setPhone(String phone)    { this.phone = phone; }

    public String getAddress()            { return address; }
    public void setAddress(String addr)   { this.address = addr; }

    public String getRole()               { return role; }
    public void setRole(String role)      { this.role = role; }

    public Timestamp getCreatedAt()                   { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)     { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt()                   { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt)     { this.updatedAt = updatedAt; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', role='" + role + "'}";
    }
}
