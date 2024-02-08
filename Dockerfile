FROM openjdk:11.0-slim
WORKDIR /app
COPY ./*-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody

ENTRYPOINT [                                                \
   "java",                                                 \
   "-jar",                                                 \
   "-Djava.security.egd=file:/dev/./urandom",              \
   "-Dsun.net.inetaddr.ttl=0",                             \
   "-Dcom.amazonaws.sdk.disableEc2Metadata=true",          \
   "app.jar"              \
]