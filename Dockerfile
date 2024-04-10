FROM openjdk:17-jdk-alpine
COPY target/medical-clinic-0.0.1-SNAPSHOT.jar medical-clinic-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/medical-clinic-0.0.1-SNAPSHOT.jar"]