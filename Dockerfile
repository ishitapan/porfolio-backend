# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- RUNTIME STAGE ----------
# Use alpine for smaller size, but ensure it has the necessary libraries
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/target/porfolio-backend-0.0.1-SNAPSHOT.jar app.jar

# Render needs to see the app listening on $PORT
ENTRYPOINT ["java", "-Xmx512m", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]