# Adding a React Frontend to a Spring Boot Application

This guide provides step-by-step instructions for Java developers to add a React frontend to an existing Spring Boot application. The final result will be a single, self-contained Spring Boot JAR that includes the compiled React UI.

## Part 1: Foundation and Setup

### 1. Introduction & Final Goal

The objective of this guide is to seamlessly integrate a modern React frontend into your Spring Boot project. By following these steps, you'll achieve:

- A single Maven build that produces a self-contained Spring Boot JAR with the React UI included
- A development environment that allows for efficient frontend and backend development
- Type-safe API interactions between your React frontend and Spring Boot backend

This guide assumes your Spring Boot backend already provides RESTful APIs for resources like Beers, Customers, and Beer Orders, as is the case with this project.

### 2. Recommended Project Structure

The recommended project structure places the frontend source code in `src/main/frontend`:

```
juniemvc/
├── pom.xml                        # Maven project configuration
├── src/
│   ├── main/
│   │   ├── frontend/              # React frontend source code
│   │   │   ├── node_modules/      # Node.js dependencies (git-ignored)
│   │   │   ├── public/            # Static assets
│   │   │   ├── src/               # React source code
│   │   │   │   ├── components/    # React components
│   │   │   │   ├── hooks/         # Custom React hooks
│   │   │   │   ├── services/      # API services
│   │   │   │   ├── types/         # TypeScript type definitions
│   │   │   │   └── ...
│   │   │   ├── package.json       # Node.js dependencies and scripts
│   │   │   ├── tsconfig.json      # TypeScript configuration
│   │   │   └── vite.config.ts     # Vite configuration
│   │   ├── java/                  # Java source code
│   │   └── resources/
│   │       ├── static/            # Compiled frontend assets (git-ignored)
│   │       └── ...
│   └── test/                      # Tests
└── ...
```

This structure has several advantages:
- It keeps the frontend code within the Maven project structure
- It allows for a single build process that includes both frontend and backend
- It separates the frontend source code from the compiled assets
- It follows Maven conventions for project organization

### 3. Initializing the React + TypeScript Project

First, create the frontend directory and initialize a new Vite project with React and TypeScript:

```bash
# Create the frontend directory
mkdir -p src/main/frontend

# Navigate to the frontend directory
cd src/main/frontend

# Initialize a new Vite project with React and TypeScript
npm create vite@latest . -- --template react-ts
```

This will generate the following key files:
- `package.json`: Node.js dependencies and scripts
- `tsconfig.json`: TypeScript configuration
- `vite.config.ts`: Vite configuration
- `src/main.tsx`: Entry point for the React application
- `src/App.tsx`: Main React component
- `public/`: Directory for static assets

### 4. Installing Dependencies and Setting up UI Framework

Install the required dependencies:

```bash
# Navigate to the frontend directory (if not already there)
cd src/main/frontend

# Install core dependencies
npm install react@19.1.0 react-dom@19.1.0 react-router-dom@7.6.36 axios@1.10.0

# Install type definitions
npm install --save-dev @types/react@19.1.0 @types/react-dom@19.1.0 @types/node@24.0.1

# Install UI and utilities
npm install @radix-ui/react-primitive@3.2.1 tailwindcss@4.1.10 tw-animate-css@1.3.4 tailwind-merge@3.3.1 postcss@8.5.5 autoprefixer@10.4.20 clsx@2.1.1 class-variance-authority@0.7.1 lucide-react@0.515.0

# Install development dependencies
npm install --save-dev typescript@5.8.3 @vitejs/plugin-react@4.5.2 jest@30.0.0 @types/jest@29.5.14 @testing-library/jest-dom@6.6.3 @testing-library/react@16.3.0 eslint prettier
```

#### Setting up Tailwind CSS

Initialize Tailwind CSS:

```bash
npx tailwindcss init -p
```

Update the `tailwind.config.js` file:

