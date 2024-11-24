FROM openjdk:17-jdk-alpine as builder

# Set the working directory
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy the pom.xml and the src directory
COPY pom.xml .
COPY src ./src
#COPY src/main/resources/templates /app/src/main/resources/templates
#COPY src/main/resources/static /app/src/main/resources/static

# Build the application
RUN mvn clean package -DskipTests

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "target/DATN-0.0.1-SNAPSHOT.jar"]