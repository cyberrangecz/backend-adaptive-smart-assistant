FROM maven:3.6.2-jdk-11-slim AS build

## default environment variables for database settings
ARG PROJECT_ARTIFACT_ID=kypo-smart-assistant

## default link to proprietary repository, e.g., Nexus repository
ARG PROPRIETARY_REPO_URL=YOUR-PATH-TO-PROPRIETARY_REPO

# install
RUN apt-get update && apt-get install -y rsyslog

# copy only essential parts
COPY /etc/kypo-smart-assistant.properties /app/etc/kypo-smart-assistant.properties
COPY pom.xml /app/pom.xml
COPY /src /app/src

# build kypo smart assistant service
RUN cd /app && \
    mvn clean install -DskipTests -Dproprietary-repo-url=$PROPRIETARY_REPO_URL && \
    cp /app/target/$PROJECT_ARTIFACT_ID-*.jar /app/kypo-smart-assistant.jar

WORKDIR /app
EXPOSE 8086
ENTRYPOINT ["java", "-Dspring.config.location=/app/etc/kypo-smart-assistant.properties", "-jar",  "/app/kypo-smart-assistant.jar"]
