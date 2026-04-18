
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY libs/trade-processor-0.0.1-SNAPSHOT.jar ./trade-processor.jar

EXPOSE 9060

ENTRYPOINT ["java", "-jar", "trade-processor.jar"]