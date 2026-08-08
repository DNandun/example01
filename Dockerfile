FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy pre-built JAR from Maven package step in Jenkins Pipeline
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

