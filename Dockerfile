FROM openjdk:17
WORKDIR /usr/src/app
COPY . .
EXPOSE 8080
CMD ["java", "-jar", "target/Shop-Online-Back-1.0.0.jar"]
