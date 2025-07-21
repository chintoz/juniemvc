# Flyway Implementation Tasks

## 1. Configure Flyway in application.properties
- [x] 1.1. Change `spring.jpa.hibernate.ddl-auto` from `create-drop` to `validate`
- [x] 1.2. Add Flyway-specific configurations:
  - [x] 1.2.1. Enable Flyway with `spring.flyway.enabled=true`
  - [x] 1.2.2. Configure baseline on migrate with `spring.flyway.baseline-on-migrate=true`
  - [x] 1.2.3. Set migration scripts location with `spring.flyway.locations=classpath:db/migration`
- [x] 1.3. Add comments explaining Flyway configuration

## 2. Create Database Schema Migration Script
- [x] 2.1. Create `V1__create_schema.sql` in `src/main/resources/db/migration`
- [x] 2.2. Define `beer` table with columns:
  - [x] 2.2.1. id, version, beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date
  - [x] 2.2.2. Add primary key constraint
  - [x] 2.2.3. Add appropriate indexes
- [x] 2.3. Define `customer` table with columns:
  - [x] 2.3.1. id, version, name, email, phone, created_date, update_date
  - [x] 2.3.2. Add primary key constraint
  - [x] 2.3.3. Add appropriate indexes
- [x] 2.4. Define `beer_order` table with columns:
  - [x] 2.4.1. id, version, order_status, created_date, update_date, customer_id
  - [x] 2.4.2. Add primary key constraint
  - [x] 2.4.3. Add foreign key constraint for customer_id
  - [x] 2.4.4. Add appropriate indexes
- [x] 2.5. Define `order_line` table with columns:
  - [x] 2.5.1. id, order_quantity, beer_id, beer_order_id
  - [x] 2.5.2. Add primary key constraint
  - [x] 2.5.3. Add foreign key constraints for beer_id and beer_order_id
  - [x] 2.5.4. Add appropriate indexes
- [x] 2.6. Add detailed comments explaining the purpose of each section

## 3. Create Beer Data Migration Script
- [x] 3.1. Create `V2__insert_beer_data.sql` in `src/main/resources/db/migration`
- [x] 3.2. Add INSERT statements for at least 10 famous beers
- [x] 3.3. Include all required fields for each beer:
  - [x] 3.3.1. beer_name
  - [x] 3.3.2. beer_style
  - [x] 3.3.3. upc
  - [x] 3.3.4. quantity_on_hand
  - [x] 3.3.5. price
- [x] 3.4. Add comments explaining the data being inserted

## 4. Create Integration Tests
- [x] 4.1. Create a test class to verify Flyway migrations
- [x] 4.2. Add test to verify the database schema is properly created
- [x] 4.3. Add test to verify initial beer data is correctly inserted
- [x] 4.4. Add test to verify the application can read the pre-populated beer data
- [x] 4.5. Use Spring Boot's test framework with `@SpringBootTest`
- [x] 4.6. Verify database state using JPA repositories

## 5. Test and Verify
- [x] 5.1. Run the application to verify Flyway migrations work correctly
- [x] 5.2. Run integration tests to verify schema and data
- [x] 5.3. Verify that the application starts without errors
- [x] 5.4. Verify that the database schema matches the entity definitions
- [x] 5.5. Verify that the beer data is correctly inserted

## 6. Documentation
- [x] 6.1. Add comments in application.properties explaining Flyway configuration
- [x] 6.2. Add detailed comments in migration scripts explaining the purpose of each section
- [x] 6.3. Update README.md (if exists) to mention Flyway usage
