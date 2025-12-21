FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/authGetway-0.0.1.jar getway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "getway.jar"]