```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

Add Tailwind directives to `src/index.css`:

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

#### Setting up Shadcn UI

Initialize Shadcn UI:

```bash
npx shadcn-ui@latest init
```

When prompted, choose the following options:
- Would you like to use TypeScript? Yes
- Which style would you like to use? Default
- Which color would you like to use as base color? Slate
- Where is your tailwind.config.js located? tailwind.config.js
- Configure the import alias for components: @/components
- Configure the import alias for utils: @/lib/utils
- Are you using React Server Components? No

Add Shadcn UI components as needed:

```bash
npx shadcn-ui@latest add button
npx shadcn-ui@latest add card
npx shadcn-ui@latest add table
npx shadcn-ui@latest add input
npx shadcn-ui@latest add select
npx shadcn-ui@latest add pagination
```

## Part 2: Build Integration and Configuration

### 1. Maven and Vite Integration

#### Configuration: frontend-maven-plugin

Add the `frontend-maven-plugin` to your `pom.xml` to integrate the frontend build with Maven:

```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.15.1</version>
    <configuration>
        <nodeVersion>v22.16.0</nodeVersion>
        <npmVersion>11.4.0</npmVersion>
        <workingDirectory>src/main/frontend</workingDirectory>
        <installDirectory>target</installDirectory>
    </configuration>
    <executions>
        <!-- Install Node and NPM -->
        <execution>
            <id>install-node-and-npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
            <phase>generate-resources</phase>
        </execution>
        
        <!-- Install npm dependencies -->
        <execution>
            <id>npm-install</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <phase>generate-resources</phase>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
        
        <!-- Build frontend -->
        <execution>
            <id>npm-build</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <phase>prepare-package</phase>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
        
        <!-- Run tests -->
        <execution>
            <id>npm-test</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <phase>test</phase>
            <configuration>
                <arguments>test</arguments>
                <skip>${skipTests}</skip>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### Maven Clean Plugin

Update the `maven-clean-plugin` to remove the compiled frontend assets from `src/main/resources/static`:

```xml
<plugin>
    <artifactId>maven-clean-plugin</artifactId>
    <configuration>
        <filesets>
            <fileset>
                <directory>src/main/resources/static</directory>
                <includes>
                    <include>**/*</include>
                </includes>
            </fileset>
        </filesets>
    </configuration>
</plugin>
```

### 2. Vite Configuration for Spring Boot

Create or update the `vite.config.ts` file in the `src/main/frontend` directory:

```typescript
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve } from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': resolve(__dirname, './src'),
    },
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: '../resources/static',
    emptyOutDir: true,
    sourcemap: true,
  },
  envPrefix: 'REACT_APP_',
});
```

Create a `.env` file in the `src/main/frontend` directory for environment variables:

```
REACT_APP_API_URL=/api
```

## Part 3: Building a Robust Frontend Application

### 1. Enhancing Developer Experience (DevEx)

#### ESLint Configuration

Create a `.eslintrc.cjs` file in the `src/main/frontend` directory:

```javascript
module.exports = {
  root: true,
  env: { browser: true, es2020: true },
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:react-hooks/recommended',
    'plugin:react/recommended',
    'plugin:react/jsx-runtime',
  ],
  ignorePatterns: ['dist', '.eslintrc.cjs'],
  parser: '@typescript-eslint/parser',
  plugins: ['react-refresh'],
  rules: {
    'react-refresh/only-export-components': [
      'warn',
      { allowConstantExport: true },
    ],
    'react/prop-types': 'off',
  },
  settings: {
    react: {
      version: 'detect',
    },
  },
};
```

#### Prettier Configuration

Create a `.prettierrc` file in the `src/main/frontend` directory:

```json
{
  "semi": true,
  "tabWidth": 2,
  "printWidth": 100,
  "singleQuote": true,
  "trailingComma": "es5",
  "bracketSpacing": true,
  "jsxBracketSameLine": false
}
```

#### Update package.json Scripts

Add the following scripts to your `package.json`:

```json
"scripts": {
  "dev": "vite",
  "build": "tsc && vite build",
  "lint": "eslint . --ext ts,tsx --report-unused-disable-directives --max-warnings 0",
  "format": "prettier --write \"src/**/*.{ts,tsx,css}\"",
  "preview": "vite preview",
  "test": "jest",
  "generate-api-types": "openapi-typescript-codegen --input ../../../openapi/openapi/openapi.yaml --output ./src/types/api"
}
```

### 2. End-to-End Type Safety with OpenAPI

Install the OpenAPI TypeScript Codegen:

```bash
npm install --save-dev openapi-typescript-codegen
```

Run the script to generate TypeScript types from the OpenAPI specification:

```bash
npm run generate-api-types
```

This will generate TypeScript types in the `src/types/api` directory based on the OpenAPI specification.

### 3. Crafting the React Components

#### Type-Safe API Service

Create a reusable Axios instance in `src/services/api.ts`:

