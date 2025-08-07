-- Add address columns to customer table
ALTER TABLE customer ADD COLUMN address_line1 VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE customer ADD COLUMN address_line2 VARCHAR(255);
ALTER TABLE customer ADD COLUMN city VARCHAR(100) NOT NULL DEFAULT '';
ALTER TABLE customer ADD COLUMN state VARCHAR(50) NOT NULL DEFAULT '';
ALTER TABLE customer ADD COLUMN postal_code VARCHAR(20) NOT NULL DEFAULT '';

-- Remove default values after initial migration
ALTER TABLE customer ALTER COLUMN address_line1 SET DEFAULT NULL;
ALTER TABLE customer ALTER COLUMN city SET DEFAULT NULL;
ALTER TABLE customer ALTER COLUMN state SET DEFAULT NULL;
ALTER TABLE customer ALTER COLUMN postal_code SET DEFAULT NULL;

-- Add indexes for frequently queried address fields
CREATE INDEX idx_customer_city ON customer(city);
CREATE INDEX idx_customer_state ON customer(state);
CREATE INDEX idx_customer_postal_code ON customer(postal_code);