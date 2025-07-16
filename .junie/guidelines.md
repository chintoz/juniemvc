# Spring Boot Guidelines

## 1. Prefer Constructor Injection over Field/Setter Injection
* Declare all the mandatory dependencies as `final` fields and inject them through the constructor.
* Spring will auto-detect if there is only one constructor, no need to add `@Autowired` on the constructor.
* Avoid field/setter injection in production code.

## 2. Prefer package-private over public for Spring components
* Declare Controllers, their request-handling methods, `@Configuration` classes and `@Bean` methods with default (package-private) visibility whenever possible. There's no obligation to make everything `public`.

## 3. Organize Configuration with Typed Properties
* Group application-specific configuration properties with a common prefix in `application.properties` or `.yml`.
* Bind them to `@ConfigurationProperties` classes with validation annotations so that the application will fail fast if the configuration is invalid.
* Prefer environment variables instead of profiles for passing different configuration properties for different environments.

## 4. Define Clear Transaction Boundaries
* Define each Service-layer method as a transactional unit.
* Annotate query-only methods with `@Transactional(readOnly = true)`.
* Annotate data-modifying methods with `@Transactional`.
* Limit the code inside each transaction to the smallest necessary scope.


## 5. Disable Open Session in View Pattern
* While using Spring Data JPA, disable the Open Session in View filter by setting ` spring.jpa.open-in-view=false` in `application.properties/yml.`

## 6. Separate Web Layer from Persistence Layer
* Don't expose entities directly as responses in controllers.
* Define explicit request and response record (DTO) classes instead.
* Apply Jakarta Validation annotations on your request records to enforce input rules.

## 7. Follow REST API Design Principles
* **Versioned, resource-oriented URLs:** Structure your endpoints as `/api/v{version}/resources` (e.g. `/api/v1/orders`).
* **Consistent patterns for collections and sub-resources:** Keep URL conventions uniform (for example, `/posts` for posts collection and `/posts/{slug}/comments` for comments of a specific post).
* **Explicit HTTP status codes via ResponseEntity:** Use `ResponseEntity<T>` to return the correct status (e.g. 200 OK, 201 Created, 404 Not Found) along with the response body.
* Use pagination for collection resources that may contain an unbounded number of items.
* The JSON payload must use a JSON object as a top-level data structure to allow for future extension.
* Use snake_case or camelCase for JSON property names consistently.

## 8. Use Command Objects for Business Operations
* Create purpose-built command records (e.g., `CreateOrderCommand`) to wrap input data.
* Accept these commands in your service methods to drive creation or update workflows.

## 9. Centralize Exception Handling
* Define a global handler class annotated with `@ControllerAdvice` (or `@RestControllerAdvice` for REST APIs) using `@ExceptionHandler` methods to handle specific exceptions.
* Return consistent error responses. Consider using the ProblemDetails response format ([RFC 9457](https://www.rfc-editor.org/rfc/rfc9457)).

## 10. Actuator
* Expose only essential actuator endpoints (such as `/health`, `/info`, `/metrics`) without requiring authentication. All the other actuator endpoints must be secured.

## 11. Internationalization with ResourceBundles
* Externalize all user-facing text such as labels, prompts, and messages into ResourceBundles rather than embedding them in code.

## 12. Use Testcontainers for integration tests
* Spin up real services (databases, message brokers, etc.) in your integration tests to mirror production environments.

## 13. Use random port for integration tests
* When writing integration tests, start the application on a random available port to avoid port conflicts by annotating the test class with:

    ```java
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    ```

## 14. Logging
* **Use a proper logging framework.**  
  Never use `System.out.println()` for application logging. Rely on SLF4J (or a compatible abstraction) and your chosen backend (Logback, Log4j2, etc.).

* **Protect sensitive data.**  
  Ensure that no credentials, personal information, or other confidential details ever appear in log output.

* **Guard expensive log calls.**  
  When building verbose messages at `DEBUG` or `TRACE` level, especially those involving method calls or complex string concatenations, wrap them in a level check or use suppliers:

```java
if (logger.isDebugEnabled()) {
    logger.debug("Detailed state: {}", computeExpensiveDetails());
}

// using Supplier/Lambda expression
logger.atDebug()
	.setMessage("Detailed state: {}")
	.addArgument(() -> computeExpensiveDetails())
    .log();
```

## 15. Database Migrations with Flyway
* Spring Boot automatically runs Flyway migrations on startup when Flyway is on the classpath.
* Place migration scripts in the default location: `src/main/resources/db/migration`.
* Follow the Flyway version naming convention: `V<version>__<description>.sql` (e.g., `V1__create_tables.sql`, `V2__add_indexes.sql`).
* Version numbers should be sequential and can include dots and underscores (e.g., `V1_1__`, `V1.2__`).
* Use repeatable migrations for scripts that can be run multiple times with prefix `R__` (e.g., `R__create_views.sql`).
* Configure Flyway properties in `application.properties` or `application.yml` with the `spring.flyway` prefix.

## 16. OpenAPI Specification Guidelines
* **API Documentation Structure:** The OpenAPI specification is organized in a modular way with the main file `openapi.yaml` referencing other files for paths, components, and schemas.
  * The main file contains general API information, tags, servers, and references to path and component files.
  * Path operations are defined in separate files in the `paths/` directory.
  * Components (schemas, responses, headers, etc.) are defined in separate files in the `components/` directory.

* **File Naming Conventions:**
  * **Path Operation Files:** Named based on the API path they represent, with path parameters replaced by underscore notation.
    * Example: `/users/{username}` is defined in `paths/users_{username}.yaml`
    * Example: `/user` is defined in `paths/user.yaml`
  * **Component Files:** Named after the component they define and placed in the appropriate subdirectory.
    * Example: `User` schema is defined in `components/schemas/User.yaml`
    * Example: `Problem` response is defined in `components/responses/Problem.yaml`

* **Component Definitions:**
  * **Schemas:** Define data models in separate files in the `components/schemas/` directory.
    * Use `$ref` to reference other schemas (e.g., `$ref: './Email.yaml'`).
  * **Responses:** Define reusable responses in separate files in the `components/responses/` directory.
    * Use `$ref` to reference schemas (e.g., `$ref: '../schemas/Problem.yaml'`).
  * **Headers, Parameters, etc.:** Follow the same pattern of defining in separate files and referencing with `$ref`.

* **Testing the OpenAPI Specification:**
  * Run `npm test` in the `openapi/` directory to validate the OpenAPI specification.
  * This command runs `redocly lint` to check for errors and inconsistencies.
  * To preview the documentation, run `npm start` which uses `redocly preview-docs`.
  * To bundle the specification into a single file, run `npm run build` which creates `dist/bundle.yaml`.
