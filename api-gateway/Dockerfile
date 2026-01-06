# Multi-stage build
# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /build

# Copy pom.xml first for better layer caching
COPY api-gateway/pom.xml ./

# Copy source code
COPY api-gateway/src ./src

# Build the application (skip tests for faster build, run tests separately)
# Build from current directory, not as a module
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Runtime image
FROM amazoncorretto:11-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Install curl for healthcheck
RUN apk add --no-cache curl

# Copy jar from build stage
COPY --from=build /build/target/*.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]