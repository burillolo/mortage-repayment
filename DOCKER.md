# Docker Setup for Mortgage Calculator

This document provides instructions for building and running the Mortgage Calculator application using Docker.

## Prerequisites

- Docker installed on your system
- Docker Compose (optional, for easier management)

## Quick Start

### Option 1: Using the Build Script (Recommended)

```bash
# Build and run the application
./build-docker.sh --run

# Or just build the image
./build-docker.sh --build-only

# View logs
./build-docker.sh --logs

# Stop the container
./build-docker.sh --stop

# Clean up everything
./build-docker.sh --clean
```

### Option 2: Using Docker Commands

```bash
# Build the Docker image
docker build -t mortgage-calculator .

# Run the container
docker run -d \
  --name mortgage-calculator-app \
  -p 8080:8080 \
  --restart unless-stopped \
  mortgage-calculator

# View logs
docker logs -f mortgage-calculator-app

# Stop the container
docker stop mortgage-calculator-app
docker rm mortgage-calculator-app
```

### Option 3: Using Docker Compose

```bash
# Build and run with docker-compose
docker-compose up --build -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## Application Access

Once the container is running, the application will be available at:

- **Main Application**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

## Docker Image Details

### Multi-Stage Build Process

1. **Frontend Builder**: Builds the Angular application using Node.js 18
2. **Backend Builder**: Compiles the Spring Boot application using Maven and OpenJDK 17
3. **Runtime Image**: Creates a lightweight production image using OpenJDK 17 JRE

### Image Specifications

- **Base Runtime**: OpenJDK 17 JRE Slim
- **Memory**: 512MB max heap, 256MB initial heap
- **Port**: 8080
- **User**: Non-root user for security
- **Health Check**: Built-in health monitoring

### Environment Variables

- `JAVA_OPTS`: JVM options (default: optimized for containers)
- `SPRING_PROFILES_ACTIVE`: Spring profile (default: production)

## Container Management

### Viewing Logs

```bash
# Real-time logs
docker logs -f mortgage-calculator-app

# Last 100 lines
docker logs --tail 100 mortgage-calculator-app
```

### Container Status

```bash
# Check if running
docker ps -f name=mortgage-calculator-app

# View resource usage
docker stats mortgage-calculator-app
```

### Debugging

```bash
# Access container shell
docker exec -it mortgage-calculator-app /bin/bash

# Check application logs inside container
docker exec mortgage-calculator-app ls -la /app
```

## Production Deployment

### Environment Configuration

For production deployment, consider setting these environment variables:

```bash
docker run -d \
  --name mortgage-calculator-app \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  -e SPRING_PROFILES_ACTIVE=production \
  --restart unless-stopped \
  mortgage-calculator
```

### Resource Limits

```bash
docker run -d \
  --name mortgage-calculator-app \
  -p 8080:8080 \
  --memory="1g" \
  --cpus="1.0" \
  --restart unless-stopped \
  mortgage-calculator
```

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using port 8080
   lsof -i :8080
   
   # Use a different port
   docker run -p 8081:8080 mortgage-calculator
   ```

2. **Out of Memory**
   ```bash
   # Increase memory limits
   docker run --memory="2g" mortgage-calculator
   ```

3. **Build Failures**
   ```bash
   # Clean build (no cache)
   docker build --no-cache -t mortgage-calculator .
   ```

### Health Check

The container includes a built-in health check that monitors the application status:

```bash
# Check health status
docker inspect --format='{{.State.Health.Status}}' mortgage-calculator-app
```

## Security Considerations

- Container runs as non-root user
- Minimal base image (OpenJDK JRE Slim)
- No unnecessary packages installed
- Health checks for monitoring

## Performance Optimization

- Multi-stage build reduces final image size
- JVM optimized for containerized environments
- Efficient layer caching for faster rebuilds
- Production-ready Angular build with optimizations
