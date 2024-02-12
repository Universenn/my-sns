FROM openjdk:11.0-slim

WORKDIR /app
COPY /build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT ["java",  "-jar", "app.jar"]