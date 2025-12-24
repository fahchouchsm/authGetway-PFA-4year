# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first, download dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Then copy the source code
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/authGetway-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
