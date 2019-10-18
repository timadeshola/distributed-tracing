FROM openjdk:8-jdk-alpine
LABEL maintaner="john.adeshola@ubagroup.com"
VOLUME /tmp/zipkin-server
EXPOSE 9411
ARG JAR_FILE=zipkin-server-2.14.0-exec.jar
ADD ${JAR_FILE} zipkin-server-2.14.0-exec.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dkafka_bootstrap_servers=http://localhost:9092", "-Dstorage_type=elasticsearch", "-Des_hosts=http://elasticsearch:9200", "-jar","zipkin-server-2.14.0-exec.jar"]
