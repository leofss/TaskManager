# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build app/target/taskmanager-0.0.1-SNAPSHOT.jar ./taskmanager.jar
EXPOSE 8080
CMD ["java", "-jar", "taskmanager.jar"]