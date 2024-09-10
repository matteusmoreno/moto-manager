# Etapa 1: Construir o projeto
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Executar a aplicação
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/moto-manager-0.0.1-SNAPSHOT.jar /app/moto_manager.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "moto_manager.jar"]
