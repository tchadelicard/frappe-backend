# Stage 1: Build stage - Use a Gradle image with JDK 17 and Amazon Corretto as the base image
FROM gradle:jdk17-corretto AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle build files to leverage caching
COPY build.gradle.kts settings.gradle.kts /app/

# Copy the source code to the working directory
COPY src /app/src

# Build the Spring Boot application using Gradle, generating the JAR file
RUN gradle --no-daemon clean bootJar


# Stage 2: Production stage - Use a lightweight Amazon Corretto JRE image for running the app
FROM amazoncorretto:17-alpine

# Create a non-root user 'spring' with a fixed user ID for security
RUN adduser -D -u 1000 spring

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the production stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Switch to the non-root user 'spring' for security
USER spring

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Set the entry point to run the Spring Boot application using Java
ENTRYPOINT ["java", "-jar", "app.jar"]