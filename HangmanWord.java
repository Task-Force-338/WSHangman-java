
public class HangmanWord {
    private String word;
    private String hint;
    private String second_hint;

    public HangmanWord(String word, String hint, String secondHint) {
        this.word = word;
        this.hint = hint;
        this.second_hint = secondHint;
    }

    public String getWord() {
        return word;
    }

    public String getHint() {
        return hint;
    }

    public String getSecondHint() {
        return second_hint;
    }
}
