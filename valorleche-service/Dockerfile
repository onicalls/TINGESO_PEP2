FROM openjdk:18
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} valorleche-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/valorleche-service-0.0.1-SNAPSHOT.jar"]