
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY libs/refdata-provider-0.0.1-SNAPSHOT.jar ./refdata-provider.jar

EXPOSE 9063

ENTRYPOINT ["java", "-jar", "refdata-provider.jar"]