# JunieMVC Developer Guidelines

## Project Overview
JunieMVC is a Spring Boot application that demonstrates a RESTful API for managing beer data. It follows the MVC (Model-View-Controller) architecture pattern and uses Spring Data JPA for database operations.

## Tech Stack
- **Java 21**: The application uses Java 21 as the programming language
- **Spring Boot 3.5.0**: Framework for creating stand-alone, production-grade Spring applications
- **Spring Data JPA**: Simplifies data access layer implementation
- **Hibernate**: ORM implementation for JPA
- **H2 Database**: In-memory database for development and testing
- **Flyway**: Database migration tool
- **Lombok**: Reduces boilerplate code
- **MapStruct**: Java bean mapping framework
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework for testing
- **Maven**: Build and dependency management tool

## Project Structure
The project follows a standard Spring Boot application structure:

```
src/
├── main/
│   ├── java/
│   │   └── es/menasoft/juniemvc/
│   │       ├── controllers/    # REST API endpoints
│   │       ├── entities/       # JPA entity classes
│   │       ├── repositories/   # Spring Data JPA repositories
│   │       ├── services/       # Business logic layer
│   │       └── JuniemvcApplication.java  # Main application class
│   └── resources/
│       └── application.properties  # Application configuration
└── test/
    ├── java/
    │   └── es/menasoft/juniemvc/
    │       ├── controllers/    # Controller tests
    │       ├── repositories/   # Repository tests
    │       ├── services/       # Service tests
    │       └── JuniemvcApplicationTests.java
    └── resources/
        └── application.properties  # Test configuration
```

## Architecture Guidelines
1. **Follow the layered architecture**:
   - Controllers: Handle HTTP requests and responses
   - Services: Implement business logic
   - Repositories: Handle data access operations

2. **Dependency Injection**:
   - Use constructor injection with Lombok's `@RequiredArgsConstructor`
   - Avoid field injection with `@Autowired`

3. **Interface-based design**:
   - Define service interfaces before implementations
   - Program to interfaces, not implementations

## Running the Application
1. **Build the application**:
   ```
   ./mvnw clean install
   ```

2. **Run the application**:
   ```
   ./mvnw spring-boot:run
   ```

3. **Access the H2 console**:
   - URL: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:testdb
   - Username: sa
   - Password: password

## Testing Guidelines
1. **Run all tests**:
   ```
   ./mvnw test
   ```

2. **Test structure**:
   - Use JUnit 5 for testing
   - Follow the BDD style (Given/When/Then)
   - Use MockMvc for controller tests
   - Use @DataJpaTest for repository tests
   - Mock dependencies for unit tests

3. **Test naming convention**:
   - Method names should describe the test scenario
   - Example: `testCreateBeer`, `testGetBeerByIdNotFound`

## Best Practices
1. **Code Style**:
   - Follow Java naming conventions
   - Use meaningful names for classes, methods, and variables
   - Add Javadoc comments for public methods

2. **Error Handling**:
   - Use appropriate HTTP status codes
   - Return meaningful error messages
   - Handle exceptions properly

3. **Database**:
   - Use Flyway for database migrations
   - Follow JPA best practices
   - Use appropriate fetch types and cascade operations

4. **API Design**:
   - Follow RESTful principles
   - Use appropriate HTTP methods
   - Version your APIs (e.g., /api/v1/beers)