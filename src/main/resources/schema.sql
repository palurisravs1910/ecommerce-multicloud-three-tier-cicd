-- ============================================================
-- E-Commerce Application - Database Schema
-- Compatible with MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- ============================================================
-- USERS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    phone       VARCHAR(20),
    address     TEXT,
    role        ENUM('CUSTOMER', 'ADMIN') DEFAULT 'CUSTOMER',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- CATEGORIES TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS categories (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- PRODUCTS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS products (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    category_id  INT,
    name         VARCHAR(200) NOT NULL,
    description  TEXT,
    price        DECIMAL(10, 2) NOT NULL,
    stock        INT DEFAULT 0,
    image_url    VARCHAR(500),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- ============================================================
-- CART TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS cart (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    product_id  INT NOT NULL,
    quantity    INT DEFAULT 1,
    added_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY uq_cart_user_product (user_id, product_id)
);

-- ============================================================
-- ORDERS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS orders (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    total_amount    DECIMAL(10, 2) NOT NULL,
    status          ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address TEXT NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================
-- ORDER ITEMS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS order_items (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    order_id    INT NOT NULL,
    product_id  INT NOT NULL,
    quantity    INT NOT NULL,
    unit_price  DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_item_order   FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- ============================================================
-- PAYMENTS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS payments (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    order_id            INT NOT NULL UNIQUE,
    stripe_payment_id   VARCHAR(255),
    stripe_client_secret VARCHAR(255),
    amount              DECIMAL(10, 2) NOT NULL,
    currency            VARCHAR(10) DEFAULT 'usd',
    status              ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    paid_at             TIMESTAMP NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- ============================================================
-- SEED DATA - Categories
-- ============================================================
INSERT INTO categories (name, description) VALUES
('Electronics',   'Phones, Laptops, Gadgets'),
('Clothing',      'Men and Women Fashion'),
('Books',         'Fiction, Non-fiction, Academic'),
('Home & Kitchen','Furniture, Appliances, Decor'),
('Sports',        'Fitness, Outdoor, Sports Gear');

-- ============================================================
-- SEED DATA - Products
-- ============================================================
INSERT INTO products (category_id, name, description, price, stock, image_url) VALUES
(1, 'Wireless Headphones',  'Noise-cancelling Bluetooth headphones', 79.99,  50,  '/images/headphones.jpg'),
(1, 'Smartphone 12 Pro',    '6.5 inch AMOLED, 128GB storage',        699.99, 30,  '/images/smartphone.jpg'),
(2, 'Men Cotton T-Shirt',   '100% cotton, available in all sizes',   19.99,  200, '/images/tshirt.jpg'),
(2, 'Women Denim Jacket',   'Slim fit, premium denim',               59.99,  80,  '/images/jacket.jpg'),
(3, 'Clean Code Book',      'By Robert C. Martin',                   35.99,  100, '/images/cleancode.jpg'),
(4, 'Coffee Maker',         'Drip coffee maker, 12-cup capacity',    49.99,  60,  '/images/coffeemaker.jpg'),
(5, 'Yoga Mat',             'Non-slip, eco-friendly, 6mm thick',     25.99,  150, '/images/yogamat.jpg');

-- ============================================================
-- SEED DATA - Admin User (password: Admin@123)
-- ============================================================
INSERT INTO users (name, email, password, role) VALUES
('Admin', 'admin@ecommerce.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');
