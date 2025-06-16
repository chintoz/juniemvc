# Beer Order Functionality Implementation Tasks

## 1. Entity Layer Implementation

### 1.1 Enhance Beer Entity
- [ ] Update the Beer entity to include a one-to-many relationship with OrderLine
- [ ] Initialize the collection to an empty HashSet
- [ ] Use appropriate fetch type (LAZY) and cascade types

### 1.2 Create Customer Entity
- [ ] Implement the Customer entity with all required fields
- [ ] Set up the one-to-many relationship with BeerOrder
- [ ] Apply appropriate JPA annotations
- [ ] Apply Lombok annotations (@Getter, @Setter, @Builder, etc.)
- [ ] Include helper methods for managing bidirectional relationships
- [ ] Use appropriate fetch type (LAZY) and cascade types

### 1.3 Create BeerOrder Entity
- [ ] Implement the BeerOrder entity with all required fields
- [ ] Set up the many-to-one relationship with Customer
- [ ] Set up the one-to-many relationship with OrderLine
- [ ] Apply appropriate JPA annotations
- [ ] Apply Lombok annotations
- [ ] Include helper methods for managing bidirectional relationships
- [ ] Use appropriate fetch type (LAZY) and cascade types

### 1.4 Create OrderLine Entity
- [ ] Implement the OrderLine entity with all required fields
- [ ] Set up the many-to-one relationships with Beer and BeerOrder
- [ ] Apply appropriate JPA annotations
- [ ] Apply Lombok annotations
- [ ] Use appropriate fetch type and cascade types

## 2. DTO Layer Implementation

### 2.1 Create CustomerDto
- [ ] Implement the CustomerDto record with all required fields
- [ ] Apply appropriate validation annotations

### 2.2 Create BeerOrderDto
- [ ] Implement the BeerOrderDto record with all required fields
- [ ] Apply appropriate validation annotations

### 2.3 Create OrderLineDto
- [ ] Implement the OrderLineDto record with all required fields
- [ ] Apply appropriate validation annotations

### 2.4 Create Command Objects
- [ ] Implement the CreateBeerOrderCommand record for order creation
- [ ] Apply appropriate validation annotations

## 3. Mapper Implementation

### 3.1 Create CustomerMapper
- [ ] Implement a MapStruct mapper for Customer <-> CustomerDto conversion
- [ ] Ignore appropriate fields when mapping from DTO to entity

### 3.2 Create BeerOrderMapper
- [ ] Implement a MapStruct mapper for BeerOrder <-> BeerOrderDto conversion
- [ ] Handle the relationship with Customer and OrderLine entities
- [ ] Ignore appropriate fields when mapping from DTO to entity

### 3.3 Create OrderLineMapper
- [ ] Implement a MapStruct mapper for OrderLine <-> OrderLineDto conversion
- [ ] Handle the relationship with Beer and BeerOrder entities
- [ ] Ignore appropriate fields when mapping from DTO to entity

## 4. Repository Implementation

### 4.1 Create CustomerRepository
- [ ] Implement a Spring Data JPA repository for Customer entities
- [ ] Extend JpaRepository for basic CRUD operations

### 4.2 Create BeerOrderRepository
- [ ] Implement a Spring Data JPA repository for BeerOrder entities
- [ ] Include methods to find orders by customer
- [ ] Extend JpaRepository for basic CRUD operations

### 4.3 Create OrderLineRepository
- [ ] Implement a Spring Data JPA repository for OrderLine entities
- [ ] Include methods to find order lines by beer and/or order
- [ ] Extend JpaRepository for basic CRUD operations

## 5. Service Layer Implementation

### 5.1 Create CustomerService
- [ ] Define the CustomerService interface with CRUD operations
- [ ] Implement the CustomerServiceImpl class
- [ ] Use constructor injection for dependencies
- [ ] Apply appropriate transaction annotations
- [ ] Methods should accept and return DTOs

### 5.2 Create BeerOrderService
- [ ] Define the BeerOrderService interface with CRUD operations
- [ ] Implement the BeerOrderServiceImpl class
- [ ] Include methods for creating, retrieving, updating, and deleting orders
- [ ] Use constructor injection for dependencies
- [ ] Apply appropriate transaction annotations
- [ ] Methods should accept and return DTOs
- [ ] Ensure proper relationship management

### 5.3 Evaluate Need for OrderLineService
- [ ] Determine if a separate OrderLineService is necessary
- [ ] If needed, follow the same pattern as other services

## 6. Controller Layer Implementation

### 6.1 Create CustomerController
- [ ] Implement a RESTful controller for Customer resources
- [ ] Follow REST API design principles
- [ ] Use appropriate HTTP methods and status codes
- [ ] Apply validation to request DTOs
- [ ] Use constructor injection for dependencies
- [ ] Return ResponseEntity with appropriate status codes

### 6.2 Create BeerOrderController
- [ ] Implement a RESTful controller for BeerOrder resources
- [ ] Include endpoints for all required operations
- [ ] Follow REST API design principles
- [ ] Use appropriate HTTP methods and status codes
- [ ] Apply validation to request DTOs
- [ ] Use constructor injection for dependencies
- [ ] Return ResponseEntity with appropriate status codes

## 7. Testing

### 7.1 Unit Tests
- [ ] Create unit tests for all service implementations
- [ ] Mock dependencies as appropriate
- [ ] Test all business logic and edge cases

### 7.2 Repository Tests
- [ ] Create integration tests for repositories
- [ ] Use Testcontainers for database testing
- [ ] Test all custom query methods

### 7.3 Controller Tests
- [ ] Create controller tests using MockMvc
- [ ] Test all endpoints with various scenarios
- [ ] Test validation logic

## 8. Configuration and Technical Requirements

### 8.1 Application Properties
- [ ] Disable Open Session in View by setting `spring.jpa.open-in-view=false`
- [ ] Configure any other necessary properties

### 8.2 Exception Handling
- [ ] Implement a global exception handler using @ControllerAdvice
- [ ] Return consistent error responses
- [ ] Handle common exceptions (EntityNotFoundException, ValidationException, etc.)

## 9. Documentation

### 9.1 API Documentation
- [ ] Document all API endpoints
- [ ] Include request/response examples
- [ ] Document validation rules

## 10. Final Review and Testing

### 10.1 Code Review
- [ ] Ensure all code follows the Spring Boot guidelines
- [ ] Verify that all requirements have been met
- [ ] Check for any potential issues or edge cases

### 10.2 End-to-End Testing
- [ ] Test the complete flow from order creation to retrieval
- [ ] Verify that all relationships are properly maintained
- [ ] Test edge cases and error scenarios