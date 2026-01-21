FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY target/elevator-*.jar elevator.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "elevator.jar"]