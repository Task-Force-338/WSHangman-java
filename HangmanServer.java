import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class HangmanServer {
    private static final String WORDLIST_FILE = "wordlist.json";

    private Set<HangmanGame> games = new HashSet<>();
    private HangmanWord[] wordlist;
    private Random random = new Random();

    public HangmanServer(int port) throws IOException {
        // Load the word list from a file
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(WORDLIST_FILE)));
        wordlist = gson.fromJson(reader, HangmanWord[].class);

        // Create a new ServerSocket and listen for incoming connections
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Hangman server started on port " + port);

        while (true) {
            // Accept a new client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress());

            // Create a new HangmanGame for the client
            HangmanGame game = new HangmanGame();
            game.start(wordlist);
            games.add(game);

            // Start a new thread to handle the client's requests
            Thread thread = new Thread(new HangmanClientHandler(clientSocket, game));
            thread.start();
        }
    }

    private HangmanWord getRandomWord() {
        return wordlist[random.nextInt(wordlist.length)];
    }


    public static void main(String[] args) throws IOException {
        new HangmanServer(args.length == 1 ? Integer.parseInt(args[0]) : 8080);
    }

    private class HangmanClientHandler implements Runnable {

        private Socket clientSocket;
        private HangmanGame game;

        public HangmanClientHandler(Socket clientSocket, HangmanGame game) {
            this.clientSocket = clientSocket;
            this.game = game;
        }

        @Override
        public void run() {
            try {
                // Create input and output streams for the client socket
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Send the details about the correct answer first
                JsonObject startMessage = new JsonObject();
                startMessage.addProperty("type", "start");
                startMessage.addProperty("word", game.getWord());
                startMessage.addProperty("hint", game.getHint());
                out.println(startMessage);

                while (true) {

                    if (game.isGameOver()) {
                        if (game.getLives() == 0) {
                            JsonObject response = new JsonObject();
                            response.addProperty("type", "gameover");
                            response.addProperty("word", game.getWord());
                            response.addProperty("win", false);
                            out.println(response);
                        } else if (game.getLives() > 0) {
                            JsonObject response = new JsonObject();
                            response.addProperty("type", "win");
                            response.addProperty("word", game.getWord());
                            response.addProperty("win", true);
                            out.println(response);
                        }
                        games.remove(game);
                        clientSocket.close();
                        break;
                    }

                    // Read the client's input
                    String input = in.readLine();

                    JsonObject data = new Gson().fromJson(input, JsonObject.class);

                    // Process the client's input and send a response
                    game.guess(data.get("letter").getAsString().toLowerCase());
                
                    if (game.isGameOver()) {
                        if (game.getLives() == 0) {
                            JsonObject response = new JsonObject();
                            response.addProperty("type", "gameover");
                            response.addProperty("gameover", true);
                            response.addProperty("word", game.getWord());
                            response.addProperty("win", false);
                            out.println(response);
                        } else {
                            JsonObject response = new JsonObject();
                            response.addProperty("type", "gameover");
                            response.addProperty("gameover", true);
                            response.addProperty("word", game.getWord());
                            response.addProperty("win", true);
                            out.println(response);
                        }
                        games.remove(game);
                        clientSocket.close();
                        break;
                    }
                    
                    if (game.getLives() == 3 && !game.isSecondHinted()) {
                        JsonObject response = new JsonObject();
                        response.addProperty("type", "guess");
                        response.addProperty("second_hint", game.getSecondHint());
                        response.addProperty("lives", game.getLives());
                        response.addProperty("word", new Gson().toJson(game.getWord()));
                        response.addProperty("guessed", new Gson().toJson(game.getGuessed()));
                        response.addProperty("gameover", game.isGameOver());
                        out.println(response);
                    }
                    else {
                        JsonObject response = new JsonObject();
                        response.addProperty("type", "guess");
                        response.addProperty("lives", game.getLives());
                        response.addProperty("word", new Gson().toJson(game.getWord()));
                        response.addProperty("guessed", new Gson().toJson(game.getGuessed()));
                        response.addProperty("gameover", game.isGameOver());
                        out.println(response);
                    }


                    // Check if the game is over
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}