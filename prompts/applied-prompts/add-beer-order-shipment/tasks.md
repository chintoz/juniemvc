# Beer Order Shipment Implementation Tasks

## 1. Entity Implementation

### 1.1 Create BeerOrderShipment Entity
- [x] Create BeerOrderShipment class in es.menasoft.juniemvc.entities package
- [x] Add required fields (id, version, shipmentDate, carrier, trackingNumber, createdDate, updateDate, beerOrder)
- [x] Apply JPA annotations (@Entity, @Table, @Id, @GeneratedValue, etc.)
- [x] Apply Lombok annotations (@Getter, @Setter, @Builder, etc.)
- [x] Implement proper relationship with BeerOrder entity (@ManyToOne)

### 1.2 Update BeerOrder Entity
- [x] Add one-to-many relationship with BeerOrderShipment
- [x] Initialize the shipments collection to an empty HashSet
- [x] Add addShipment(BeerOrderShipment shipment) helper method
- [x] Add removeShipment(BeerOrderShipment shipment) helper method

## 2. DTO Implementation

### 2.1 Create BeerOrderShipmentDto
- [x] Create BeerOrderShipmentDto record in es.menasoft.juniemvc.models package
- [x] Add required fields (id, version, shipmentDate, carrier, trackingNumber, createdDate, updateDate, beerOrderId)
- [x] Apply validation annotations (@NotNull, @NotBlank, etc.)

### 2.2 Create CreateBeerOrderShipmentCommand
- [x] Create CreateBeerOrderShipmentCommand record in es.menasoft.juniemvc.models package
- [x] Add required fields (beerOrderId, shipmentDate, carrier, trackingNumber)
- [x] Apply validation annotations (@NotNull, @NotBlank, etc.)

## 3. Mapper Implementation

### 3.1 Create BeerOrderShipmentMapper
- [x] Create BeerOrderShipmentMapper interface in es.menasoft.juniemvc.mappers package
- [x] Add @Mapper annotation with appropriate component model
- [x] Define method to convert BeerOrderShipment to BeerOrderShipmentDto
- [x] Define method to convert BeerOrderShipmentDto to BeerOrderShipment
- [x] Define method to convert CreateBeerOrderShipmentCommand to BeerOrderShipment
- [x] Add mapping for BeerOrder relationship
- [x] Configure appropriate field mappings and ignores

## 4. Repository Implementation

### 4.1 Create BeerOrderShipmentRepository
- [x] Create BeerOrderShipmentRepository interface in es.menasoft.juniemvc.repositories package
- [x] Extend JpaRepository<BeerOrderShipment, Integer>
- [x] Add findAllByBeerOrder(BeerOrder beerOrder) method
- [x] Add findAllByBeerOrderId(Integer beerOrderId) method

## 5. Service Implementation

### 5.1 Create BeerOrderShipmentService Interface
- [x] Create BeerOrderShipmentService interface in es.menasoft.juniemvc.services package
- [x] Define createShipment(CreateBeerOrderShipmentCommand command) method
- [x] Define getShipmentById(Integer id) method
- [x] Define getAllShipments() method
- [x] Define getShipmentsByBeerOrderId(Integer beerOrderId) method
- [x] Define updateShipment(Integer id, BeerOrderShipmentDto shipmentDto) method
- [x] Define deleteShipment(Integer id) method

### 5.2 Create BeerOrderShipmentServiceImpl
- [x] Create BeerOrderShipmentServiceImpl class in es.menasoft.juniemvc.services package
- [x] Implement BeerOrderShipmentService interface
- [x] Add constructor with required dependencies (repositories, mappers)
- [x] Implement createShipment method with @Transactional annotation
- [x] Implement getShipmentById method with @Transactional(readOnly = true) annotation
- [x] Implement getAllShipments method with @Transactional(readOnly = true) annotation
- [x] Implement getShipmentsByBeerOrderId method with @Transactional(readOnly = true) annotation
- [x] Implement updateShipment method with @Transactional annotation
- [x] Implement deleteShipment method with @Transactional annotation
- [x] Add proper error handling and validation

## 6. Controller Implementation

### 6.1 Create BeerOrderShipmentController
- [x] Create BeerOrderShipmentController class in es.menasoft.juniemvc.controllers package
- [x] Add constructor with required dependencies (service)
- [x] Implement POST endpoint for creating a shipment (/api/v1/shipments)
- [x] Implement GET endpoint for retrieving all shipments (/api/v1/shipments)
- [x] Implement GET endpoint for retrieving a shipment by ID (/api/v1/shipments/{id})
- [x] Implement GET endpoint for retrieving shipments by beer order (/api/v1/orders/{id}/shipments)
- [x] Implement PUT endpoint for updating a shipment (/api/v1/shipments/{id})
- [x] Implement DELETE endpoint for deleting a shipment (/api/v1/shipments/{id})
- [x] Add proper validation, error handling, and HTTP status codes
- [x] Add comprehensive JavaDoc comments

## 7. Testing

### 7.1 Unit Tests

#### 7.1.1 BeerOrderShipmentServiceImplTest
- [x] Create BeerOrderShipmentServiceImplTest class
- [x] Set up test environment with mocks for dependencies
- [x] Test createShipment method (success and failure scenarios)
- [x] Test getShipmentById method (found and not found scenarios)
- [x] Test getAllShipments method
- [x] Test getShipmentsByBeerOrderId method
- [x] Test updateShipment method (success and failure scenarios)
- [x] Test deleteShipment method (success and failure scenarios)

### 7.2 Integration Tests

#### 7.2.1 BeerOrderShipmentRepositoryTest
- [x] Create BeerOrderShipmentRepositoryTest class
- [x] Set up test environment with Testcontainers
- [x] Test save and findById methods
- [x] Test findAllByBeerOrder method
- [x] Test findAllByBeerOrderId method

### 7.3 Controller Tests

#### 7.3.1 BeerOrderShipmentControllerTest
- [x] Create BeerOrderShipmentControllerTest class
- [x] Set up test environment with MockMvc and mocked service
- [x] Test POST endpoint (valid and invalid requests)
- [x] Test GET all endpoint
- [x] Test GET by ID endpoint (found and not found scenarios)
- [x] Test GET by beer order ID endpoint
- [x] Test PUT endpoint (valid and invalid requests, found and not found scenarios)
- [x] Test DELETE endpoint (success and failure scenarios)

## 8. OpenAPI Documentation

### 8.1 Create OpenAPI Schema for BeerOrderShipmentDto
- [x] Create BeerOrderShipmentDto.yaml in openapi/components/schemas directory
- [x] Define properties with appropriate types and descriptions
- [x] Add required properties
- [x] Add example values

### 8.2 Create OpenAPI Schema for CreateBeerOrderShipmentCommand
- [x] Create CreateBeerOrderShipmentCommand.yaml in openapi/components/schemas directory
- [x] Define properties with appropriate types and descriptions
- [x] Add required properties
- [x] Add example values

### 8.3 Create OpenAPI Path Operations
- [x] Create shipments.yaml in openapi/paths directory for POST and GET all endpoints
- [x] Create shipments_{id}.yaml in openapi/paths directory for GET by ID, PUT, and DELETE endpoints
- [x] Create orders_{id}_shipments.yaml in openapi/paths directory for GET shipments by beer order endpoint
- [x] Define operations, parameters, request bodies, and responses
- [x] Reference appropriate schemas