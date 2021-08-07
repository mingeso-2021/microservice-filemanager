FROM openjdk:11.0.11-oracle

COPY . .

RUN  chmod +x ./gradlew

RUN ./gradlew build

COPY . .

COPY docker-entrypoint.sh /

RUN chmod +x docker-entrypoint.sh

ENTRYPOINT ["bash", "docker-entrypoint.sh"]

EXPOSE 1818