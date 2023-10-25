import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class HangmanClient {

    public static void main(String[] args) throws IOException {

        // The address and port of the server to connect to
        String address = args.length == 1 ? args[0] : "localhost";
        int port = args.length == 2 ? Integer.parseInt(args[1]) : 8080;

        // Create a new Socket and connect to the server
        Socket socket = new Socket(address, port);
        System.out.println("Connected to server at " + address + ":" + port);

        // Create input and output streams for the socket
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Send a "start" message to the server
        // TF-HangmanSpec doesn'tr equire the client to send a "start" message
        // JsonObject startMessage = new JsonObject();
        // startMessage.addProperty("type", "start");
        // out.println(startMessage);

        System.out.println("Hangman");
        System.out.println("Lives: 6");

        // START PHASE (or drone phase or whatever you want to call it. who tf plays rainbow six siege nowadays)
        // Read the server's response and print the word and hint
        JsonObject response = new Gson().fromJson(in.readLine(), JsonObject.class);
        String word = response.get("word").getAsString();
        String hint = response.get("hint").getAsString();
        System.out.println("The word has " + word.length() + " letters");
        System.out.println("Hint: " + hint);

        // Loop until the game is over
        while (true) {
            // Read the user's input and send it to the server
            System.out.print("Guess a letter: ");
            String letter = new BufferedReader(new InputStreamReader(System.in)).readLine();
            
            if (letter.length() != 1) {
                System.out.println("Please enter a single letter");
                continue;
            }

            JsonObject guessMessage = new JsonObject();
            guessMessage.addProperty("type", "guess");
            guessMessage.addProperty("letter", letter);
            out.println(guessMessage);

            // Read the server's response and print the updated word and hint
            response = new Gson().fromJson(in.readLine(), JsonObject.class);
            /**
                if data["type"] == "guess":
                print("Word: {}".format("".join(letter if letter in data["guessed"] else "_" for letter in data["word"])))
                print("Guessed: {}".format(data["guessed"]))
                print("Lives: {}".format(data["lives"]))
            **/

            if (response.get("type").getAsString().equals("guess")) {
                String displayWord = "";
                for (int i = 0; i < word.length(); i++) {
                    if (response.get("guessed").getAsString().contains(String.valueOf(word.charAt(i)))) {
                        displayWord += word.charAt(i);
                    } else {
                        displayWord += "_";
                    }
                }
                System.out.println("Word: " + displayWord);
                System.out.println("Guessed: " + response.get("guessed").getAsString());
                System.out.println("Lives: " + response.get("lives").getAsString());
            }
            if (response.has("second_hint")) {
                System.out.println("Second hint: " + response.get("second_hint").getAsString());
            }
            // Check if the game is over
            if (response.get("gameover").getAsBoolean()) {
                if (response.get("type").getAsString().equals("gameover")) {
                    if (response.get("win").getAsBoolean()) {
                        System.out.println("You win!");
                        System.out.println("The word was " + response.get("word").getAsString());
                        break;
                    } else {
                        System.out.println("You lose!");
                        System.out.println("The word was " + response.get("word").getAsString());
                        break;
                    }
                }
            }


        }

        // Close the socket
        socket.close();
    }
}