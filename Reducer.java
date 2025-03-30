import java.util.*;

public class Reducer {
    // Simulates the reduce function which aggregates key-value pairs
    public Map<String, Integer> reduce(Map<String, List<Integer>> groupedData) {
        Map<String, Integer> result = new HashMap<>();
        
        for (Map.Entry<String, List<Integer>> entry : groupedData.entrySet()) {
            String word = entry.getKey();
            List<Integer> counts = entry.getValue();
            
            // Sum all counts for this word
            int sum = counts.stream().mapToInt(Integer::intValue).sum();
            result.put(word, sum);
        }
        return result;
    }
}
