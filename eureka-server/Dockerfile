# Use lightweight Java 17 runtime
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file (dynamic name handled via wildcard)
COPY build/libs/*.jar app.jar

#ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]