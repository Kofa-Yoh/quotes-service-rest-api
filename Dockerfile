FROM openjdk:17-oracle

WORKDIR /app

COPY build/libs/QuotesServiceApi-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/data/ /root/data/

ENV SERVER_PORT=8081
ENV DATABASE_URL=jdbc:h2:mem:quotes_db
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=root
ENV H2_URL=/h2-console
ENV SWAGGER_URL=/swagger

CMD ["java", "-jar", "app.jar"]