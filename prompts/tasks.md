# React Frontend Integration Task List

## Part 1: Foundation and Setup

1. [x] Create the recommended project structure
   - [x] Create `src/main/frontend` directory
   - [x] Set up `.gitignore` to exclude `node_modules` and compiled assets

2. [x] Initialize React + TypeScript project
   - [x] Run `npm create vite@latest . -- --template react-ts` in the frontend directory
   - [x] Review and understand the generated files

3. [x] Install required dependencies
   - [x] Install core dependencies (React, React Router, Axios)
   - [x] Install TypeScript type definitions
   - [x] Install UI and utility libraries
   - [x] Install development dependencies

4. [x] Set up UI frameworks
   - [x] Initialize and configure Tailwind CSS
   - [x] Initialize and configure Shadcn UI
   - [x] Add essential Shadcn UI components (button, card, table, etc.)

## Part 2: Build Integration and Configuration

5. [x] Configure Maven integration
   - [x] Add `frontend-maven-plugin` to `pom.xml`
   - [x] Configure Node.js and npm versions
   - [x] Set up executions for install, build, and test
   - [x] Update `maven-clean-plugin` to clean compiled frontend assets

6. [x] Configure Vite for Spring Boot integration
   - [x] Create/update `vite.config.ts` with proper settings
   - [x] Configure API proxy for development
   - [x] Set output directory to `../resources/static`
   - [x] Create `.env` file for environment variables

## Part 3: Building a Robust Frontend Application

7. [x] Enhance developer experience
   - [x] Create ESLint configuration (`.eslintrc.cjs`)
   - [x] Create Prettier configuration (`.prettierrc`)
   - [x] Add scripts to `package.json` for development, build, test, lint, and format

8. [x] Implement end-to-end type safety
   - [x] Install OpenAPI TypeScript Codegen
   - [x] Add script to generate TypeScript types from OpenAPI spec
   - [x] Run the script to generate initial types

9. [x] Create core API services
   - [x] Create reusable Axios instance with interceptors
   - [x] Implement type-safe service for Beer API
   - [x] Implement services for other resources (Customers, Beer Orders)

10. [x] Develop custom React hooks
    - [x] Create hook for Beer data management
    - [x] Create hooks for other resources
    - [x] Implement state management for loading, error handling, and pagination

11. [x] Build UI components
    - [x] Create Beer list component with filtering and pagination
    - [x] Create detail views for resources
    - [x] Create forms for creating and updating resources
    - [x] Implement navigation and routing

## Part 4: Running and Testing

12. [x] Set up testing infrastructure
    - [x] Configure Jest with TypeScript
    - [x] Create setup file for testing
    - [x] Write unit tests for components
    - [x] Write tests for hooks and services

13. [x] Document development workflow
    - [x] Document how to run backend and frontend in development mode
    - [x] Document how to build the entire application
    - [x] Document testing procedures

14. [x] Update project guidelines
    - [x] Add frontend development guidelines to project documentation
    - [x] Include information about project structure, workflow, and code style

15. [x] Perform final integration testing
    - [x] Test development workflow
    - [x] Test production build
    - [x] Verify that frontend and backend work together correctly