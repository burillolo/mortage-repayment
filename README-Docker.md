# Mortgage Calculator - Docker Setup

This document provides instructions for building and running the Mortgage Calculator application using Docker.

## Overview

The application consists of:
- **Backend**: Spring Boot REST API (Java 17)
- **Frontend**: Angular 18 web application
- **Database**: In-memory calculations (no external database required)

## Prerequisites

- Docker installed on your system
- At least 4GB of available RAM
- Port 8080 available on your machine

## Quick Start

### 1. Build the Docker Image

```bash
docker build -t mortgage-calculator .
```

This will:
- Build the Angular frontend with production optimizations
- Compile the Spring Boot backend
- Create a multi-stage Docker image with both frontend and backend

### 2. Run the Container

```bash
docker run -p 8080:8080 --name mortgage-calculator-app mortgage-calculator
```

### 3. Access the Application

- **Web Interface**: http://localhost:8080
- **API Endpoint**: http://localhost:8080/api

## API Usage

### Calculate Mortgage Schedule

```bash
curl "http://localhost:8080/api?principal=100000&interest=5.5&duration=24&page=0&size=10"
```

**Parameters:**
- `principal`: Loan amount (e.g., 100000)
- `interest`: Annual interest rate (e.g., 5.5)
- `duration`: Loan duration in years (e.g., 24)
- `page`: Page number for pagination (0-based)
- `size`: Number of results per page

**Response:**
```json
{
  "content": [
    {
      "id": 288,
      "principal": 167.76,
      "interest": 458.33,
      "restante": 287.0,
      "capitalPendiente": 99832.24
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 29,
  "totalElements": 288,
  "first": true,
  "last": false
}
```

## Docker Image Details

### Image Size
- **Total Size**: ~200MB
- **Base Image**: Alpine Linux (eclipse-temurin:17-jre-alpine)
- **Optimization**: Multi-stage build with minimal runtime dependencies

### Security Features
- Non-root user execution
- Minimal attack surface with Alpine Linux
- No unnecessary packages or services

### Performance
- JVM optimized for containers
- Angular production build with optimizations
- Efficient resource usage

## Container Management

### Stop the Container
```bash
docker stop mortgage-calculator-app
```

### Remove the Container
```bash
docker rm mortgage-calculator-app
```

### View Container Logs
```bash
docker logs mortgage-calculator-app
```

### Run in Background (Detached Mode)
```bash
docker run -d -p 8080:8080 --name mortgage-calculator-app mortgage-calculator
```

## Development vs Production

### Development
- Use `npm start` for frontend development
- Use `mvn spring-boot:run` for backend development
- Hot reload enabled for both frontend and backend

### Production
- Use Docker container for deployment
- Optimized builds with minification and compression
- Single container deployment

## Troubleshooting

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Container Won't Start
```bash
# Check container logs
docker logs mortgage-calculator-app

# Check if image exists
docker images | grep mortgage-calculator
```

### Memory Issues
```bash
# Run with memory limit
docker run -p 8080:8080 -m 1g --name mortgage-calculator-app mortgage-calculator
```

## Environment Variables

The application can be configured using environment variables:

```bash
docker run -p 8080:8080 \
  -e SERVER_PORT=8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  --name mortgage-calculator-app \
  mortgage-calculator
```

## Health Check

The application includes a health check endpoint:

```bash
curl http://localhost:8080/actuator/health
```

## Scaling

For production deployment, consider:

1. **Load Balancer**: Use nginx or similar
2. **Multiple Instances**: Run multiple containers
3. **Database**: Add external database for persistence
4. **Monitoring**: Add application monitoring

## Support

For issues or questions:
1. Check the container logs
2. Verify port availability
3. Ensure sufficient system resources
4. Review the application configuration

## License

This project is licensed under the MIT License.
