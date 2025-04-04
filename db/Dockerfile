FROM eclipse-temurin:21-alpine

WORKDIR /app

COPY build/libs/db.jar /app/db.jar
COPY src/main/resources/db/migration /app/db/migration

ENV DB_URL=jdbc:postgresql://db:5432/warcoat
ENV DB_USER=warcoat
ENV DB_PASS=warcoat

ENTRYPOINT ["java", "-jar", "/app/db.jar"]