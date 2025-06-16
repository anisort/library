FROM openjdk:21-jdk
ARG JAR_FILE=target/*.jar
COPY auth/target/auth-0.0.1-SNAPSHOT.jar application.jar
CMD apt-get update -y
ENTRYPOINT ["java", "-jar", "/application.jar"]