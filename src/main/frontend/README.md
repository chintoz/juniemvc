# JunieMVC Frontend

This is the React frontend for the JunieMVC application, a Spring Boot application with a React frontend for managing beers, customers, and orders.

## Development Workflow

### Prerequisites

- Node.js (v22.16.0 or later)
- npm (v11.4.0 or later)
- Java 21 (for running the backend)
- Maven (for building the entire application)

### Running in Development Mode

To run the application in development mode, you need to start both the Spring Boot backend and the Vite development server:

1. **Start the Spring Boot backend:**

   ```bash
   # From the project root directory
   ./mvnw spring-boot:run
   ```

   This will start the backend server on http://localhost:8080.

2. **Start the Vite development server:**

   ```bash
   # From the frontend directory
   cd src/main/frontend
   npm run dev
   ```

   This will start the frontend development server on http://localhost:3000.

The Vite development server is configured to proxy API requests to the Spring Boot backend, so you can develop the frontend with hot reloading while still interacting with the backend.

### Building the Application

To build the entire application (both frontend and backend) for production:

```bash
# From the project root directory
./mvnw clean package
```

This will:
1. Clean the project
2. Install Node.js and npm (if not already installed)
3. Install frontend dependencies
4. Build the React frontend
5. Run frontend tests
6. Compile the Java code
7. Package everything into a single JAR file

The resulting JAR file will be located in the `target` directory and can be run with:

```bash
java -jar target/juniemvc-0.0.1-SNAPSHOT.jar
```

The application will be available at http://localhost:8080.

### Testing

#### Running Frontend Tests

To run the frontend tests:

```bash
# From the frontend directory
cd src/main/frontend
npm test
```

This will run all Jest tests for the frontend.

#### Running Backend Tests

To run the backend tests:

```bash
# From the project root directory
./mvnw test
```

#### Running All Tests

To run all tests (both frontend and backend):

```bash
# From the project root directory
./mvnw clean test
```

## Project Structure

The frontend code is organized as follows:

```
src/main/frontend/
├── node_modules/         # Node.js dependencies (git-ignored)
├── public/               # Static assets
├── src/                  # React source code
│   ├── components/       # React components
│   │   ├── beer/         # Beer-related components
│   │   └── ui/           # UI components (from Shadcn UI)
│   ├── hooks/            # Custom React hooks
│   ├── pages/            # Page components
│   ├── services/         # API service layer
│   ├── types/            # TypeScript type definitions
│   │   └── api/          # Generated API types
│   ├── App.tsx           # Main App component
│   └── main.tsx          # Entry point
├── .eslintrc.cjs         # ESLint configuration
├── .prettierrc           # Prettier configuration
├── jest.config.js        # Jest configuration
├── package.json          # NPM dependencies and scripts
├── tsconfig.json         # TypeScript configuration
└── vite.config.ts        # Vite configuration
```

## Available Scripts

In the frontend directory, you can run:

- `npm run dev`: Starts the development server
- `npm run build`: Builds the frontend for production
- `npm run lint`: Lints the code
- `npm run format`: Formats the code with Prettier
- `npm test`: Runs the tests
- `npm run generate-api-types`: Generates TypeScript types from the OpenAPI specification

## Technologies Used

- React 19
- TypeScript
- Vite
- React Router
- Axios
- Tailwind CSS
- Shadcn UI
- Jest
- Testing Library
