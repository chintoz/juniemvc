# Requirements: Implementing DTOs for the Beer API

## Background
The current implementation directly exposes JPA entities in the REST API, which violates the separation of concerns principle and creates tight coupling between the persistence layer and the API contract. This project aims to implement Data Transfer Objects (DTOs) to properly separate these concerns.

## Objectives
1. Separate the web layer from the persistence layer using DTOs
2. Implement proper mapping between entities and DTOs
3. Update the service layer to work with DTOs
4. Ensure all REST endpoints use DTOs for input and output

## Detailed Requirements

### 1. Create DTO Classes
- Create a new package `es.menasoft.juniemvc.models` for DTO classes
- Implement a `BeerDto` record with the following properties:
  - Integer id
  - Integer version
  - String beerName
  - String beerStyle
  - String upc
  - Integer quantityOnHand
  - BigDecimal price
  - LocalDateTime createdDate
  - LocalDateTime updateDate
- Apply appropriate validation annotations from Jakarta Validation:
  - `@NotBlank` for string fields that shouldn't be empty (beerName, beerStyle, upc)
  - `@NotNull` for required numeric fields (price, quantityOnHand)
  - `@Positive` for price and quantityOnHand

### 2. Implement MapStruct Mapper
- Create a new package `es.menasoft.juniemvc.mappers` for mapper interfaces
- Create a `BeerMapper` interface using MapStruct:
  ```java
  @Mapper(componentModel = "spring")
  public interface BeerMapper {
      BeerDto beerToBeerDto(Beer beer);
      
      @Mapping(target = "id", ignore = true)
      @Mapping(target = "createdDate", ignore = true)
      @Mapping(target = "updateDate", ignore = true)
      Beer beerDtoToBeer(BeerDto beerDto);
  }
  ```
- Ensure the mapper ignores id, createdDate, and updateDate when converting from DTO to entity

### 3. Update Service Layer
- Modify the `BeerService` interface to accept and return DTOs instead of entities:
  - `BeerDto saveBeer(BeerDto beerDto)`
  - `Optional<BeerDto> getBeerById(Integer id)`
  - `List<BeerDto> getAllBeers()`
  - `Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto)`
  - `boolean deleteBeer(Integer id)`
- Update the `BeerServiceImpl` to use the mapper for converting between entities and DTOs
- Apply appropriate transaction annotations:
  - `@Transactional(readOnly = true)` for query methods
  - `@Transactional` for data-modifying methods

### 4. Update Controller Layer
- Modify the `BeerController` to accept and return DTOs instead of entities
- Ensure all endpoints use the updated service methods
- Apply validation to request DTOs using `@Valid` annotation
- Maintain the same HTTP status codes and response structure

### 5. Testing
- Update existing tests to work with DTOs instead of entities
- Add validation tests to ensure input validation is working correctly

## Technical Constraints
- Use Project Lombok for the DTO classes
- Use MapStruct for object mapping
- Follow the existing project structure and naming conventions
- Adhere to the Spring Boot guidelines in the project, particularly:
  - Guideline #6: Separate Web Layer from Persistence Layer
  - Guideline #8: Use Command Objects for Business Operations

## Deliverables
1. DTO classes in the models package
2. Mapper interfaces in the mappers package
3. Updated service layer that works with DTOs
4. Updated controller layer that accepts and returns DTOs
5. Updated tests that verify the functionality with DTOs