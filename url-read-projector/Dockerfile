# Stage 1: Build and extraction of layers
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

RUN cp target/*-SNAPSHOT.jar app.jar || cp target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract --destination extracted

# Stage 2: Final executable image
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /build/extracted/dependencies/ ./
COPY --from=builder /build/extracted/spring-boot-loader/ ./
COPY --from=builder /build/extracted/snapshot-dependencies/ ./
COPY --from=builder /build/extracted/application/ ./

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseZGC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]