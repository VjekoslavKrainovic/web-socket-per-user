# Specify the base image
FROM eclipse-temurin:21-jdk AS build

# Copy source code
COPY . /app

# Set the working directory
WORKDIR /app

RUN chmod +x mvnw
# Install dependencies
RUN ./mvnw package -DskipTests
RUN mv -f target/*.jar app.jar

# Final stage
FROM eclipse-temurin:21-jre
ARG PORT
ENV PORT=${PORT}

COPY --from=build /app/app.jar .

RUN useradd runtime
USER runtime

# Specify the command to run the application
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "app.jar" ]