#!/bin/bash

# This script tests the integration between the frontend and backend for the JunieMVC application

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Testing JunieMVC Frontend-Backend Integration${NC}"
echo "=================================="

# Check if Java is installed
echo -e "\n${YELLOW}Checking Java installation...${NC}"
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo -e "${GREEN}✓ Java is installed (${JAVA_VERSION})${NC}"
else
    echo -e "${RED}✗ Java is not installed${NC}"
    echo "Please install Java 21 or later"
    exit 1
fi

# Check if Maven is installed
echo -e "\n${YELLOW}Checking Maven installation...${NC}"
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn --version | head -n 1)
    echo -e "${GREEN}✓ Maven is installed (${MVN_VERSION})${NC}"
else
    echo -e "${RED}✗ Maven is not installed${NC}"
    echo "Please install Maven"
    exit 1
fi

# Check if curl is installed
echo -e "\n${YELLOW}Checking curl installation...${NC}"
if command -v curl &> /dev/null; then
    CURL_VERSION=$(curl --version | head -n 1)
    echo -e "${GREEN}✓ curl is installed (${CURL_VERSION})${NC}"
else
    echo -e "${RED}✗ curl is not installed${NC}"
    echo "Please install curl"
    exit 1
fi

# Check if we're in the frontend directory
echo -e "\n${YELLOW}Checking current directory...${NC}"
if [ "$(basename $(pwd))" = "frontend" ]; then
    echo -e "${GREEN}✓ Current directory is frontend${NC}"
else
    echo -e "${RED}✗ Current directory is not frontend${NC}"
    echo "Please run this script from the frontend directory"
    exit 1
fi

# Navigate to the project root directory
echo -e "\n${YELLOW}Navigating to project root directory...${NC}"
cd ../../../
if [ -f "pom.xml" ]; then
    echo -e "${GREEN}✓ Found project root directory${NC}"
else
    echo -e "${RED}✗ Could not find project root directory${NC}"
    echo "Please run this script from the frontend directory"
    exit 1
fi

# Build the entire application
echo -e "\n${YELLOW}Building the entire application...${NC}"
echo "This may take a few minutes..."
./mvnw clean package -DskipTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Application built successfully${NC}"
else
    echo -e "${RED}✗ Application build failed${NC}"
    exit 1
fi

# Check if the JAR file exists
echo -e "\n${YELLOW}Checking for JAR file...${NC}"
JAR_FILE=$(find target -name "*.jar" -not -name "*sources.jar" -not -name "*javadoc.jar" | head -n 1)
if [ -n "$JAR_FILE" ]; then
    echo -e "${GREEN}✓ Found JAR file: $JAR_FILE${NC}"
else
    echo -e "${RED}✗ Could not find JAR file${NC}"
    echo "The build process did not create a JAR file"
    exit 1
fi

# Start the application
echo -e "\n${YELLOW}Starting the application...${NC}"
echo "The application will be started in the background and terminated after the tests"
java -jar $JAR_FILE &
APP_PID=$!

# Wait for the application to start
echo -e "\n${YELLOW}Waiting for the application to start...${NC}"
MAX_ATTEMPTS=30
ATTEMPT=0
while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
    ATTEMPT=$((ATTEMPT+1))
    echo "Attempt $ATTEMPT of $MAX_ATTEMPTS..."
    
    # Check if the application is still running
    if ! kill -0 $APP_PID 2>/dev/null; then
        echo -e "${RED}✗ Application process terminated unexpectedly${NC}"
        exit 1
    fi
    
    # Try to access the application
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080)
    if [ "$HTTP_CODE" = "200" ]; then
        echo -e "${GREEN}✓ Application started successfully${NC}"
        break
    fi
    
    sleep 2
done

if [ $ATTEMPT -ge $MAX_ATTEMPTS ]; then
    echo -e "${RED}✗ Application failed to start within the expected time${NC}"
    kill $APP_PID
    exit 1
fi

# Test API endpoints
echo -e "\n${YELLOW}Testing API endpoints...${NC}"

# Test Beer API
echo "Testing Beer API..."
BEER_RESPONSE=$(curl -s http://localhost:8080/api/v1/beers)
if [ -n "$BEER_RESPONSE" ] && [[ "$BEER_RESPONSE" == *"content"* ]]; then
    echo -e "${GREEN}✓ Beer API is working${NC}"
else
    echo -e "${RED}✗ Beer API is not working${NC}"
    echo "Response: $BEER_RESPONSE"
    kill $APP_PID
    exit 1
fi

# Test frontend static resources
echo -e "\n${YELLOW}Testing frontend static resources...${NC}"
INDEX_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/)
if [ "$INDEX_RESPONSE" = "200" ]; then
    echo -e "${GREEN}✓ Frontend index.html is accessible${NC}"
else
    echo -e "${RED}✗ Frontend index.html is not accessible${NC}"
    echo "HTTP Code: $INDEX_RESPONSE"
    kill $APP_PID
    exit 1
fi

# Check for JavaScript files
echo "Checking for JavaScript files..."
JS_RESPONSE=$(curl -s http://localhost:8080/ | grep -o 'src="[^"]*\.js"')
if [ -n "$JS_RESPONSE" ]; then
    echo -e "${GREEN}✓ JavaScript files are referenced in index.html${NC}"
else
    echo -e "${RED}✗ JavaScript files are not referenced in index.html${NC}"
    kill $APP_PID
    exit 1
fi

# Check for CSS files
echo "Checking for CSS files..."
CSS_RESPONSE=$(curl -s http://localhost:8080/ | grep -o 'href="[^"]*\.css"')
if [ -n "$CSS_RESPONSE" ]; then
    echo -e "${GREEN}✓ CSS files are referenced in index.html${NC}"
else
    echo -e "${RED}✗ CSS files are not referenced in index.html${NC}"
    kill $APP_PID
    exit 1
fi

# Stop the application
echo -e "\n${YELLOW}Stopping the application...${NC}"
kill $APP_PID
echo -e "${GREEN}✓ Application stopped${NC}"

echo -e "\n${GREEN}All integration tests passed!${NC}"
echo "The frontend and backend are working together correctly."
echo "You can run the application with: java -jar $JAR_FILE"

echo -e "\n${YELLOW}Integration test completed${NC}"
echo "=================================="