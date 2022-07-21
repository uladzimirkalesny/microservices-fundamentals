FROM adoptopenjdk/openjdk11:alpine
ARG JAR_FILE=target/eureka-server-1.0.0.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]