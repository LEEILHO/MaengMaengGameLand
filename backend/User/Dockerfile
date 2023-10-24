FROM openjdk:11
ENV TZ=Asia/Seoul
ARG JAR_FILE=./build/libs/User-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]