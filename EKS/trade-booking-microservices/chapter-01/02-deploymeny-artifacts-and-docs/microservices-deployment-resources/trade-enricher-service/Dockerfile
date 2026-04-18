
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY libs/trade-enricher-0.0.1-SNAPSHOT.jar ./trade-enricher.jar

EXPOSE 9062

ENTRYPOINT ["java", "-jar", "trade-enricher.jar"]