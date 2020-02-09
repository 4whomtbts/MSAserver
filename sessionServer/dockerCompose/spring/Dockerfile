FROM openjdk:12-jdk

# 어떤 이미지로부터 새로운 이미지를 생성할 지 지정. 플랫폼 : 버전 형태로 작성
MAINTAINER yonghoon <btc_yh@naver.com>
# Dockerfile을 생성-관리하는 사람

EXPOSE 8080

ARG JAR_FILE=build/libs/chatting-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} /chatting-springboot.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/chatting-springboot.jar"]

