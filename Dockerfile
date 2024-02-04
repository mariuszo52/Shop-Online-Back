FROM openjdk:17.0.1-jdk-bullseye

EXPOSE 8080

CMD ["java", "-jar", "target/Shop-Online-Back-1.0.0.jar"]