```typescript
import axios from 'axios';

const API_URL = import.meta.env.REACT_APP_API_URL || '/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for authentication if needed
api.interceptors.request.use(
  (config) => {
    // You can add auth token here if needed
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle common errors here
    return Promise.reject(error);
  }
);

export default api;
```

Create a beer service in `src/services/beerService.ts`:

```typescript
import api from './api';
import { Beer, BeerPage } from '../types/api';

export interface BeerFilter {
  beerName?: string;
  beerStyle?: string;
  page?: number;
  size?: number;
  sortField?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export const beerService = {
  getBeers: async (filter: BeerFilter = {}): Promise<BeerPage> => {
    const { beerName, beerStyle, page = 0, size = 20, sortField = 'id', sortDirection = 'ASC' } = filter;
    
    const params = new URLSearchParams();
    if (beerName) params.append('beerName', beerName);
    if (beerStyle) params.append('beerStyle', beerStyle);
    params.append('page', page.toString());
    params.append('size', size.toString());
    params.append('sortField', sortField);
    params.append('sortDirection', sortDirection);
    
    const response = await api.get<BeerPage>('/v1/beers', { params });
    return response.data;
  },
  
  getBeerById: async (id: number): Promise<Beer> => {
    const response = await api.get<Beer>(`/v1/beers/${id}`);
    return response.data;
  },
  
  createBeer: async (beer: Omit<Beer, 'id'>): Promise<Beer> => {
    const response = await api.post<Beer>('/v1/beers', beer);
    return response.data;
  },
  
  updateBeer: async (id: number, beer: Beer): Promise<Beer> => {
    const response = await api.put<Beer>(`/v1/beers/${id}`, beer);
    return response.data;
  },
  
  patchBeer: async (id: number, beer: Partial<Beer>): Promise<Beer> => {
    const response = await api.patch<Beer>(`/v1/beers/${id}`, beer);
    return response.data;
  },
  
  deleteBeer: async (id: number): Promise<void> => {
    await api.delete(`/v1/beers/${id}`);
  },
};
```

#### Custom Hooks

Create a custom hook for beers in `src/hooks/useBeer.ts`:

```typescript
import { useState, useEffect, useCallback } from 'react';
import { beerService, BeerFilter } from '../services/beerService';
import { Beer, BeerPage } from '../types/api';

export function useBeer() {
  const [beers, setBeers] = useState<Beer[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [filter, setFilter] = useState<BeerFilter>({
    page: 0,
    size: 20,
    sortField: 'id',
    sortDirection: 'ASC',
  });

  const fetchBeers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await beerService.getBeers(filter);
      setBeers(response.content);
      setTotalElements(response.totalElements);
      setTotalPages(response.totalPages);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An error occurred'));
    } finally {
      setLoading(false);
    }
  }, [filter]);

  useEffect(() => {
    fetchBeers();
  }, [fetchBeers]);

  const updateFilter = useCallback((newFilter: Partial<BeerFilter>) => {
    setFilter((prev) => ({ ...prev, ...newFilter }));
  }, []);

  const refreshBeers = useCallback(() => {
    fetchBeers();
  }, [fetchBeers]);

  return {
    beers,
    loading,
    error,
    totalElements,
    totalPages,
    filter,
    updateFilter,
    refreshBeers,
  };
}
```

#### UI Components

Create a beer list component in `src/components/BeerList.tsx`:

