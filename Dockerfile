FROM openjdk:17-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the host to the container
COPY target/*.jar /app/app.jar
EXPOSE 8089
# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]