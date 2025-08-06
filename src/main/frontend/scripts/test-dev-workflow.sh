#!/bin/bash

# This script tests the development workflow for the JunieMVC frontend

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Testing JunieMVC Frontend Development Workflow${NC}"
echo "=================================="

# Check if Node.js is installed
echo -e "\n${YELLOW}Checking Node.js installation...${NC}"
if command -v node &> /dev/null; then
    NODE_VERSION=$(node -v)
    echo -e "${GREEN}✓ Node.js is installed (${NODE_VERSION})${NC}"
else
    echo -e "${RED}✗ Node.js is not installed${NC}"
    echo "Please install Node.js v22.16.0 or later"
    exit 1
fi

# Check if npm is installed
echo -e "\n${YELLOW}Checking npm installation...${NC}"
if command -v npm &> /dev/null; then
    NPM_VERSION=$(npm -v)
    echo -e "${GREEN}✓ npm is installed (${NPM_VERSION})${NC}"
else
    echo -e "${RED}✗ npm is not installed${NC}"
    echo "Please install npm v11.4.0 or later"
    exit 1
fi

# Check if required files exist
echo -e "\n${YELLOW}Checking project structure...${NC}"
REQUIRED_FILES=(
    "package.json"
    "vite.config.ts"
    "tsconfig.json"
    "src/main.tsx"
    "src/App.tsx"
)

for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓ $file exists${NC}"
    else
        echo -e "${RED}✗ $file does not exist${NC}"
        echo "Please check the project structure"
        exit 1
    fi
done

# Check if node_modules exists
echo -e "\n${YELLOW}Checking node_modules...${NC}"
if [ -d "node_modules" ]; then
    echo -e "${GREEN}✓ node_modules exists${NC}"
else
    echo -e "${YELLOW}! node_modules does not exist, running npm install...${NC}"
    npm install
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ npm install completed successfully${NC}"
    else
        echo -e "${RED}✗ npm install failed${NC}"
        exit 1
    fi
fi

# Check if TypeScript compiles
echo -e "\n${YELLOW}Checking TypeScript compilation...${NC}"
npm run build --if-present
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ TypeScript compilation successful${NC}"
else
    echo -e "${RED}✗ TypeScript compilation failed${NC}"
    exit 1
fi

# Run linting
echo -e "\n${YELLOW}Running linting...${NC}"
npm run lint --if-present
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Linting passed${NC}"
else
    echo -e "${RED}✗ Linting failed${NC}"
    echo "Please fix the linting errors"
    exit 1
fi

# Run tests
echo -e "\n${YELLOW}Running tests...${NC}"
npm test --if-present
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Tests passed${NC}"
else
    echo -e "${RED}✗ Tests failed${NC}"
    echo "Please fix the failing tests"
    exit 1
fi

# Check if Vite dev server starts
echo -e "\n${YELLOW}Testing Vite dev server...${NC}"
echo "Starting Vite dev server (will be terminated after 5 seconds)..."
timeout 5 npm run dev &
DEV_PID=$!

# Wait for a moment to let the server start
sleep 5

# Check if the process is still running
if kill -0 $DEV_PID 2>/dev/null; then
    echo -e "${GREEN}✓ Vite dev server started successfully${NC}"
    kill $DEV_PID
else
    echo -e "${RED}✗ Vite dev server failed to start${NC}"
    exit 1
fi

echo -e "\n${GREEN}All development workflow tests passed!${NC}"
echo "You can now run the following commands:"
echo "  - npm run dev: Start the development server"
echo "  - npm run build: Build the frontend for production"
echo "  - npm test: Run tests"
echo "  - npm run lint: Run linting"
echo "  - npm run format: Format code with Prettier"

echo -e "\n${YELLOW}Development workflow test completed${NC}"
echo "=================================="