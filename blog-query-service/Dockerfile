# Multi-stage build
# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copy root pom.xml first
COPY pom.xml ./

# Copy all module pom.xml files (Maven needs all modules listed in root pom)
COPY blog-common/pom.xml ./blog-common/
COPY blog-user-service/pom.xml ./blog-user-service/
COPY blog-command-service/pom.xml ./blog-command-service/
COPY blog-query-service/pom.xml ./blog-query-service/
COPY api-gateway/pom.xml ./api-gateway/

# Copy source code for blog-common (dependency) and blog-query-service
COPY blog-common/src ./blog-common/src
COPY blog-query-service/src ./blog-query-service/src

# Build only blog-query-service and its dependencies (blog-common)
RUN mvn clean package -pl blog-query-service -am -Dmaven.test.skip=true

# Stage 2: Runtime image
FROM amazoncorretto:21-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Install curl for healthcheck
RUN apk add --no-cache curl

# Copy jar from build stage
COPY --from=build /build/blog-query-service/target/*.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app
USER appuser

# Expose port
EXPOSE 8083

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8083/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]