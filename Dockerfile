# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Resolve dependencies first so this layer is cached unless the POM changes.
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre-alpine AS runtime

# curl is used by the container healthcheck below.
RUN apk add --no-cache curl \
 && addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

USER spring:spring

EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0" \
    SPRING_PROFILES_ACTIVE=dev

HEALTHCHECK --interval=15s --timeout=5s --start-period=60s --retries=10 \
  CMD curl -fsS http://localhost:8080/v3/api-docs > /dev/null || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
