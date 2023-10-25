# Real men either use Alpine as base
# FROM alpine:latest
# and wastes their time installing JDK
# RUN apk add openjdk11

# Or just use OpenJDK as base. That exists.
# https://hub.docker.com/_/openjdk OpenJDK image has been deprecated.
# turns out out of 5 choices offered, Eclipse's build is the most based one.
FROM eclipse-temurin:latest

# you can specify the version of your OpenJRE you want to use, but for the sake of simplicity, I'll just use the latest version.

# Set the working directory to /app. Gotta keep things clean.
WORKDIR /app

# Copy the server file into the container at /app
COPY HangmanServer.java /app
COPY HangmanGame.java /app
COPY HangmanWord.java /app

# Thanks to the fact that I don't use Maven, I have to download the JAR artifact manually.
RUN wget https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar -O /app/gson.jar

# We have to compile the classes first
RUN javac -cp /app/gson.jar HangmanGame.java
RUN javac -cp /app/gson.jar HangmanWord.java
RUN javac -cp /app/gson.jar HangmanServer.java


# Expose the port that the server will be listening on. This is for the container's side.
EXPOSE 8765

# Run the server when the container launches.
# CMD differs from RUN in that it is run when the container is launched, not when the image is built.
# CMD is also overwritten by the command line arguments when the container is launched.
# So if you want to run the server with different arguments, you can do so by changing the CMD arguments below.
CMD ["java", "-cp", "/app:/app/gson.jar", "HangmanServer", "8765"]

# maybe return some funny strings when the server is launched?
# CMD ["echo", "Hello! I am on Team Chitoge!"]
# Maybe not, unless I specify in the license that the server is not for Onodera fans.

# technically this is a linux machine so you could just... halt the server????
# CMD ["halt"]
# don't do this

# !!! DO NOT NAME THE IMAGE SAYORI !!! 