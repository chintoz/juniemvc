# Task List for Adding DTOs to Beer API

## 1. Create DTO Classes
- [x] 1.1. Create a new package `es.menasoft.juniemvc.models`
- [x] 1.2. Implement a `BeerDto` record with the following properties:
  - [x] 1.2.1. Integer id
  - [x] 1.2.2. Integer version
  - [x] 1.2.3. String beerName
  - [x] 1.2.4. String beerStyle
  - [x] 1.2.5. String upc
  - [x] 1.2.6. Integer quantityOnHand
  - [x] 1.2.7. BigDecimal price
  - [x] 1.2.8. LocalDateTime createdDate
  - [x] 1.2.9. LocalDateTime updateDate
- [x] 1.3. Apply appropriate validation annotations:
  - [x] 1.3.1. `@NotBlank` for string fields (beerName, beerStyle, upc)
  - [x] 1.3.2. `@NotNull` for required numeric fields (price, quantityOnHand)
  - [x] 1.3.3. `@Positive` for price and quantityOnHand

## 2. Implement MapStruct Mapper
- [x] 2.1. Add MapStruct dependencies to pom.xml
- [x] 2.2. Create a new package `es.menasoft.juniemvc.mappers`
- [x] 2.3. Implement a `BeerMapper` interface using MapStruct:
  - [x] 2.3.1. Add `@Mapper(componentModel = "spring")` annotation
  - [x] 2.3.2. Create `beerToBeerDto(Beer beer)` method
  - [x] 2.3.3. Create `beerDtoToBeer(BeerDto beerDto)` method with appropriate field mappings
  - [x] 2.3.4. Ensure the mapper ignores id, createdDate, and updateDate when converting from DTO to entity

## 3. Update Service Layer
- [x] 3.1. Update BeerService Interface
  - [x] 3.1.1. Modify `saveBeer` to accept and return DTOs
  - [x] 3.1.2. Modify `getBeerById` to return DTOs
  - [x] 3.1.3. Modify `getAllBeers` to return DTOs
  - [x] 3.1.4. Modify `updateBeer` to accept and return DTOs
  - [x] 3.1.5. Modify `deleteBeer` method signature if needed
- [x] 3.2. Update BeerServiceImpl
  - [x] 3.2.1. Inject the `BeerMapper` into the service implementation
  - [x] 3.2.2. Update `saveBeer` to use the mapper for converting between entities and DTOs
  - [x] 3.2.3. Update `getBeerById` to use the mapper
  - [x] 3.2.4. Update `getAllBeers` to use the mapper
  - [x] 3.2.5. Update `updateBeer` to use the mapper
  - [x] 3.2.6. Update `deleteBeer` to use the mapper if needed
  - [x] 3.2.7. Apply `@Transactional(readOnly = true)` for query methods (getBeerById, getAllBeers)
  - [x] 3.2.8. Apply `@Transactional` for data-modifying methods (saveBeer, updateBeer, deleteBeer)

## 4. Update Controller Layer
- [x] 4.1. Update BeerController
  - [x] 4.1.1. Modify `createBeer` to accept and return DTOs
  - [x] 4.1.2. Modify `getBeerById` to return DTOs
  - [x] 4.1.3. Modify `getAllBeers` to return DTOs
  - [x] 4.1.4. Modify `updateBeer` to accept and return DTOs
  - [x] 4.1.5. Modify `deleteBeer` if needed
  - [x] 4.1.6. Apply validation to request DTOs using `@Valid` annotation
  - [x] 4.1.7. Maintain the same HTTP status codes and response structure

## 5. Update Tests
- [x] 5.1. Update BeerControllerTest
  - [x] 5.1.1. Update test data setup to use DTOs instead of entities
  - [x] 5.1.2. Update service mock to work with DTOs
  - [x] 5.1.3. Update request/response expectations to match the DTO structure
  - [x] 5.1.4. Add validation tests to ensure input validation is working correctly
- [x] 5.2. Update BeerServiceImplTest
  - [x] 5.2.1. Update test data setup to use DTOs
  - [x] 5.2.2. Add mapper mock to convert between entities and DTOs
  - [x] 5.2.3. Update repository mock to work with entities
  - [x] 5.2.4. Update assertions to match the DTO structure

## 6. Verify Implementation
- [x] 6.1. Run Tests
  - [x] 6.1.1. Run all tests to ensure they pass with the new DTO implementation
  - [x] 6.1.2. Verify that validation is working correctly
- [x] 6.2. Manual Testing
  - [x] 6.2.1. Test the API endpoints manually to ensure they work as expected
  - [x] 6.2.2. Verify that validation errors are handled correctly

## 7. Documentation and Cleanup
- [x] 7.1. Update any existing documentation to reflect the DTO implementation
- [x] 7.2. Review code for any remaining entity exposures in the API
- [x] 7.3. Ensure consistent error handling across all endpoints
- [x] 7.4. Perform final code review to ensure adherence to Spring Boot best practices
