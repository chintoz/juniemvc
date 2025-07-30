-- Database Migration Script for Beer Order Shipment
-- This script adds the beer_order_shipment table to the database

-- Beer Order Shipment Table
-- Stores information about shipments for beer orders
CREATE TABLE beer_order_shipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    shipment_date DATE NOT NULL,
    carrier VARCHAR(255) NOT NULL,
    tracking_number VARCHAR(255) NOT NULL,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    beer_order_id INT,
    
    -- Foreign key constraint to beer_order table
    CONSTRAINT fk_beer_order_shipment_beer_order FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),
    
    -- Add indexes for frequently queried columns
    INDEX idx_beer_order_shipment_date (shipment_date),
    INDEX idx_beer_order_shipment_carrier (carrier),
    INDEX idx_beer_order_shipment_tracking_number (tracking_number),
    INDEX idx_beer_order_shipment_beer_order (beer_order_id)
);

-- Add comments to explain the purpose of this migration script
-- This script adds the beer_order_shipment table to the database
-- It defines the table with appropriate columns, foreign key, and indexes
-- The table stores information about shipments for beer orders