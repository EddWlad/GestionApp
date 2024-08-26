FROM maven:3.8.3-openjdk-17 as build

COPY src /home/app/src
COPY pom.xml /home/app

WORKDIR /home/app

RUN mvn clean package

FROM openjdk:17-slim

COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/usr/local/lib/app.jar"]