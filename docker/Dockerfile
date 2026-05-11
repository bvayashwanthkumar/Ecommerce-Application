# Multi-stage Dockerfile for better optimization

# Stage 1: Build stage
FROM maven:3.8.1-openjdk-11 AS builder

WORKDIR /build

# Copy pom.xml
COPY pom.xml .

# Download dependencies (cached)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /build/target/ecommerce-app-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
