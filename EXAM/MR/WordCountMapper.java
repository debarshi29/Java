import java.util.*;

public class WordCountMapper {

    /**
     * Map each word in the line to (word, 1) pair.
     *
     * @param line The input string (a line of text).
     * @return A list of (word, 1) key-value pairs.
     */
    public List<Map.Entry<String, Integer>> map(String line) {
        List<Map.Entry<String, Integer>> mappedWords = new ArrayList<>();
        String[] words = line.toLowerCase().split("\\W+"); // Split by non-word characters

        for (String word : words) {
            if (!word.trim().isEmpty()) {
                mappedWords.add(new AbstractMap.SimpleEntry<>(word, 1));
            }
        }
        return mappedWords;
    }
}
