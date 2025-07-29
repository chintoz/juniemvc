# Prompt Variables
Apply the following variables to placeholders in the prompt. Placeholders are denoted by `${variable}` syntax.

# Placeholders Definitions.
The following key value pairs are used to replace placeholders in the prompt. Format variable defines the variable name and
value defines the value to replace the placeholder with. Defined as `variable name` = `value` pairs in the following list:

* entity_name = `Foo`
* property_name = `bar`

## Task Description
Your task is to add a property for an existing entity. This task should take into consideration flyway migrations, add the property at entity level and DTO level, update documentation and verify all the tests are passing. 
Once all the Java code is implemented, the OpenAPI documentation should be updated to reflect the changes. Use guidelines from the file `.junie/guidelines.md`.

### Task Steps
* Inspect the existing entity `${entity_name}` and identify where to add the new property `${property_name}`.
* Add the new property `${property_name}` to the entity `${entity_name}`.
* Add the new property `${property_name}` to the DTO for the entity `${entity_name}`.
* Add the new property `${property_name}` to the OpenAPI documentation for the entity `${entity_name}`.
* Create a Flyway migration script to add the new property `${property_name}` to the database table for the entity `${entity_name}`.
* Update the OpenAPI documentation to reflect the changes made to the entity `${entity_name}`.
* Verify the OpenAPI documentation is valid.
* Update the unit tests for the entity `${entity_name}` to cover the new property `${property_name}`.
* Create new unit tests for the entity `${entity_name}` to cover the new property `${property_name}`.
* Verify the updated unit tests are passing.
* Update the service layer to support the new property `${property_name}`.
* Update the repository layer to support the new property `${property_name}`.
* Update the controller layer to support the new property `${property_name}`.
* Update the OpenAPI documentation to reflect the changes made to the controller for the entity `${entity_name}`.
* Verify the OpenAPI documentation is valid.
* Update the parameters of the controller for the entity `${entity_name}` to include the new property `${property_name}`.
* Verify the OpenAPI documentation is valid.
* Update the unit tests for the controller for the entity `${entity_name}` to cover the new property `${property_name}`.
Ç