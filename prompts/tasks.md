# React Frontend Integration Task List

## Part 1: Foundation and Setup

1. [ ] Create the recommended project structure
   - [ ] Create `src/main/frontend` directory
   - [ ] Set up `.gitignore` to exclude `node_modules` and compiled assets

2. [ ] Initialize React + TypeScript project
   - [ ] Run `npm create vite@latest . -- --template react-ts` in the frontend directory
   - [ ] Review and understand the generated files

3. [ ] Install required dependencies
   - [ ] Install core dependencies (React, React Router, Axios)
   - [ ] Install TypeScript type definitions
   - [ ] Install UI and utility libraries
   - [ ] Install development dependencies

4. [ ] Set up UI frameworks
   - [ ] Initialize and configure Tailwind CSS
   - [ ] Initialize and configure Shadcn UI
   - [ ] Add essential Shadcn UI components (button, card, table, etc.)

## Part 2: Build Integration and Configuration

5. [ ] Configure Maven integration
   - [ ] Add `frontend-maven-plugin` to `pom.xml`
   - [ ] Configure Node.js and npm versions
   - [ ] Set up executions for install, build, and test
   - [ ] Update `maven-clean-plugin` to clean compiled frontend assets

6. [ ] Configure Vite for Spring Boot integration
   - [ ] Create/update `vite.config.ts` with proper settings
   - [ ] Configure API proxy for development
   - [ ] Set output directory to `../resources/static`
   - [ ] Create `.env` file for environment variables

## Part 3: Building a Robust Frontend Application

7. [ ] Enhance developer experience
   - [ ] Create ESLint configuration (`.eslintrc.cjs`)
   - [ ] Create Prettier configuration (`.prettierrc`)
   - [ ] Add scripts to `package.json` for development, build, test, lint, and format

8. [ ] Implement end-to-end type safety
   - [ ] Install OpenAPI TypeScript Codegen
   - [ ] Add script to generate TypeScript types from OpenAPI spec
   - [ ] Run the script to generate initial types

9. [ ] Create core API services
   - [ ] Create reusable Axios instance with interceptors
   - [ ] Implement type-safe service for Beer API
   - [ ] Implement services for other resources (Customers, Beer Orders)

10. [ ] Develop custom React hooks
    - [ ] Create hook for Beer data management
    - [ ] Create hooks for other resources
    - [ ] Implement state management for loading, error handling, and pagination

11. [ ] Build UI components
    - [ ] Create Beer list component with filtering and pagination
    - [ ] Create detail views for resources
    - [ ] Create forms for creating and updating resources
    - [ ] Implement navigation and routing

## Part 4: Running and Testing

12. [ ] Set up testing infrastructure
    - [ ] Configure Jest with TypeScript
    - [ ] Create setup file for testing
    - [ ] Write unit tests for components
    - [ ] Write tests for hooks and services

13. [ ] Document development workflow
    - [ ] Document how to run backend and frontend in development mode
    - [ ] Document how to build the entire application
    - [ ] Document testing procedures

14. [ ] Update project guidelines
    - [ ] Add frontend development guidelines to project documentation
    - [ ] Include information about project structure, workflow, and code style

15. [ ] Perform final integration testing
    - [ ] Test development workflow
    - [ ] Test production build
    - [ ] Verify that frontend and backend work together correctly