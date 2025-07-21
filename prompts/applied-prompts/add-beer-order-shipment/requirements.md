# Requirements: Implementing Beer Order Shipment Functionality

## Background
The current implementation provides CRUD operations for Beer, Customer, and BeerOrder entities but lacks the ability to track shipments for beer orders. This enhancement aims to implement a complete beer order shipment tracking system with proper entity relationships and all necessary layers for CRUD operations.

## Objectives
1. Implement a new JPA entity for BeerOrderShipment
2. Create DTOs for the new entity
3. Establish a one-to-many relationship between BeerOrder and BeerOrderShipment
4. Implement repositories, services, mappers, and controllers for the new entity
5. Ensure proper relationship management between entities
6. Provide a complete API for beer order shipment management

## Detailed Requirements

### 1. Entity Implementation

#### 1.1 Create BeerOrderShipment Entity
- Implement a BeerOrderShipment entity with the following properties:
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

#### 1.2 Update BeerOrder Entity
- Update the existing BeerOrder entity to include a relationship with BeerOrderShipment:
  ```java
  @OneToMany(mappedBy = "beerOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @Builder.Default
  private Set<BeerOrderShipment> shipments = new HashSet<>();
  ```
- Add helper methods for managing the bidirectional relationship:
  ```java
  public BeerOrderShipment addShipment(BeerOrderShipment shipment) {
      shipments.add(shipment);
      shipment.setBeerOrder(this);
      return shipment;
  }

  public BeerOrderShipment removeShipment(BeerOrderShipment shipment) {
      shipments.remove(shipment);
      shipment.setBeerOrder(null);
      return shipment;
  }
  ```

### 2. DTO Implementation

#### 2.1 Create BeerOrderShipmentDto
- Implement a BeerOrderShipmentDto record with the following properties:
  - Integer id
  - Integer version
  - LocalDate shipmentDate
  - String carrier
  - String trackingNumber
  - LocalDateTime createdDate
  - LocalDateTime updateDate
  - Integer beerOrderId
- Apply appropriate validation annotations:
  ```java
  public record BeerOrderShipmentDto(
      // Read-only
      Integer id,
      Integer version,
      
      @NotNull(message = "Shipment date is required")
      LocalDate shipmentDate,
      
      @NotBlank(message = "Carrier is required")
      String carrier,
      
      @NotBlank(message = "Tracking number is required")
      String trackingNumber,
      
      // Read-only create date
      LocalDateTime createdDate,
      
      // Read-only update date
      LocalDateTime updateDate,
      
      @NotNull(message = "Beer order ID is required")
      Integer beerOrderId
  ) {}
  ```

#### 2.2 Create Command Objects
- Implement a CreateBeerOrderShipmentCommand record for shipment creation:
  ```java
  public record CreateBeerOrderShipmentCommand(
      @NotNull(message = "Beer order ID is required")
      Integer beerOrderId,
      
      @NotNull(message = "Shipment date is required")
      LocalDate shipmentDate,
      
      @NotBlank(message = "Carrier is required")
      String carrier,
      
      @NotBlank(message = "Tracking number is required")
      String trackingNumber
  ) {}
  ```

### 3. Mapper Implementation

#### 3.1 Create BeerOrderShipmentMapper
- Implement a MapStruct mapper for BeerOrderShipment <-> BeerOrderShipmentDto conversion
- Handle the relationship with BeerOrder entity
- Ignore appropriate fields when mapping from DTO to entity
- Include methods for mapping from CreateBeerOrderShipmentCommand to BeerOrderShipment

### 4. Repository Implementation

#### 4.1 Create BeerOrderShipmentRepository
- Implement a Spring Data JPA repository for BeerOrderShipment entities
- Include methods to find shipments by beer order:
  ```java
  public interface BeerOrderShipmentRepository extends JpaRepository<BeerOrderShipment, Integer> {
      List<BeerOrderShipment> findAllByBeerOrder(BeerOrder beerOrder);
      List<BeerOrderShipment> findAllByBeerOrderId(Integer beerOrderId);
  }
  ```

### 5. Service Implementation

#### 5.1 Create BeerOrderShipmentService
- Implement a service interface and implementation for BeerOrderShipment CRUD operations
- Include methods for:
  - Creating a new shipment
  - Retrieving shipments (by ID, by beer order, all)
  - Updating shipment details
  - Deleting shipments
- Methods should accept and return DTOs
- Apply appropriate transaction annotations
- Ensure proper relationship management
- Service interface should include:
  ```java
  public interface BeerOrderShipmentService {
      BeerOrderShipmentDto createShipment(CreateBeerOrderShipmentCommand command);
      Optional<BeerOrderShipmentDto> getShipmentById(Integer id);
      List<BeerOrderShipmentDto> getAllShipments();
      List<BeerOrderShipmentDto> getShipmentsByBeerOrderId(Integer beerOrderId);
      Optional<BeerOrderShipmentDto> updateShipment(Integer id, BeerOrderShipmentDto shipmentDto);
      boolean deleteShipment(Integer id);
  }
  ```

### 6. Controller Implementation

#### 6.1 Create BeerOrderShipmentController
- Implement a RESTful controller for BeerOrderShipment resources
- Include endpoints for:
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

### 7. Testing

#### 7.1 Unit Tests
- Create unit tests for BeerOrderShipmentService implementation
- Mock dependencies as appropriate

#### 7.2 Integration Tests
- Create integration tests for BeerOrderShipmentRepository
- Use Testcontainers for database testing

#### 7.3 Controller Tests
- Create controller tests for BeerOrderShipmentController using MockMvc
- Test all endpoints with various scenarios (success, validation failure, not found, etc.)

### 8. OpenAPI Documentation

#### 8.1 Create OpenAPI Schema for BeerOrderShipmentDto
- Create a YAML file in the openapi/components/schemas directory
- Define the schema for BeerOrderShipmentDto

#### 8.2 Create OpenAPI Schema for CreateBeerOrderShipmentCommand
- Create a YAML file in the openapi/components/schemas directory
- Define the schema for CreateBeerOrderShipmentCommand

#### 8.3 Create OpenAPI Path Operations
- Create YAML files in the openapi/paths directory for each endpoint
- Define the operations, parameters, request bodies, and responses

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
1. BeerOrderShipment entity with proper relationship to BeerOrder
2. Updated BeerOrder entity with relationship to BeerOrderShipment
3. DTO classes for BeerOrderShipment
4. Mapper interface for entity-DTO conversion
5. Repository interface for data access
6. Service interface and implementation for business logic
7. RESTful controller for API endpoints
8. Comprehensive tests for all components
9. OpenAPI documentation for the new endpoints