
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY libs/exchange-0.0.1-SNAPSHOT.jar ./exchange.jar

EXPOSE 9061

ENTRYPOINT ["java", "-jar", "exchange.jar"]