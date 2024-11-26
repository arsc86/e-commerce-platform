FROM eclipse-temurin:17-jre

# Instalar dumb-init para manejar señales (SIGTERM, SIGINT)
RUN apt-get update && \
    apt-get install -y --no-install-recommends dumb-init && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN useradd -m appuser

USER appuser

WORKDIR /app

ARG JAR
ARG TARGET_PATH

COPY $TARGET_PATH/$JAR app.jar

EXPOSE 8080

ENTRYPOINT ["dumb-init", "java", "-Dspring.config.location=/app/application.properties", "-jar", "app.jar"]
