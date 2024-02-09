# APP
FROM openjdk:11-jdk

#컨테이너 안에 .jar 파일을 app.jar 파일로 복사
COPY build/libs/***-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT ["java",  "-jar", "app.jar"]