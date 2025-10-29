FROM openjdk:17-jdk-slim
WORKDIR /app

# copy sources
COPY src ./src

# compile all sources except UiApp.java into /app/out
RUN find src/main/java -name "*.java" ! -name "UiApp.java" > sources.txt \
 && mkdir -p out \
 && javac -d out @sources.txt

# If Main.java has no package:
CMD ["java","-cp","out","Main"]
# If Main.java has a package, change to:
# CMD ["java","-cp","out","your.package.Main"]
