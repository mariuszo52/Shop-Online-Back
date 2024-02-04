FROM openjdk:17
WORKDIR /usr/src/app
COPY . .
EXPOSE 8080
CMD ["java", "-jar", "target/your-application.jar"]
