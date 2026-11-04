
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordManager {

    private final Map<String, String> words = new LinkedHashMap<>();

    public WordManager() {
        words.put("JAVA", "A popular programming language.");
        words.put("COMPUTER", "An electronic device used for processing data.");
        words.put("GARDEN", "A place where flowers and plants grow.");
        words.put("PYTHON", "A programming language named after a snake.");
        words.put("KEYBOARD", "An input device used for typing.");
        words.put("ELEPHANT", "The largest land animal.");
        words.put("INTERNET", "A worldwide network connecting computers.");
        words.put("DEVELOPER", "A person who creates software.");
        words.put("RAINBOW", "A colorful arc that appears after rain.");
        words.put("DATABASE", "An organized collection of information.");
    }

    public String getRandomWord() {
        List<String> wordList = new ArrayList<>(words.keySet());
        Collections.shuffle(wordList);

        return wordList.get(0);
    }

    public String scrambleWord(String word) {
        List<Character> letters = new ArrayList<>();

        for (char letter : word.toCharArray()) {
            letters.add(letter);
        }

        Collections.shuffle(letters);

        StringBuilder scrambled = new StringBuilder();

        for (char letter : letters) {
            scrambled.append(letter);
        }

        // Avoid showing the original word as the scrambled version.
        if (scrambled.toString().equals(word)) {
            Collections.rotate(letters, 1);

            scrambled.setLength(0);

            for (char letter : letters) {
                scrambled.append(letter);
            }
        }

        return scrambled.toString();
    }

    public String getHint(String word) {
        return words.getOrDefault(word, "No hint available.");
    }
}
