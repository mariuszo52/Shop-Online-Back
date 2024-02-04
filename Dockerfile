FROM openjdk:17.0.1-jdk-bullseye

WORKDIR /app

COPY . .

RUN ./mvnw clean install -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/Shop-Online-Back-1.0.0.jar"]
