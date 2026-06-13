FROM openjdk:21-ea-1-jdk-slim

COPY target/Service-Producto-0.0.1.jar app_service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app_service.jar"]