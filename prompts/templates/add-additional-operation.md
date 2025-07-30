# Prompt Variables
Apply the following variables to placeholders in the prompt. Placeholders are denoted by `${variable}` syntax.

# Placeholders Definitions.
The following key value pairs are used to replace placeholders in the prompt. Format variable defines the variable name and
value defines the value to replace the placeholder with. Defined as `variable name` = `value` pairs in the following list:

* controller_name = `FooController`

## Task Description
Your task is to implement a new operation PATCH in ${controller_name}. Once all the Java code is implemented,
the OpenAPI documentation should be updated to reflect the changes. Use guidelines from the file `.junie/guidelines.md`.

### Task Steps
* Implement the new operation in the controller class `${controller_name}`. 
* The new operation should behave as PATH Restful operation, meaning it should be idempotent and should not change the state of the resource.
* The operation should be implemented in the controller class `${controller_name}`.
* It should be created a DTO without not null or not blank constraints.
* It should allow for the update for one or more properties of the resource while others are null.
* It should be added an update method in MapStruct mapper ignoring null properties.
* The operation should be documented in the OpenAPI documentation.
* The operation should be tested in the test class `${controller_name}Test`.
* The operation should be tested in the test class `${controller_name}Test` using the `@Test` annotation.
* The operation should be propagated to all the layers of the application, including the service layer and the repository layer.
* The operation should be tested in service and repository layers
* At the end, it should be ensured all the tests pass after the implementation.
