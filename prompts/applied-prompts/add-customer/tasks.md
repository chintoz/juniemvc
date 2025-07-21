# Customer Entity Enhancement Task List

This document contains the detailed task list for implementing address information to the Customer entity in the JunieMVC application.

## 1. Database Schema Updates

- [x] Create new Flyway migration script V3__add_customer_address.sql
- [x] Add address_line_1 column (NOT NULL with default value)
- [x] Add address_line_2 column (nullable)
- [x] Add city column (NOT NULL with default value)
- [x] Add state column (NOT NULL with default value)
- [x] Add postal_code column (NOT NULL with default value)
- [x] Remove default values after initial migration

## 2. Entity Updates

- [x] Update Customer.java entity with addressLine1 property (NotNull)
- [x] Add addressLine2 property (nullable)
- [x] Add city property (NotNull)
- [x] Add state property (NotNull)
- [x] Add postalCode property (NotNull)
- [x] Add getters and setters for new properties

## 3. DTO Updates

- [x] Update CustomerDto record with addressLine1 field (with validation annotations)
- [x] Add addressLine2 field (with validation annotations)
- [x] Add city field (with validation annotations)
- [x] Add state field (with validation annotations)
- [x] Add postalCode field (with validation annotations)

## 4. Service Layer Updates

- [x] Update CustomerServiceImpl.updateCustomer() method to handle address properties
- [x] Ensure any other service methods properly handle the new address properties

## 5. Test Updates

### 5.1 Entity Tests
- [x] Update tests that create Customer entities to include address properties
- [x] Add validation tests for the new not-null constraints

### 5.2 Repository Tests
- [x] Update CustomerRepositoryTest to include address properties in test data
- [x] Add tests to verify address properties are correctly persisted and retrieved

### 5.3 Service Tests
- [x] Update CustomerServiceImplTest to include address properties in test data
- [x] Ensure updateCustomer tests verify address properties are correctly updated

### 5.4 Controller Tests
- [x] Update CustomerControllerTest to include address properties in test data
- [x] Add tests for validation of address properties in request bodies

## 6. OpenAPI Documentation Updates

- [x] Update or create Customer schema in OpenAPI specification
- [x] Document validation constraints for address properties
- [x] Ensure all Customer-related endpoints are properly documented

## 7. Integration Testing

- [x] Test Flyway migration script execution
- [x] Verify Customer CRUD operations through the API
- [x] Test validation errors for invalid address data
- [x] Verify backward compatibility with existing code