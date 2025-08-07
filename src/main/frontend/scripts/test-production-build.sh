#!/bin/bash

# This script tests the production build for the JunieMVC frontend

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Testing JunieMVC Frontend Production Build${NC}"
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

# Clean any previous build
echo -e "\n${YELLOW}Cleaning previous build...${NC}"
if [ -d "../resources/static" ]; then
    echo "Removing ../resources/static directory..."
    rm -rf ../resources/static
    echo -e "${GREEN}✓ Previous build cleaned${NC}"
else
    echo -e "${GREEN}✓ No previous build found${NC}"
fi

# Run the production build
echo -e "\n${YELLOW}Running production build...${NC}"
npm run build
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Production build successful${NC}"
else
    echo -e "${RED}✗ Production build failed${NC}"
    exit 1
fi

# Check if the build output directory exists
echo -e "\n${YELLOW}Checking build output...${NC}"
if [ -d "../resources/static" ]; then
    echo -e "${GREEN}✓ Build output directory exists${NC}"
else
    echo -e "${RED}✗ Build output directory does not exist${NC}"
    echo "The build process did not create the expected output directory"
    exit 1
fi

# Check for essential build artifacts
echo -e "\n${YELLOW}Checking build artifacts...${NC}"
REQUIRED_ARTIFACTS=(
    "../resources/static/index.html"
    "../resources/static/assets"
)

for artifact in "${REQUIRED_ARTIFACTS[@]}"; do
    if [ -e "$artifact" ]; then
        echo -e "${GREEN}✓ $artifact exists${NC}"
    else
        echo -e "${RED}✗ $artifact does not exist${NC}"
        echo "The build process did not create all required artifacts"
        exit 1
    fi
done

# Check if index.html contains expected content
echo -e "\n${YELLOW}Checking index.html content...${NC}"
if grep -q "<script" "../resources/static/index.html" && grep -q "<link" "../resources/static/index.html"; then
    echo -e "${GREEN}✓ index.html contains expected content${NC}"
else
    echo -e "${RED}✗ index.html does not contain expected content${NC}"
    echo "The build process did not create a valid index.html file"
    exit 1
fi

# Check if assets directory contains JS and CSS files
echo -e "\n${YELLOW}Checking assets directory...${NC}"
if [ "$(find ../resources/static/assets -name "*.js" | wc -l)" -gt 0 ]; then
    echo -e "${GREEN}✓ Assets directory contains JavaScript files${NC}"
else
    echo -e "${RED}✗ Assets directory does not contain JavaScript files${NC}"
    echo "The build process did not create JavaScript files"
    exit 1
fi

if [ "$(find ../resources/static/assets -name "*.css" | wc -l)" -gt 0 ]; then
    echo -e "${GREEN}✓ Assets directory contains CSS files${NC}"
else
    echo -e "${RED}✗ Assets directory does not contain CSS files${NC}"
    echo "The build process did not create CSS files"
    exit 1
fi

echo -e "\n${GREEN}All production build tests passed!${NC}"
echo "The frontend has been successfully built for production."
echo "The build artifacts are located in ../resources/static"
echo "These will be included in the Spring Boot JAR when building the entire application."

echo -e "\n${YELLOW}Production build test completed${NC}"
echo "=================================="