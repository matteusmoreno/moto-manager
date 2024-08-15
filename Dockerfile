FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/moto-manager-0.0.1-SNAPSHOT.jar /app/moto_manager.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "moto_manager.jar"]
