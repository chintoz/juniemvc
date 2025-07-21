# Implementation Plan: Beer Order Shipment Functionality

## 1. Entity Implementation

### 1.1 Create BeerOrderShipment Entity
- Create a new entity class in `es.menasoft.juniemvc.entities` package
- Implement all required fields:
  - Integer id (primary key)
  - Integer version (for optimistic locking)
  - LocalDate shipmentDate
  - String carrier
  - String trackingNumber
  - LocalDateTime createdDate
  - LocalDateTime updateDate
  - BeerOrder beerOrder (many-to-one relationship)
- Apply appropriate JPA annotations
- Apply appropriate Lombok annotations (@Getter, @Setter, @Builder, etc.)
- Ensure proper relationship with BeerOrder entity

### 1.2 Update BeerOrder Entity
- Add a one-to-many relationship with BeerOrderShipment
- Initialize the collection to an empty HashSet
- Add helper methods for managing the bidirectional relationship:
  - addShipment(BeerOrderShipment shipment)
  - removeShipment(BeerOrderShipment shipment)

## 2. DTO Implementation

### 2.1 Create BeerOrderShipmentDto
- Create a record in `es.menasoft.juniemvc.models` package
- Include all required fields:
  - Integer id
  - Integer version
  - LocalDate shipmentDate
  - String carrier
  - String trackingNumber
  - LocalDateTime createdDate
  - LocalDateTime updateDate
  - Integer beerOrderId
- Apply appropriate validation annotations

### 2.2 Create CreateBeerOrderShipmentCommand
- Create a record in `es.menasoft.juniemvc.models` package
- Include required fields for shipment creation:
  - Integer beerOrderId
  - LocalDate shipmentDate
  - String carrier
  - String trackingNumber
- Apply appropriate validation annotations

## 3. Mapper Implementation

### 3.1 Create BeerOrderShipmentMapper
- Create a MapStruct mapper interface in `es.menasoft.juniemvc.mappers` package
- Define methods for:
  - BeerOrderShipment to BeerOrderShipmentDto conversion
  - BeerOrderShipmentDto to BeerOrderShipment conversion
  - CreateBeerOrderShipmentCommand to BeerOrderShipment conversion
- Handle the relationship with BeerOrder entity
- Ignore appropriate fields when mapping from DTO to entity
- Follow the existing mapper patterns in the project

## 4. Repository Implementation

### 4.1 Create BeerOrderShipmentRepository
- Create a Spring Data JPA repository interface in `es.menasoft.juniemvc.repositories` package
- Extend JpaRepository<BeerOrderShipment, Integer>
- Add methods to find shipments by beer order:
  - List<BeerOrderShipment> findAllByBeerOrder(BeerOrder beerOrder)
  - List<BeerOrderShipment> findAllByBeerOrderId(Integer beerOrderId)

## 5. Service Implementation

### 5.1 Create BeerOrderShipmentService Interface
- Create a service interface in `es.menasoft.juniemvc.services` package
- Define methods for:
  - Creating a new shipment
  - Retrieving shipments (by ID, by beer order, all)
  - Updating shipment details
  - Deleting shipments
- Methods should accept and return DTOs

### 5.2 Create BeerOrderShipmentServiceImpl
- Create a service implementation in `es.menasoft.juniemvc.services` package
- Implement the BeerOrderShipmentService interface
- Inject required repositories and mappers
- Apply appropriate transaction annotations
- Ensure proper relationship management
- Follow the existing service implementation patterns in the project

## 6. Controller Implementation

### 6.1 Create BeerOrderShipmentController
- Create a RESTful controller in `es.menasoft.juniemvc.controllers` package
- Define endpoints for:
  - POST /api/v1/shipments - Create a new shipment
  - GET /api/v1/shipments - Get all shipments
  - GET /api/v1/shipments/{id} - Get shipment by ID
  - GET /api/v1/orders/{id}/shipments - Get shipments by beer order
  - PUT /api/v1/shipments/{id} - Update shipment
  - DELETE /api/v1/shipments/{id} - Delete shipment
- Follow REST API design principles
- Use appropriate HTTP methods and status codes
- Apply validation to request DTOs
- Include comprehensive JavaDoc comments
- Follow the existing controller patterns in the project

## 7. Testing

### 7.1 Unit Tests

#### 7.1.1 BeerOrderShipmentServiceImplTest
- Create unit tests for BeerOrderShipmentServiceImpl
- Test all service methods
- Mock dependencies (repositories, mappers)
- Test success scenarios and error scenarios
- Follow the existing unit test patterns in the project

### 7.2 Integration Tests

#### 7.2.1 BeerOrderShipmentRepositoryTest
- Create integration tests for BeerOrderShipmentRepository
- Test all repository methods
- Use Testcontainers for database testing
- Follow the existing integration test patterns in the project

### 7.3 Controller Tests

#### 7.3.1 BeerOrderShipmentControllerTest
- Create controller tests for BeerOrderShipmentController
- Test all endpoints
- Test success scenarios, validation failures, not found scenarios
- Use MockMvc for testing
- Follow the existing controller test patterns in the project

## 8. OpenAPI Documentation

### 8.1 Create OpenAPI Schema for BeerOrderShipmentDto
- Create a YAML file in the `openapi/components/schemas` directory
- Define the schema for BeerOrderShipmentDto
- Include all properties with appropriate types and descriptions
- Follow the existing schema patterns in the project

### 8.2 Create OpenAPI Schema for CreateBeerOrderShipmentCommand
- Create a YAML file in the `openapi/components/schemas` directory
- Define the schema for CreateBeerOrderShipmentCommand
- Include all properties with appropriate types and descriptions
- Follow the existing schema patterns in the project

### 8.3 Create OpenAPI Path Operations
- Create YAML files in the `openapi/paths` directory for each endpoint
- Define the operations, parameters, request bodies, and responses
- Reference the appropriate schemas
- Follow the existing path operation patterns in the project

## 9. Implementation Approach

The implementation will follow these steps:

1. Start with the entity layer:
   - Create BeerOrderShipment entity
   - Update BeerOrder entity

2. Implement the data access layer:
   - Create BeerOrderShipmentRepository

3. Implement the service layer:
   - Create DTOs
   - Create Mapper
   - Create Service interface and implementation

4. Implement the web layer:
   - Create Controller

5. Add OpenAPI documentation:
   - Create schemas
   - Create path operations

6. Implement tests:
   - Unit tests
   - Integration tests
   - Controller tests

This approach ensures that each layer is built on top of a solid foundation, following the layered architecture pattern.

## 10. Technical Considerations

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