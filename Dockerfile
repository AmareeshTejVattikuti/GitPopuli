FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY target/your-application.jar /app/
ENTRYPOINT ["java","-jar","/app/git-populi-1.0.0.jar"]
EXPOSE 8080