# Build stage
FROM openjdk:21-jdk-slim as build

# Set working directory
WORKDIR /app

# Copy build.gradle and settings.gradle files
COPY . .

# Grant permissions
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew build --no-daemon

# Copy the source code
COPY src /app/src

# Build the application
RUN ./gradlew build -x test --no-daemon

# Runtime stage
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the binary from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8000

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]