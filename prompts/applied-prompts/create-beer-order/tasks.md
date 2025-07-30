# Beer Order Functionality Implementation Tasks

## 1. Entity Layer Implementation

### 1.1 Enhance Beer Entity
- [x] Update the Beer entity to include a one-to-many relationship with OrderLine
- [x] Initialize the collection to an empty HashSet
- [x] Use appropriate fetch type (LAZY) and cascade types

### 1.2 Create Customer Entity
- [x] Implement the Customer entity with all required fields
- [x] Set up the one-to-many relationship with BeerOrder
- [x] Apply appropriate JPA annotations
- [x] Apply Lombok annotations (@Getter, @Setter, @Builder, etc.)
- [x] Include helper methods for managing bidirectional relationships
- [x] Use appropriate fetch type (LAZY) and cascade types

### 1.3 Create BeerOrder Entity
- [x] Implement the BeerOrder entity with all required fields
- [x] Set up the many-to-one relationship with Customer
- [x] Set up the one-to-many relationship with OrderLine
- [x] Apply appropriate JPA annotations
- [x] Apply Lombok annotations
- [x] Include helper methods for managing bidirectional relationships
- [x] Use appropriate fetch type (LAZY) and cascade types

### 1.4 Create OrderLine Entity
- [x] Implement the OrderLine entity with all required fields
- [x] Set up the many-to-one relationships with Beer and BeerOrder
- [x] Apply appropriate JPA annotations
- [x] Apply Lombok annotations
- [x] Use appropriate fetch type and cascade types

## 2. DTO Layer Implementation

### 2.1 Create CustomerDto
- [x] Implement the CustomerDto record with all required fields
- [x] Apply appropriate validation annotations

### 2.2 Create BeerOrderDto
- [x] Implement the BeerOrderDto record with all required fields
- [x] Apply appropriate validation annotations

### 2.3 Create OrderLineDto
- [x] Implement the OrderLineDto record with all required fields
- [x] Apply appropriate validation annotations

### 2.4 Create Command Objects
- [x] Implement the CreateBeerOrderCommand record for order creation
- [x] Apply appropriate validation annotations

## 3. Mapper Implementation

### 3.1 Create CustomerMapper
- [x] Implement a MapStruct mapper for Customer <-> CustomerDto conversion
- [x] Ignore appropriate fields when mapping from DTO to entity

### 3.2 Create BeerOrderMapper
- [x] Implement a MapStruct mapper for BeerOrder <-> BeerOrderDto conversion
- [x] Handle the relationship with Customer and OrderLine entities
- [x] Ignore appropriate fields when mapping from DTO to entity

### 3.3 Create OrderLineMapper
- [x] Implement a MapStruct mapper for OrderLine <-> OrderLineDto conversion
- [x] Handle the relationship with Beer and BeerOrder entities
- [x] Ignore appropriate fields when mapping from DTO to entity

## 4. Repository Implementation

### 4.1 Create CustomerRepository
- [x] Implement a Spring Data JPA repository for Customer entities
- [x] Extend JpaRepository for basic CRUD operations

### 4.2 Create BeerOrderRepository
- [x] Implement a Spring Data JPA repository for BeerOrder entities
- [x] Include methods to find orders by customer
- [x] Extend JpaRepository for basic CRUD operations

### 4.3 Create OrderLineRepository
- [x] Implement a Spring Data JPA repository for OrderLine entities
- [x] Include methods to find order lines by beer and/or order
- [x] Extend JpaRepository for basic CRUD operations

## 5. Service Layer Implementation

### 5.1 Create CustomerService
- [x] Define the CustomerService interface with CRUD operations
- [x] Implement the CustomerServiceImpl class
- [x] Use constructor injection for dependencies
- [x] Apply appropriate transaction annotations
- [x] Methods should accept and return DTOs

### 5.2 Create BeerOrderService
- [x] Define the BeerOrderService interface with CRUD operations
- [x] Implement the BeerOrderServiceImpl class
- [x] Include methods for creating, retrieving, updating, and deleting orders
- [x] Use constructor injection for dependencies
- [x] Apply appropriate transaction annotations
- [x] Methods should accept and return DTOs
- [x] Ensure proper relationship management

### 5.3 Evaluate Need for OrderLineService
- [x] Determine if a separate OrderLineService is necessary
- [x] If needed, follow the same pattern as other services

## 6. Controller Layer Implementation

### 6.1 Create CustomerController
- [x] Implement a RESTful controller for Customer resources
- [x] Follow REST API design principles
- [x] Use appropriate HTTP methods and status codes
- [x] Apply validation to request DTOs
- [x] Use constructor injection for dependencies
- [x] Return ResponseEntity with appropriate status codes

### 6.2 Create BeerOrderController
- [x] Implement a RESTful controller for BeerOrder resources
- [x] Include endpoints for all required operations
- [x] Follow REST API design principles
- [x] Use appropriate HTTP methods and status codes
- [x] Apply validation to request DTOs
- [x] Use constructor injection for dependencies
- [x] Return ResponseEntity with appropriate status codes

## 7. Testing

### 7.1 Unit Tests
- [x] Create unit tests for all service implementations
- [x] Mock dependencies as appropriate
- [x] Test all business logic and edge cases

### 7.2 Repository Tests
- [x] Create integration tests for repositories
- [x] Use Testcontainers for database testing
- [x] Test all custom query methods

### 7.3 Controller Tests
- [x] Create controller tests using MockMvc
- [x] Test all endpoints with various scenarios
- [x] Test validation logic

## 8. Configuration and Technical Requirements

### 8.1 Application Properties
- [x] Disable Open Session in View by setting `spring.jpa.open-in-view=false`
- [x] Configure any other necessary properties

### 8.2 Exception Handling
- [x] Implement a global exception handler using @ControllerAdvice
- [x] Return consistent error responses
- [x] Handle common exceptions (EntityNotFoundException, ValidationException, etc.)

## 9. Documentation

### 9.1 API Documentation
- [x] Document all API endpoints
- [x] Include request/response examples
- [x] Document validation rules

## 10. Final Review and Testing

### 10.1 Code Review
- [x] Ensure all code follows the Spring Boot guidelines
- [x] Verify that all requirements have been met
- [x] Check for any potential issues or edge cases

### 10.2 End-to-End Testing
- [x] Test the complete flow from order creation to retrieval
- [x] Verify that all relationships are properly maintained
- [x] Test edge cases and error scenarios
