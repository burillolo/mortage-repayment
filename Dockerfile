# Multi-stage Dockerfile for Mortgage Repayment Calculator
# Stage 1: Build Angular Frontend
FROM node:18-alpine AS frontend-builder

# Set working directory
WORKDIR /app/frontend

# Copy package files
COPY src/main/frontend/cuadro-web/package*.json ./

# Install dependencies (including dev dependencies for build)
RUN npm ci

# Copy source code
COPY src/main/frontend/cuadro-web/ ./

# Build Angular application for production
RUN npm run build -- --configuration=production

# Stage 2: Build Java Backend
FROM maven:3.8.6-eclipse-temurin-17 AS backend-builder

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml ./
COPY mvnw ./
COPY mvnw.cmd ./

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Copy built frontend from previous stage
COPY --from=frontend-builder /app/frontend/../../resources/public/* ./src/main/resources/public/

# Build the application (skip frontend build since it's already done)
RUN mvn clean package -DskipTests

# Stage 3: Runtime Image
FROM eclipse-temurin:17-jre

# Install necessary packages
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Create app user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set working directory
WORKDIR /app

# Copy the built JAR from backend-builder stage
COPY --from=backend-builder /app/target/*.jar app.jar

# Change ownership to app user
RUN chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]