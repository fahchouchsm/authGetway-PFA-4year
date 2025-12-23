# Use Maven + JDK 21 for building
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run the jar
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/authGetway-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]