# Task List for Adding DTOs to Beer API

## 1. Create DTO Classes
- [ ] 1.1. Create a new package `es.menasoft.juniemvc.models`
- [ ] 1.2. Implement a `BeerDto` record with the following properties:
  - [ ] 1.2.1. Integer id
  - [ ] 1.2.2. Integer version
  - [ ] 1.2.3. String beerName
  - [ ] 1.2.4. String beerStyle
  - [ ] 1.2.5. String upc
  - [ ] 1.2.6. Integer quantityOnHand
  - [ ] 1.2.7. BigDecimal price
  - [ ] 1.2.8. LocalDateTime createdDate
  - [ ] 1.2.9. LocalDateTime updateDate
- [ ] 1.3. Apply appropriate validation annotations:
  - [ ] 1.3.1. `@NotBlank` for string fields (beerName, beerStyle, upc)
  - [ ] 1.3.2. `@NotNull` for required numeric fields (price, quantityOnHand)
  - [ ] 1.3.3. `@Positive` for price and quantityOnHand

## 2. Implement MapStruct Mapper
- [ ] 2.1. Add MapStruct dependencies to pom.xml
- [ ] 2.2. Create a new package `es.menasoft.juniemvc.mappers`
- [ ] 2.3. Implement a `BeerMapper` interface using MapStruct:
  - [ ] 2.3.1. Add `@Mapper(componentModel = "spring")` annotation
  - [ ] 2.3.2. Create `beerToBeerDto(Beer beer)` method
  - [ ] 2.3.3. Create `beerDtoToBeer(BeerDto beerDto)` method with appropriate field mappings
  - [ ] 2.3.4. Ensure the mapper ignores id, createdDate, and updateDate when converting from DTO to entity

## 3. Update Service Layer
- [ ] 3.1. Update BeerService Interface
  - [ ] 3.1.1. Modify `saveBeer` to accept and return DTOs
  - [ ] 3.1.2. Modify `getBeerById` to return DTOs
  - [ ] 3.1.3. Modify `getAllBeers` to return DTOs
  - [ ] 3.1.4. Modify `updateBeer` to accept and return DTOs
  - [ ] 3.1.5. Modify `deleteBeer` method signature if needed
- [ ] 3.2. Update BeerServiceImpl
  - [ ] 3.2.1. Inject the `BeerMapper` into the service implementation
  - [ ] 3.2.2. Update `saveBeer` to use the mapper for converting between entities and DTOs
  - [ ] 3.2.3. Update `getBeerById` to use the mapper
  - [ ] 3.2.4. Update `getAllBeers` to use the mapper
  - [ ] 3.2.5. Update `updateBeer` to use the mapper
  - [ ] 3.2.6. Update `deleteBeer` to use the mapper if needed
  - [ ] 3.2.7. Apply `@Transactional(readOnly = true)` for query methods (getBeerById, getAllBeers)
  - [ ] 3.2.8. Apply `@Transactional` for data-modifying methods (saveBeer, updateBeer, deleteBeer)

## 4. Update Controller Layer
- [ ] 4.1. Update BeerController
  - [ ] 4.1.1. Modify `createBeer` to accept and return DTOs
  - [ ] 4.1.2. Modify `getBeerById` to return DTOs
  - [ ] 4.1.3. Modify `getAllBeers` to return DTOs
  - [ ] 4.1.4. Modify `updateBeer` to accept and return DTOs
  - [ ] 4.1.5. Modify `deleteBeer` if needed
  - [ ] 4.1.6. Apply validation to request DTOs using `@Valid` annotation
  - [ ] 4.1.7. Maintain the same HTTP status codes and response structure

## 5. Update Tests
- [ ] 5.1. Update BeerControllerTest
  - [ ] 5.1.1. Update test data setup to use DTOs instead of entities
  - [ ] 5.1.2. Update service mock to work with DTOs
  - [ ] 5.1.3. Update request/response expectations to match the DTO structure
  - [ ] 5.1.4. Add validation tests to ensure input validation is working correctly
- [ ] 5.2. Update BeerServiceImplTest
  - [ ] 5.2.1. Update test data setup to use DTOs
  - [ ] 5.2.2. Add mapper mock to convert between entities and DTOs
  - [ ] 5.2.3. Update repository mock to work with entities
  - [ ] 5.2.4. Update assertions to match the DTO structure

## 6. Verify Implementation
- [ ] 6.1. Run Tests
  - [ ] 6.1.1. Run all tests to ensure they pass with the new DTO implementation
  - [ ] 6.1.2. Verify that validation is working correctly
- [ ] 6.2. Manual Testing
  - [ ] 6.2.1. Test the API endpoints manually to ensure they work as expected
  - [ ] 6.2.2. Verify that validation errors are handled correctly

## 7. Documentation and Cleanup
- [ ] 7.1. Update any existing documentation to reflect the DTO implementation
- [ ] 7.2. Review code for any remaining entity exposures in the API
- [ ] 7.3. Ensure consistent error handling across all endpoints
- [ ] 7.4. Perform final code review to ensure adherence to Spring Boot best practices