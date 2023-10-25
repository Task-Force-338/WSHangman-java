# WSHangman-java
An Operating System project that forces us to write a hangman game that uses  TCP sockets. This one is Java.

I know. I screwed up by writing it to use Websockets and `asyncio`. I'm not fixing it. For non-websocket, see the Java version.

# Pre-requisites
- You have to **be a human being**. Unless you are going to write a `pyautogui` script to play the game for you. In that case, go ahead. I'm not stopping you.
    - You also need a basic understanding on English vocabulary. If you don't know what a word means, you can always look it up on Google.
    - (Optional) Implement a Neuro-sama-like Vtuber AI to play the game for you.
    - (Optional) You also should have basic knowledge on:
      - Programming
      - Ryu Ga Gotoku Series
      - Call of Duty
      - Planes
      - Counter-Strike
      - Industrial Communication Protocols
      - ~~How to write a good README~~
      - Gepgraphy of Japan
      - Racing
      - S.T.A.L.K.E.R.
      - Cold War History
      - Linux
      - Valorant
      - TF2
      - British Empire
      - K-On!
      - Balkan Geography
      - Geopolitics of Ex-Soviet States
      - Cars
      - Japanese Arcade Games
      - Food
      - Yuri (the genre, not the Russian name)
      - Yuri (the Russian name, not the genre)
- You'll need **any computing device** that can run Java or higher. I'm using an Acer Nitro, but I have confirmed that it also works with a guest under an ESXi host and a Raspberry Pi 4. If you're feeling fancy you can also run it on a EC2 instance. Just make sure that you shut it down when you're done.
- You'll need **Java**. I'm using whatever version of OpenJRE that comes with RPM Fusion. Minus point if you use Oracle's, Amazon's, or Microsoft's Java. That's just cringe.
- You'll need a raw artifact of Gson. Just get it from Maven Central [here](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1) Click on the .jar file and download it.
- You'll need **Docker or Podman** if you want to **run the server in a container.** You'll also need to build the image first. The ability to build images comes with every copy of Docker Engine and Podman.
- And if you like, you could try to build this project with **Maven**. And if you do, I'd be more than happy to accept your pull request.
  - Or **Gradle**. Just don't use Ant.

# Usage


### Server

First, compile the external classes first. You have to do this because we don't want to use buildtools.

```bash
$ javac -cp <path to Gson jar> -d . HangmanGame.java
$ javac -cp <path to Gson jar> -d . HangmanWord.java
```

Run the Server (You can do it raw, but Docker is easier.)
Or use Podman, because Docker's business decision is why Podman exists in the first place. You have to build the image first, though.

```bash
# Raw
$ java -cp <path to Gson jar> HangmanServer

# Docker
$ docker build -t wshangman-py .
# please do not name the image Sayori, please.
$ docker run -p 8765:8765 wshangman-py

# Podman
# You know that Podman is a drop-in replacement for Docker, right?
$ podman build -t wshangman-py .
$ podman run -p 8765:8765 wshangman-py
```
### Client

Run the Client. The client only needs the Gson jar to run.

```bash
$ java -cp <path to Gson jar> HangmanClient

# Why would you want to run the client in a container?
```

Regarding on how to play the game, I'll let you figure it out yourself. I'm not going to spoonfeed you.

## Regarding Docker Compose
Why would you even need Docker Compose for this? It's a single container. You can just run it with Docker or Podman.

# Report Bugs
cope.

# License
Licensed under ~~WTFPL. This shit isn't worth it bros.~~ BSD 3-Clause License.

![](https://i.redd.it/g6pf82y0znx51.gif)
