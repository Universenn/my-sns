FROM openjdk:11.0-slim

COPY build/libs/***-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT ["java",  "-jar", "app.jar"]