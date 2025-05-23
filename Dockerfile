ARG PROJECT_ARTIFACT_ID=adaptive-smart-assistant

############ RUNNABLE STAGE ############
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

ARG PROJECT_ARTIFACT_ID
ARG PROPRIETARY_REPO_URL=YOUR-PATH-TO-PROPRIETARY_REPO
ARG GITHUB_ACTOR=REGISTRY-USER
ARG READ_PACKAGES_TOKEN=REGISTRY-TOKEN
ARG MAVEN_CLI_OPTS=EXTRA-OPTIONS

COPY pom.xml /app/pom.xml
COPY /src /app/src
COPY etc/ci_settings.xml /app/etc/ci_settings.xml

# Build JAR file
RUN mvn clean install -DskipTests $MAVEN_CLI_OPTS -Dproprietary-repo-url=$PROPRIETARY_REPO_URL && \
    cp /app/target/$PROJECT_ARTIFACT_ID-*.jar /app/$PROJECT_ARTIFACT_ID.jar

############ RUNNABLE STAGE ############
FROM eclipse-temurin:17-jre-focal AS final

WORKDIR /app

ARG PROJECT_ARTIFACT_ID

ENV PROJECT_ARTIFACT_ID=${PROJECT_ARTIFACT_ID}

COPY etc/$PROJECT_ARTIFACT_ID.properties /app/etc/$PROJECT_ARTIFACT_ID.properties
COPY entrypoint.sh /app/entrypoint.sh
COPY --from=build /app/$PROJECT_ARTIFACT_ID.jar ./

RUN apt-get update && \
    # Required to use nc command in the wait for it function, see entrypoint.sh
    apt-get install -y netcat && \
    # Make a file executable
    chmod a+x entrypoint.sh

EXPOSE 8086

ENTRYPOINT ["./entrypoint.sh"]
