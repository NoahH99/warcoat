FROM eclipse-temurin:21-alpine

WORKDIR /app

COPY build/libs/bot.jar /app/bot.jar

ENV JDBC_DATABASE_URL=jdbc:postgresql://db:5432/warcoat
ENV JDBC_DATABASE_USERNAME=warcoat
ENV JDBC_DATABASE_PASSWORD=warcoat

ENTRYPOINT ["java", "-jar", "/app/bot.jar"]