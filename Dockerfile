# OpenJDK 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 타임존 설정 (Asia/Seoul)
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 컨테이너 실행 시 실행할 명령어
CMD ["java", "-jar", "app.jar"]
