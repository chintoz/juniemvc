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
    update_date TIMESTAMP
);

-- Add indexes for frequently queried beer columns
CREATE INDEX idx_beer_name ON beer(beer_name);
CREATE INDEX idx_beer_style ON beer(beer_style);
CREATE INDEX idx_upc ON beer(upc);

-- Customer Table
-- Stores customer information including name, contact details
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    created_date TIMESTAMP,
    update_date TIMESTAMP
);

-- Add indexes for frequently queried customer columns
CREATE INDEX idx_customer_name ON customer(name);
CREATE INDEX idx_customer_email ON customer(email);

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
    CONSTRAINT fk_beer_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Add indexes for frequently queried beer_order columns
CREATE INDEX idx_beer_order_status ON beer_order(order_status);
CREATE INDEX idx_beer_order_customer ON beer_order(customer_id);

-- Order Line Table
-- Stores individual line items for each order
CREATE TABLE order_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_quantity INT NOT NULL,
    beer_id INT,
    beer_order_id INT,

    -- Foreign key constraints
    CONSTRAINT fk_order_line_beer FOREIGN KEY (beer_id) REFERENCES beer(id),
    CONSTRAINT fk_order_line_beer_order FOREIGN KEY (beer_order_id) REFERENCES beer_order(id)
);

-- Add indexes for frequently queried order_line columns
CREATE INDEX idx_order_line_beer ON order_line(beer_id);
CREATE INDEX idx_order_line_beer_order ON order_line(beer_order_id);

-- Add comments to explain the purpose of this migration script
-- This script creates the initial database schema for the JunieMVC application
-- It defines tables for beers, customers, orders, and order lines with appropriate relationships
-- Primary keys, foreign keys, and indexes are defined for optimal performance
