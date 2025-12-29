FROM maven:3.8.2-openjdk-17

WORKDIR /app

LABEL authors="garikcareer@gmail.com"

COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

COPY /target/springboot-maven-thymeleaf-h2-template-*.jar app.jar

EXPOSE 8080
#
#ARG JAR_FILE=target/userdirectory-0.0.1a.jar
#
#COPY ${JAR_FILE} target/userdirectory-0.0.1a.jar
CMD ["java","-jar","app.jar"]