```typescript
import { useState } from 'react';
import { useBeer } from '../hooks/useBeer';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Select } from './ui/select';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from './ui/table';
import { Pagination } from './ui/pagination';

export function BeerList() {
  const {
    beers,
    loading,
    error,
    totalPages,
    filter,
    updateFilter,
    refreshBeers,
  } = useBeer();
  
  const [nameFilter, setNameFilter] = useState('');
  const [styleFilter, setStyleFilter] = useState('');
  
  const handleSearch = () => {
    updateFilter({
      beerName: nameFilter || undefined,
      beerStyle: styleFilter || undefined,
      page: 0, // Reset to first page on new search
    });
  };
  
  const handlePageChange = (page: number) => {
    updateFilter({ page });
  };
  
  const handleSortChange = (field: string) => {
    const direction = filter.sortField === field && filter.sortDirection === 'ASC' ? 'DESC' : 'ASC';
    updateFilter({ sortField: field, sortDirection: direction });
  };
  
  if (error) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Error</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Failed to load beers: {error.message}</p>
          <Button onClick={refreshBeers}>Retry</Button>
        </CardContent>
      </Card>
    );
  }
  
  return (
    <Card>
      <CardHeader>
        <CardTitle>Beers</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col gap-4">
          <div className="flex flex-wrap gap-4">
            <Input
              placeholder="Filter by name"
              value={nameFilter}
              onChange={(e) => setNameFilter(e.target.value)}
              className="max-w-xs"
            />
            <Input
              placeholder="Filter by style"
              value={styleFilter}
              onChange={(e) => setStyleFilter(e.target.value)}
              className="max-w-xs"
            />
            <Button onClick={handleSearch}>Search</Button>
          </div>
          
          {loading ? (
            <div>Loading...</div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead onClick={() => handleSortChange('id')} className="cursor-pointer">
                      ID {filter.sortField === 'id' && (filter.sortDirection === 'ASC' ? '↑' : '↓')}
                    </TableHead>
                    <TableHead onClick={() => handleSortChange('name')} className="cursor-pointer">
                      Name {filter.sortField === 'name' && (filter.sortDirection === 'ASC' ? '↑' : '↓')}
                    </TableHead>
                    <TableHead onClick={() => handleSortChange('style')} className="cursor-pointer">
                      Style {filter.sortField === 'style' && (filter.sortDirection === 'ASC' ? '↑' : '↓')}
                    </TableHead>
                    <TableHead onClick={() => handleSortChange('upc')} className="cursor-pointer">
                      UPC {filter.sortField === 'upc' && (filter.sortDirection === 'ASC' ? '↑' : '↓')}
                    </TableHead>
                    <TableHead onClick={() => handleSortChange('price')} className="cursor-pointer">
                      Price {filter.sortField === 'price' && (filter.sortDirection === 'ASC' ? '↑' : '↓')}
                    </TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {beers.map((beer) => (
                    <TableRow key={beer.id}>
                      <TableCell>{beer.id}</TableCell>
                      <TableCell>{beer.name}</TableCell>
                      <TableCell>{beer.style}</TableCell>
                      <TableCell>{beer.upc}</TableCell>
                      <TableCell>${beer.price?.toFixed(2)}</TableCell>
                      <TableCell>
                        <Button variant="outline" size="sm">View</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
              
              {totalPages > 1 && (
                <Pagination
                  currentPage={filter.page || 0}
                  totalPages={totalPages}
                  onPageChange={handlePageChange}
                />
              )}
            </>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
```

## Part 4: Running and Testing

### 1. Unit Testing the Frontend

#### Jest Configuration

Create a `jest.config.js` file in the `src/main/frontend` directory:

```javascript
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
  },
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'],
  testMatch: ['**/__tests__/**/*.ts?(x)', '**/?(*.)+(spec|test).ts?(x)'],
  transform: {
    '^.+\\.tsx?$': 'ts-jest',
  },
};
```

Create a `src/setupTests.ts` file:

```typescript
import '@testing-library/jest-dom';
```

#### Example Unit Test

Create a test for the BeerList component in `src/components/BeerList.test.tsx`:

