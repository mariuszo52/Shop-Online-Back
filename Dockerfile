FROM openjdk:17

RUN apt-get update && apt-get install -y maven

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN mvn clean install -DskipTests

CMD ["java", "-jar", "target/your-application.jar"]
