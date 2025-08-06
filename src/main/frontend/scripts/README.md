# JunieMVC Frontend Test Scripts

This directory contains scripts for testing the JunieMVC frontend integration with the Spring Boot backend.

## Available Scripts

- `test-dev-workflow.sh`: Tests the development workflow
- `test-production-build.sh`: Tests the production build process
- `test-integration.sh`: Tests the integration between the frontend and backend

## Usage

Before running the scripts, make sure they are executable:

```bash
chmod +x test-dev-workflow.sh test-production-build.sh test-integration.sh
```

### Testing Development Workflow

This script checks if the development environment is properly set up and if the development server can be started.

```bash
./test-dev-workflow.sh
```

### Testing Production Build

This script builds the frontend for production and verifies that the build artifacts are correctly generated.

```bash
./test-production-build.sh
```

### Testing Integration

This script tests the integration between the frontend and backend by building the entire application, starting it, and verifying that the API endpoints and frontend static resources are accessible.

```bash
./test-integration.sh
```

## Requirements

- Node.js v22.16.0 or later
- npm v11.4.0 or later
- Java 21 or later
- Maven
- curl

## Notes

- The scripts should be run from the `src/main/frontend/scripts` directory
- The integration test script will build and start the entire application, so make sure no other application is running on port 8080
- The scripts will exit with a non-zero status code if any test fails