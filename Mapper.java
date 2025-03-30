import java.util.*;

public class Mapper {
    // Simulates the map function which outputs key-value pairs
    public List<Map.Entry<String, Integer>> map(String document) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        String[] words = document.split("\\s+");  // Split the document into words
        
        for (String word : words) {
            // Convert the word to lowercase and remove punctuation
            word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
            if (!word.isEmpty()) {
                // Emit word with count 1
                result.add(new AbstractMap.SimpleEntry<>(word, 1));
            }
        }
        return result;
    }
}
