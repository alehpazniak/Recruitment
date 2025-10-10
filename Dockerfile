FROM maven:4.0.0-rc-4-amazoncorretto-17

WORKDIR /app
COPY . /app
RUN mvn clean install
CMD java -jar target/recruitment-0.0.1-SNAPSHOT.jar