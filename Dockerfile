FROM openjdk:24-jdk-slim

WORKDIR /app

COPY build/libs/auth-service-all.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
