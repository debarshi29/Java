import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCountReducer {

    /**
     * Reduces the mapped word counts by summing them.
     *
     * @param mappedData The list of (word, count) pairs from the mapper.
     * @return A map of word counts.
     */
    public Map<String, Integer> reduce(List<Map.Entry<String, Integer>> mappedData) {
        Map<String, Integer> reducedCounts = new HashMap<>();

        // Aggregate the word counts
        for (Map.Entry<String, Integer> entry : mappedData) {
            reducedCounts.put(entry.getKey(), reducedCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }

        return reducedCounts;
    }
}
