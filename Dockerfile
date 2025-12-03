FROM eclipse-temurin:24-jdk AS builder
WORKDIR /app

COPY gradle ./gradle
COPY gradlew .
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar ./spring-boot-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]