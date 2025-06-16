# Requirements: Implementing Beer Order Functionality

## Background
The current implementation provides CRUD operations for Beer entities but lacks the ability to create and manage beer orders. This project aims to implement a complete beer ordering system with proper entity relationships and all necessary layers for CRUD operations.

## Objectives
1. Implement JPA entities for the beer ordering domain model
2. Create DTOs for all new entities
3. Implement repositories, services, mappers, and controllers for all new entities
4. Ensure proper relationship management between entities
5. Provide a complete API for beer order management

## Detailed Requirements

### 1. Entity Implementation

#### 1.1 Enhance Beer Entity
- Update the existing Beer entity to include a relationship with OrderLine:
  ```java
  @OneToMany(mappedBy = "beer")
  private Set<OrderLine> orderLines = new HashSet<>();
  ```

#### 1.2 Create Customer Entity
- Implement a Customer entity with the following properties:
  - Integer id (primary key)
  - Integer version (for optimistic locking)
  - String name
  - String email
  - String phone
  - LocalDateTime createdDate
  - LocalDateTime updateDate
  - Set<BeerOrder> orders (one-to-many relationship)
- Apply appropriate JPA annotations
- Apply appropriate Lombok annotations (@Getter, @Setter, @Builder, etc.)
- Include helper methods for managing bidirectional relationships

#### 1.3 Create BeerOrder Entity
- Implement a BeerOrder entity with the following properties:
  - Integer id (primary key)
  - Integer version (for optimistic locking)
  - String orderStatus
  - LocalDateTime createdDate
  - LocalDateTime updateDate
  - Customer customer (many-to-one relationship)
  - Set<OrderLine> orderLines (one-to-many relationship)
- Apply appropriate JPA annotations
- Apply appropriate Lombok annotations
- Include helper methods for managing bidirectional relationships

#### 1.4 Create OrderLine Entity
- Implement an OrderLine entity with the following properties:
  - Integer id (primary key)
  - Integer orderQuantity
  - Beer beer (many-to-one relationship)
  - BeerOrder beerOrder (many-to-one relationship)
- Apply appropriate JPA annotations
- Apply appropriate Lombok annotations

### 2. DTO Implementation

#### 2.1 Create CustomerDto
- Implement a CustomerDto record with the following properties:
  - Integer id
  - Integer version
  - String name
  - String email
  - String phone
  - LocalDateTime createdDate
  - LocalDateTime updateDate
- Apply appropriate validation annotations

#### 2.2 Create BeerOrderDto
- Implement a BeerOrderDto record with the following properties:
  - Integer id
  - Integer version
  - String orderStatus
  - LocalDateTime createdDate
  - LocalDateTime updateDate
  - Integer customerId
  - List<OrderLineDto> orderLines
- Apply appropriate validation annotations

#### 2.3 Create OrderLineDto
- Implement an OrderLineDto record with the following properties:
  - Integer id
  - Integer orderQuantity
  - Integer beerId
  - String beerName (for display purposes)
- Apply appropriate validation annotations

#### 2.4 Create Command Objects
- Implement a CreateBeerOrderCommand record for order creation:
  ```java
  public record CreateBeerOrderCommand(
      @NotNull Integer customerId,
      @NotEmpty List<OrderLineDto> orderLines
  ) {}
  ```

### 3. Mapper Implementation

#### 3.1 Create CustomerMapper
- Implement a MapStruct mapper for Customer <-> CustomerDto conversion
- Ignore appropriate fields when mapping from DTO to entity

#### 3.2 Create BeerOrderMapper
- Implement a MapStruct mapper for BeerOrder <-> BeerOrderDto conversion
- Handle the relationship with Customer and OrderLine entities
- Ignore appropriate fields when mapping from DTO to entity

#### 3.3 Create OrderLineMapper
- Implement a MapStruct mapper for OrderLine <-> OrderLineDto conversion
- Handle the relationship with Beer and BeerOrder entities
- Ignore appropriate fields when mapping from DTO to entity

### 4. Repository Implementation

#### 4.1 Create CustomerRepository
- Implement a Spring Data JPA repository for Customer entities

#### 4.2 Create BeerOrderRepository
- Implement a Spring Data JPA repository for BeerOrder entities
- Include methods to find orders by customer

#### 4.3 Create OrderLineRepository
- Implement a Spring Data JPA repository for OrderLine entities
- Include methods to find order lines by beer and/or order

### 5. Service Implementation

#### 5.1 Create CustomerService
- Implement a service interface and implementation for Customer CRUD operations
- Methods should accept and return DTOs
- Apply appropriate transaction annotations

#### 5.2 Create BeerOrderService
- Implement a service interface and implementation for BeerOrder CRUD operations
- Include methods for:
  - Creating a new order
  - Retrieving orders (by ID, by customer, all)
  - Updating order status
  - Canceling/deleting orders
- Methods should accept and return DTOs
- Apply appropriate transaction annotations
- Ensure proper relationship management

#### 5.3 Create OrderLineService (if needed)
- Consider whether a separate service for OrderLine is necessary
- If implemented, follow the same patterns as other services

### 6. Controller Implementation

#### 6.1 Create CustomerController
- Implement a RESTful controller for Customer resources
- Follow REST API design principles
- Use appropriate HTTP methods and status codes
- Apply validation to request DTOs

#### 6.2 Create BeerOrderController
- Implement a RESTful controller for BeerOrder resources
- Include endpoints for:
  - POST /api/v1/orders - Create a new order
  - GET /api/v1/orders - Get all orders
  - GET /api/v1/orders/{id} - Get order by ID
  - GET /api/v1/customers/{id}/orders - Get orders by customer
  - PUT /api/v1/orders/{id} - Update order
  - DELETE /api/v1/orders/{id} - Delete order
- Follow REST API design principles
- Use appropriate HTTP methods and status codes
- Apply validation to request DTOs

### 7. Testing

#### 7.1 Unit Tests
- Create unit tests for all service implementations
- Mock dependencies as appropriate

#### 7.2 Integration Tests
- Create integration tests for repositories
- Use Testcontainers for database testing

#### 7.3 Controller Tests
- Create controller tests using MockMvc
- Test all endpoints with various scenarios (success, validation failure, not found, etc.)

## Technical Constraints
- Follow the Spring Boot guidelines provided in the project
- Use constructor injection for dependencies
- Use package-private visibility when possible
- Define clear transaction boundaries
- Disable Open Session in View
- Separate web layer from persistence layer using DTOs
- Follow REST API design principles
- Use command objects for business operations
- Implement centralized exception handling
- Use appropriate fetch types for relationships (LAZY for collections)
- Initialize collections to empty collections rather than null
- Use appropriate cascade types

## Deliverables
1. JPA entities with proper relationships
2. DTO classes for all entities
3. Mapper interfaces for entity-DTO conversion
4. Repository interfaces for data access
5. Service interfaces and implementations for business logic
6. RESTful controllers for API endpoints
7. Comprehensive tests for all components