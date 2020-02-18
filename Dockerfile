FROM maven:3.6.1-jdk-8-slim
WORKDIR /usr/src/java-app
COPY ./target/*.jar ./app.jar
CMD ["java", "-jar", "-Dserver.port=8080","-Dspring.profiles.active=prod", "app.jar"]