```typescript
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BeerList } from './BeerList';
import { useBeer } from '../hooks/useBeer';

// Mock the custom hook
jest.mock('../hooks/useBeer');

const mockUseBeer = useBeer as jest.MockedFunction<typeof useBeer>;

describe('BeerList', () => {
  beforeEach(() => {
    // Default mock implementation
    mockUseBeer.mockReturnValue({
      beers: [
        { id: 1, name: 'Test Beer', style: 'IPA', upc: '123456', price: 9.99 },
        { id: 2, name: 'Another Beer', style: 'Stout', upc: '654321', price: 8.99 },
      ],
      loading: false,
      error: null,
      totalElements: 2,
      totalPages: 1,
      filter: {
        page: 0,
        size: 20,
        sortField: 'id',
        sortDirection: 'ASC',
      },
      updateFilter: jest.fn(),
      refreshBeers: jest.fn(),
    });
  });

  it('renders the beer list', () => {
    render(<BeerList />);
    
    // Check if the title is rendered
    expect(screen.getByText('Beers')).toBeInTheDocument();
    
    // Check if the beers are rendered
    expect(screen.getByText('Test Beer')).toBeInTheDocument();
    expect(screen.getByText('Another Beer')).toBeInTheDocument();
  });

  it('shows loading state', () => {
    mockUseBeer.mockReturnValue({
      beers: [],
      loading: true,
      error: null,
      totalElements: 0,
      totalPages: 0,
      filter: {
        page: 0,
        size: 20,
        sortField: 'id',
        sortDirection: 'ASC',
      },
      updateFilter: jest.fn(),
      refreshBeers: jest.fn(),
    });
    
    render(<BeerList />);
    
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  it('shows error state', () => {
    mockUseBeer.mockReturnValue({
      beers: [],
      loading: false,
      error: new Error('Failed to fetch'),
      totalElements: 0,
      totalPages: 0,
      filter: {
        page: 0,
        size: 20,
        sortField: 'id',
        sortDirection: 'ASC',
      },
      updateFilter: jest.fn(),
      refreshBeers: jest.fn(),
    });
    
    render(<BeerList />);
    
    expect(screen.getByText('Failed to load beers: Failed to fetch')).toBeInTheDocument();
  });

  it('handles search', async () => {
    const updateFilter = jest.fn();
    mockUseBeer.mockReturnValue({
      beers: [],
      loading: false,
      error: null,
      totalElements: 0,
      totalPages: 0,
      filter: {
        page: 0,
        size: 20,
        sortField: 'id',
        sortDirection: 'ASC',
      },
      updateFilter,
      refreshBeers: jest.fn(),
    });
    
    render(<BeerList />);
    
    // Fill in the search fields
    await userEvent.type(screen.getByPlaceholderText('Filter by name'), 'IPA');
    await userEvent.type(screen.getByPlaceholderText('Filter by style'), 'Ale');
    
    // Click the search button
    await userEvent.click(screen.getByText('Search'));
    
    // Check if updateFilter was called with the correct parameters
    expect(updateFilter).toHaveBeenCalledWith({
      beerName: 'IPA',
      beerStyle: 'Ale',
      page: 0,
    });
  });
});
```

### 2. Integrated Workflow: Running and Building

#### Development Mode

To run the application in development mode, you'll need to run both the Spring Boot backend and the Vite dev server:

1. Start the Spring Boot backend:
   ```bash
   ./mvnw spring-boot:run
   ```

2. In a separate terminal, start the Vite dev server:
   ```bash
   cd src/main/frontend
   npm run dev
   ```

The Vite dev server will proxy API requests to the Spring Boot backend, allowing you to develop the frontend with hot reloading while still interacting with the backend.

#### Production Build

To build the entire application for production:

```bash
./mvnw clean package
```

This will:
1. Clean the project
2. Install Node.js and npm (if not already installed)
3. Install npm dependencies
4. Build the React frontend
5. Run frontend tests
6. Compile the Java code
7. Package everything into a single JAR file

To run the final JAR file:

```bash
java -jar target/juniemvc-0.0.1-SNAPSHOT.jar
```

### 3. Updating Project Guidelines

Add the following section to your project guidelines:

```markdown
## Frontend Development Guidelines

### Project Structure
- Frontend source code is located in `src/main/frontend`
- Compiled frontend assets are placed in `src/main/resources/static`
- Follow the component structure defined in the project

### Development Workflow
- Run the backend with `./mvnw spring-boot:run`
- Run the frontend dev server with `cd src/main/frontend && npm run dev`
- Access the dev server at http://localhost:3000

### Building
- Build the entire application with `./mvnw clean package`
- Run the packaged application with `java -jar target/juniemvc-0.0.1-SNAPSHOT.jar`

### Testing
- Write unit tests for all components
- Run frontend tests with `cd src/main/frontend && npm test`
- Run all tests (backend and frontend) with `./mvnw test`

### Code Style
- Follow the ESLint and Prettier configurations
- Format code with `cd src/main/frontend && npm run format`
- Lint code with `cd src/main/frontend && npm run lint`

### Type Safety
- Use TypeScript for all frontend code
- Generate API types from OpenAPI spec with `cd src/main/frontend && npm run generate-api-types`
- Update API types whenever the backend API changes
```

## Conclusion

By following this guide, you've successfully integrated a React frontend with your Spring Boot application. The result is a single, self-contained JAR file that includes both the backend and frontend code.

Key benefits of this approach:
- Single build process for the entire application
- Type-safe API interactions between frontend and backend
- Modern development experience with hot reloading
- Efficient production deployment with a single JAR file

Remember to update the API types whenever the backend API changes to maintain type safety between the frontend and backend.