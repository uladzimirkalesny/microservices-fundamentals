FROM adoptopenjdk/openjdk11:alpine
ARG JAR_FILE=target/resource-service-1.0.0.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]