FROM openjdk:17
WORKDIR /usr/src/app
COPY Shop-Online-Back .
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "target/Shop-Online-Back-1.0.0.jar"]
