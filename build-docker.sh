#!/bin/bash

# Mortgage Calculator Docker Build Script
# This script builds and optionally runs the Docker container

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
IMAGE_NAME="mortgage-calculator"
CONTAINER_NAME="mortgage-calculator-app"
PORT="8080"

echo -e "${BLUE}🏠 Mortgage Calculator Docker Build Script${NC}"
echo "=============================================="

# Function to display usage
usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -b, --build-only    Build the Docker image only"
    echo "  -r, --run           Build and run the container"
    echo "  -s, --stop          Stop and remove the container"
    echo "  -l, --logs          Show container logs"
    echo "  -c, --clean         Clean up Docker resources"
    echo "  -h, --help          Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 --build-only     # Build image only"
    echo "  $0 --run            # Build and run"
    echo "  $0 --logs           # View logs"
    echo "  $0 --clean          # Clean up"
}

# Function to build Docker image
build_image() {
    echo -e "${YELLOW}🔨 Building Docker image...${NC}"
    docker build -t $IMAGE_NAME .
    echo -e "${GREEN}✅ Docker image built successfully!${NC}"
}

# Function to run container
run_container() {
    echo -e "${YELLOW}🚀 Starting container...${NC}"
    
    # Stop existing container if running
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        echo -e "${YELLOW}⚠️  Stopping existing container...${NC}"
        docker stop $CONTAINER_NAME
        docker rm $CONTAINER_NAME
    fi
    
    # Run new container
    docker run -d \
        --name $CONTAINER_NAME \
        -p $PORT:8080 \
        --restart unless-stopped \
        $IMAGE_NAME
    
    echo -e "${GREEN}✅ Container started successfully!${NC}"
    echo -e "${BLUE}🌐 Application available at: http://localhost:$PORT${NC}"
    echo -e "${BLUE}📊 Health check: http://localhost:$PORT/actuator/health${NC}"
}

# Function to stop container
stop_container() {
    echo -e "${YELLOW}🛑 Stopping container...${NC}"
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        docker stop $CONTAINER_NAME
        docker rm $CONTAINER_NAME
        echo -e "${GREEN}✅ Container stopped and removed!${NC}"
    else
        echo -e "${YELLOW}⚠️  Container is not running${NC}"
    fi
}

# Function to show logs
show_logs() {
    echo -e "${YELLOW}📋 Showing container logs...${NC}"
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        docker logs -f $CONTAINER_NAME
    else
        echo -e "${RED}❌ Container is not running${NC}"
    fi
}

# Function to clean up Docker resources
cleanup() {
    echo -e "${YELLOW}🧹 Cleaning up Docker resources...${NC}"
    
    # Stop and remove container
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        docker stop $CONTAINER_NAME
        docker rm $CONTAINER_NAME
    fi
    
    # Remove image
    if [ "$(docker images -q $IMAGE_NAME)" ]; then
        docker rmi $IMAGE_NAME
    fi
    
    # Clean up unused resources
    docker system prune -f
    
    echo -e "${GREEN}✅ Cleanup completed!${NC}"
}

# Main script logic
case "${1:-}" in
    -b|--build-only)
        build_image
        ;;
    -r|--run)
        build_image
        run_container
        ;;
    -s|--stop)
        stop_container
        ;;
    -l|--logs)
        show_logs
        ;;
    -c|--clean)
        cleanup
        ;;
    -h|--help)
        usage
        ;;
    "")
        echo -e "${YELLOW}No option specified. Building and running...${NC}"
        build_image
        run_container
        ;;
    *)
        echo -e "${RED}❌ Unknown option: $1${NC}"
        usage
        exit 1
        ;;
esac
