FROM adoptopenjdk/openjdk11:alpine
ARG JAR_FILE=target/api-gateway-1.0.0.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]