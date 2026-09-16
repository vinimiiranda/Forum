# ---- Etapa 1: build ----------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copia primeiro o pom para aproveitar o cache de dependencias do Docker:
# enquanto o pom nao mudar, esta camada nao e reconstruida.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Etapa 2: runtime --------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Roda a aplicacao com um usuario sem privilegios de root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
