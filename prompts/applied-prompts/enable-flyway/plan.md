# Flyway Implementation Plan for JunieMVC

## Overview
This document outlines the detailed plan for implementing Flyway database migrations in the JunieMVC application. The implementation will follow the requirements specified in the requirements.md file.

## Current State Analysis
- The application uses an H2 in-memory database
- Hibernate is currently set to `create-drop` mode, recreating the schema on each application startup
- Flyway dependency is already included in the pom.xml
- The migration directory structure exists at `src/main/resources/db/migration` but is empty
- Entity classes (Beer, Customer, BeerOrder, OrderLine) are defined with appropriate relationships

## Implementation Plan

### 1. Configure Flyway in application.properties
1. Update `application.properties` to:
   - Change `spring.jpa.hibernate.ddl-auto` from `create-drop` to `validate`
   - Add Flyway-specific configurations:
     ```properties
     # Flyway Configuration
     spring.flyway.enabled=true
     spring.flyway.baseline-on-migrate=true
     spring.flyway.locations=classpath:db/migration
     ```

### 2. Create Database Schema Migration Script
1. Create `V1__create_schema.sql` in `src/main/resources/db/migration` with:
   - Table definitions for all entities:
     - `beer` table with columns: id, version, beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date
     - `customer` table with columns: id, version, name, email, phone, created_date, update_date
     - `beer_order` table with columns: id, version, order_status, created_date, update_date, customer_id (foreign key)
     - `order_line` table with columns: id, order_quantity, beer_id (foreign key), beer_order_id (foreign key)
   - Primary key constraints for all tables
   - Foreign key constraints for relationships
   - Appropriate data types for each column
   - Indexes for frequently queried columns

### 3. Create Beer Data Migration Script
1. Create `V2__insert_beer_data.sql` in `src/main/resources/db/migration` with:
   - INSERT statements for at least 10 famous beers
   - Include all required fields: beer_name, beer_style, upc, quantity_on_hand, price
   - Use realistic data for each beer

### 4. Create Integration Tests
1. Create a test class to verify:
   - Flyway migrations are applied correctly
   - The database schema is properly created
   - Initial beer data is correctly inserted
   - The application can read the pre-populated beer data
2. Use Spring Boot's test framework with `@SpringBootTest`
3. Verify database state using JPA repositories

### 5. Documentation
1. Add comments in application.properties explaining Flyway configuration
2. Add detailed comments in migration scripts explaining the purpose of each section
3. Update README.md (if exists) to mention Flyway usage

## Implementation Steps

### Step 1: Update application.properties
1. Modify `src/main/resources/application.properties` to change Hibernate mode and add Flyway configuration

### Step 2: Create Schema Migration Script
1. Create `src/main/resources/db/migration/V1__create_schema.sql` with table definitions

### Step 3: Create Data Migration Script
1. Create `src/main/resources/db/migration/V2__insert_beer_data.sql` with beer data

### Step 4: Create Integration Tests
1. Create test class to verify migrations and data

### Step 5: Test and Verify
1. Run the application to verify Flyway migrations work correctly
2. Run integration tests to verify schema and data

## Conclusion
This implementation plan provides a structured approach to implementing Flyway database migrations in the JunieMVC application. Following this plan will ensure that the application meets all the requirements specified in the requirements document.