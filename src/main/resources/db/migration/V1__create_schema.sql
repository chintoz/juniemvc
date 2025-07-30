-- Database Schema Migration Script for JunieMVC Application
-- This script creates the initial database schema for the application

-- Beer Table
-- Stores information about beers including name, style, price, and inventory
CREATE TABLE beer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    beer_name VARCHAR(255) NOT NULL,
    beer_style VARCHAR(255),
    upc VARCHAR(255) NOT NULL,
    quantity_on_hand INT,
    price DECIMAL(10,2) NOT NULL,
    created_date TIMESTAMP,
    update_date TIMESTAMP,

    -- Add indexes for frequently queried columns
    INDEX idx_beer_name (beer_name),
    INDEX idx_beer_style (beer_style),
    INDEX idx_upc (upc)
);

-- Customer Table
-- Stores customer information including name, contact details
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    created_date TIMESTAMP,
    update_date TIMESTAMP,

    -- Add indexes for frequently queried columns
    INDEX idx_customer_name (name),
    INDEX idx_customer_email (email)
);

-- Beer Order Table
-- Stores order information and links to customer
CREATE TABLE beer_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    order_status VARCHAR(50),
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    customer_id INT,

    -- Foreign key constraint to customer table
    CONSTRAINT fk_beer_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),

    -- Add indexes for frequently queried columns
    INDEX idx_beer_order_status (order_status),
    INDEX idx_beer_order_customer (customer_id)
);

-- Order Line Table
-- Stores individual line items for each order
CREATE TABLE order_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_quantity INT NOT NULL,
    beer_id INT,
    beer_order_id INT,

    -- Foreign key constraints
    CONSTRAINT fk_order_line_beer FOREIGN KEY (beer_id) REFERENCES beer(id),
    CONSTRAINT fk_order_line_beer_order FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),

    -- Add indexes for frequently queried columns
    INDEX idx_order_line_beer (beer_id),
    INDEX idx_order_line_beer_order (beer_order_id)
);

-- Add comments to explain the purpose of this migration script
-- This script creates the initial database schema for the JunieMVC application
-- It defines tables for beers, customers, orders, and order lines with appropriate relationships
-- Primary keys, foreign keys, and indexes are defined for optimal performance
