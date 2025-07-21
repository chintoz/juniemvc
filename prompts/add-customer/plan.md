# Customer Entity Enhancement Plan

This document outlines the detailed plan for enhancing the Customer entity in the JunieMVC application to include address information and ensure all components are properly updated.

## 1. Current State Analysis

The JunieMVC application already has a Customer entity with the following components:

- **Customer Entity**: Contains basic fields (id, version, name, email, phone) and has a OneToMany relationship with BeerOrder
- **CustomerDto**: Record with validation annotations for the existing fields
- **CustomerMapper**: MapStruct mapper for converting between Customer entity and CustomerDto
- **CustomerRepository**: Standard Spring Data JPA repository
- **CustomerService/CustomerServiceImpl**: Service layer with standard CRUD operations
- **CustomerController**: RESTful controller exposing CRUD endpoints

The database schema is managed by Flyway with existing migration scripts:
- V1__create_schema.sql: Creates the initial schema including the customer table without address fields
- V2__insert_beer_data.sql: Inserts sample beer data

## 2. Required Changes

According to the requirements, we need to add the following address properties to the Customer entity:
- address_line_1 (not null)
- address_line_2
- city (not null)
- state (not null)
- postal_code (not null)

## 3. Implementation Plan

### 3.1 Update Customer Entity

Update the Customer.java entity to add the following properties:
```java
@NotNull
private String addressLine1;

private String addressLine2;

@NotNull
private String city;

@NotNull
private String state;

@NotNull
private String postalCode;
```

### 3.2 Create Flyway Migration Script

Create a new Flyway migration script (V3__add_customer_address.sql) to add address columns to the customer table:
```sql
-- Add address columns to customer table
ALTER TABLE customer
ADD COLUMN address_line_1 VARCHAR(255) NOT NULL DEFAULT '',
ADD COLUMN address_line_2 VARCHAR(255),
ADD COLUMN city VARCHAR(100) NOT NULL DEFAULT '',
ADD COLUMN state VARCHAR(50) NOT NULL DEFAULT '',
ADD COLUMN postal_code VARCHAR(20) NOT NULL DEFAULT '';

-- Remove default values after initial migration
ALTER TABLE customer
ALTER COLUMN address_line_1 DROP DEFAULT,
ALTER COLUMN city DROP DEFAULT,
ALTER COLUMN state DROP DEFAULT,
ALTER COLUMN postal_code DROP DEFAULT;
```

### 3.3 Update CustomerDto

Update the CustomerDto record to include the new address properties with appropriate validation annotations:
```java
@NotBlank(message = "Address line 1 is required")
@Size(max = 255, message = "Address line 1 must be less than 255 characters")
String addressLine1,

@Size(max = 255, message = "Address line 2 must be less than 255 characters")
String addressLine2,

@NotBlank(message = "City is required")
@Size(max = 100, message = "City must be less than 100 characters")
String city,

@NotBlank(message = "State is required")
@Size(max = 50, message = "State must be less than 50 characters")
String state,

@NotBlank(message = "Postal code is required")
@Size(max = 20, message = "Postal code must be less than 20 characters")
String postalCode,
```

### 3.4 Update CustomerServiceImpl

Update the updateCustomer method in CustomerServiceImpl to handle the new address properties:
```java
existingCustomer.setName(customerToUpdate.getName());
existingCustomer.setEmail(customerToUpdate.getEmail());
existingCustomer.setPhone(customerToUpdate.getPhone());
existingCustomer.setAddressLine1(customerToUpdate.getAddressLine1());
existingCustomer.setAddressLine2(customerToUpdate.getAddressLine2());
existingCustomer.setCity(customerToUpdate.getCity());
existingCustomer.setState(customerToUpdate.getState());
existingCustomer.setPostalCode(customerToUpdate.getPostalCode());
```

### 3.5 Update Tests

#### 3.5.1 Update Entity Tests
- Update any tests that create Customer entities to include address properties
- Ensure validation tests cover the new not-null constraints

#### 3.5.2 Update Repository Tests
- Update CustomerRepositoryTest to include address properties in test data
- Add tests to verify address properties are correctly persisted and retrieved

#### 3.5.3 Update Service Tests
- Update CustomerServiceImplTest to include address properties in test data
- Ensure updateCustomer tests verify address properties are correctly updated

#### 3.5.4 Update Controller Tests
- Update CustomerControllerTest to include address properties in test data
- Add tests for validation of address properties in request bodies

### 3.6 Update OpenAPI Documentation

Update the OpenAPI schema for Customer to include the new address properties:
- Create or update the Customer schema in the OpenAPI specification
- Document the validation constraints for the address properties
- Ensure all Customer-related endpoints are properly documented

## 4. Testing Strategy

### 4.1 Unit Tests
- Test Customer entity validation
- Test CustomerMapper with address properties
- Test CustomerService methods with address properties

### 4.2 Integration Tests
- Test CustomerRepository with address properties
- Test CustomerController endpoints with address properties
- Test Flyway migration script execution

### 4.3 Manual Testing
- Verify Customer CRUD operations through the API
- Verify validation errors are properly returned for invalid address data

## 5. Potential Challenges and Considerations

### 5.1 Data Migration
- The migration script adds NOT NULL columns with default values to handle existing data
- Consider adding a separate data migration script if existing customers need specific address values

### 5.2 Backward Compatibility
- Ensure any existing code that uses Customer entities or DTOs is updated to handle the new properties
- Consider if any API clients need to be updated to handle the new properties

### 5.3 Performance
- Monitor database performance after adding the new columns
- Consider adding indexes for frequently queried address fields if needed