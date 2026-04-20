
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY libs/trading-api-gateway-0.0.1-SNAPSHOT.jar ./trading-api-gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "trading-api-gateway.jar"]
