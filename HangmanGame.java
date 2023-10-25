import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HangmanGame {
    private String word;
    private String hint;
    private List<String> guessed;
    private String secondhint;
    private int lives;
    private boolean gameover;
    private boolean secondHinted;

    public HangmanGame() {
        this.word = "";
        this.hint = "";
        this.secondhint = "";
        this.guessed = new ArrayList<>();
        this.lives = 6;
        this.gameover = false;
        this.secondHinted = false;
    }

    public void start(String[] wordlist) {
        Random random = new Random();
        int index = random.nextInt(wordlist.length);
        String randomWord = wordlist[index];
        String[] parts = randomWord.split(":");
        this.word = parts[0];
        this.hint = parts[1];
        this.secondhint = parts[2];
    }

    public void start(HangmanWord[] wordlist) {
        Random random = new Random();
        int index = random.nextInt(wordlist.length);
        this.word = wordlist[index].getWord();
        this.hint = wordlist[index].getHint();
        this.secondhint = wordlist[index].getSecondHint();
    }    

    public void guess(String letter) {
        if (guessed.contains(letter)) {
            return;
        }
        guessed.add(letter);
        if (!word.contains(letter)) {
            lives--;
        }
        if (lives == 0 || allLettersGuessed()) {
            gameover = true;
        }
    }

    public String getWord() {
        return word;
    }

    public String getHint() {
        return hint;
    }

    public String getSecondHint() {
        return secondhint;
    }

    public List<String> getGuessed() {
        return guessed;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameover;
    }

    public boolean isSecondHinted() {
        return secondHinted;
    }

    private boolean allLettersGuessed() {
        for (char c : word.toCharArray()) {
            if (!guessed.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }
}