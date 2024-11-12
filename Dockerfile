# JDK 21 사용
FROM openjdk:21-jdk-slim

# JAR 파일을 컨테이너에 복사
COPY ./build/libs/oTP-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
