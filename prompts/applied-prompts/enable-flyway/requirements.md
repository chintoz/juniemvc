# Implementing Flyway Database Migrations

## Overview
This document outlines the requirements for implementing Flyway database migrations in the JunieMVC application. Flyway will be used to manage database schema changes and populate initial data.

## Current State
- The application uses an H2 in-memory database
- Hibernate is currently set to `create-drop` mode, recreating the schema on each application startup
- Flyway dependency is already included in the pom.xml
- The migration directory structure exists at `src/main/resources/db/migration` but contains no migration scripts

## Requirements

### 1. Configure Flyway
1. Update `application.properties` to:
   - Disable Hibernate's automatic schema generation (`spring.jpa.hibernate.ddl-auto=validate`)
   - Add Flyway-specific configurations if needed

### 2. Create Database Schema Migration
1. Create a migration script `V1__create_schema.sql` to establish the initial database schema:
   - Create tables for all entities (Beer, Customer, BeerOrder, OrderLine)
   - Define appropriate constraints, primary keys, and foreign keys
   - Ensure the schema matches the JPA entity definitions

### 3. Populate Initial Beer Data
1. Create a migration script `V2__insert_beer_data.sql` to populate the Beer table with:
   - At least 10 famous beers from around the world
   - Include all required fields: beerName, beerStyle, upc, quantityOnHand, price
   - Use realistic data for each beer

### 4. Testing Requirements
1. Create integration tests to verify:
   - Migrations are applied correctly
   - The database schema is properly created
   - Initial beer data is correctly inserted
   - The application can read the pre-populated beer data

### 5. Documentation
1. Document the Flyway configuration in code comments
2. Add comments in migration scripts explaining the purpose of each section

## Technical Specifications

### Beer Entity Fields to Populate
- `beerName`: String - Name of the beer
- `beerStyle`: String - Style of the beer (e.g., IPA, Stout, Lager)
- `upc`: String - Universal Product Code
- `quantityOnHand`: Integer - Available quantity
- `price`: BigDecimal - Price of the beer

### Migration Naming Convention
- Follow Flyway's versioned migration naming convention: `V<version>__<description>.sql`
- Use sequential version numbers (V1, V2, etc.)
- Use descriptive names that clearly indicate the purpose of the migration

### Best Practices
1. Make migrations idempotent where possible
2. Keep migrations small and focused on a single purpose
3. Never modify existing migration files after they've been committed
4. Test migrations thoroughly before deployment
5. Consider using repeatable migrations (R__) for views, stored procedures, or other objects that can be replaced

## Acceptance Criteria
1. The application starts successfully with Flyway enabled
2. Flyway migrations create the database schema and populate initial data
3. All integration tests pass
4. The application can retrieve the pre-populated beer data through its API
5. Hibernate validation mode is active, ensuring the schema matches entity definitions