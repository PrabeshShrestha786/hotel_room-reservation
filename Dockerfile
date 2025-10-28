# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy source code
COPY ./src /app

# Compile Java code (output to current directory)
RUN javac Main.java

# Run the program (no src/ prefix)
CMD ["java", "Main"]
