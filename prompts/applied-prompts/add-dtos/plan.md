# Implementation Plan: Adding DTOs to Beer API

## Overview
This plan outlines the steps needed to implement Data Transfer Objects (DTOs) for the Beer API, separating the web layer from the persistence layer according to the requirements.

## 1. Create DTO Classes

### 1.1 Create BeerDto Record
- Create a new package `es.menasoft.juniemvc.models`
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
- Apply appropriate validation annotations:
  - `@NotBlank` for string fields (beerName, beerStyle, upc)
  - `@NotNull` for required numeric fields (price, quantityOnHand)
  - `@Positive` for price and quantityOnHand

## 2. Implement MapStruct Mapper

### 2.1 Create BeerMapper Interface
- Create a new package `es.menasoft.juniemvc.mappers`
- Implement a `BeerMapper` interface using MapStruct:
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

## 3. Update Service Layer

### 3.1 Update BeerService Interface
- Modify the `BeerService` interface to accept and return DTOs:
  - `BeerDto saveBeer(BeerDto beerDto)`
  - `Optional<BeerDto> getBeerById(Integer id)`
  - `List<BeerDto> getAllBeers()`
  - `Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto)`
  - `boolean deleteBeer(Integer id)`

### 3.2 Update BeerServiceImpl
- Inject the `BeerMapper` into the service implementation
- Update all methods to use the mapper for converting between entities and DTOs
- Apply transaction annotations:
  - `@Transactional(readOnly = true)` for query methods (getBeerById, getAllBeers)
  - `@Transactional` for data-modifying methods (saveBeer, updateBeer, deleteBeer)
- Implementation details:
  - saveBeer: Convert DTO to entity, save entity, convert result back to DTO
  - getBeerById: Find entity by ID, convert to DTO if found
  - getAllBeers: Get all entities, convert each to DTO
  - updateBeer: Find entity by ID, update fields from DTO, save entity, convert result back to DTO
  - deleteBeer: Find entity by ID, delete if found

## 4. Update Controller Layer

### 4.1 Update BeerController
- Modify all endpoints to accept and return DTOs instead of entities
- Apply validation to request DTOs using `@Valid` annotation
- Maintain the same HTTP status codes and response structure
- Implementation details:
  - createBeer: Accept BeerDto, call service.saveBeer, return DTO with CREATED status
  - getBeerById: Call service.getBeerById, return DTO with OK status or NOT_FOUND
  - getAllBeers: Call service.getAllBeers, return list of DTOs with OK status
  - updateBeer: Accept BeerDto, call service.updateBeer, return DTO with OK status or NOT_FOUND
  - deleteBeer: Call service.deleteBeer, return NO_CONTENT status if successful or NOT_FOUND

## 5. Update Tests

### 5.1 Update BeerControllerTest
- Update test data setup to use DTOs instead of entities
- Update service mock to work with DTOs
- Update request/response expectations to match the DTO structure
- Add validation tests to ensure input validation is working correctly

### 5.2 Update BeerServiceImplTest
- Update test data setup to use DTOs
- Add mapper mock to convert between entities and DTOs
- Update repository mock to work with entities (repository still works with entities)
- Update assertions to match the DTO structure

## 6. Verify Implementation

### 6.1 Run Tests
- Run all tests to ensure they pass with the new DTO implementation
- Verify that validation is working correctly

### 6.2 Manual Testing
- Test the API endpoints manually to ensure they work as expected
- Verify that validation errors are handled correctly

## Timeline and Dependencies

1. Create DTO Classes (Day 1)
2. Implement MapStruct Mapper (Day 1)
3. Update Service Layer (Day 2)
   - Depends on: DTO Classes, MapStruct Mapper
4. Update Controller Layer (Day 2)
   - Depends on: Updated Service Layer
5. Update Tests (Day 3)
   - Depends on: All previous steps
6. Verify Implementation (Day 3)
   - Depends on: All previous steps

## Risks and Mitigations

### Risks
1. **Breaking API Changes**: The API contract might change when switching to DTOs.
   - **Mitigation**: Ensure DTOs have the same structure as entities to maintain backward compatibility.

2. **Test Failures**: Existing tests might fail due to the changes.
   - **Mitigation**: Update tests incrementally and run them frequently to catch issues early.

3. **Performance Impact**: Adding mapping between entities and DTOs might impact performance.
   - **Mitigation**: Use MapStruct for efficient mapping and consider caching if necessary.

## Conclusion
This plan outlines the steps needed to implement DTOs for the Beer API, separating the web layer from the persistence layer. By following this plan, we will improve the architecture of the application and adhere to Spring Boot best practices.