FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

FROM openjdk:17
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]