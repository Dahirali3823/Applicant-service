FROM openjdk:24-jdk


WORKDIR /app


COPY target/applicant-service-0.0.1-SNAPSHOT.jar app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]