import java.util.*;

public class WordCountMapReduce {
    public static void main(String[] args) {
        // Sample documents to process
        String[] documents = {
            "Hello world",
            "World of MapReduce",
            "Hello from the world of Java",
            "MapReduce in the world of Hadoop"
        };

        // Step 1: Mapping
        Mapper mapper = new Mapper();
        List<Map.Entry<String, Integer>> mappedData = new ArrayList<>();
        
        for (String doc : documents) {
            mappedData.addAll(mapper.map(doc));
        }

        // Step 2: Shuffle and Sort (Group by key)
        Map<String, List<Integer>> groupedData = new HashMap<>();
        
        for (Map.Entry<String, Integer> entry : mappedData) {
            String word = entry.getKey();
            int count = entry.getValue();
            
            groupedData.computeIfAbsent(word, k -> new ArrayList<>()).add(count);
        }

        // Step 3: Reducing
        Reducer reducer = new Reducer();
        Map<String, Integer> reducedData = reducer.reduce(groupedData);

        // Step 4: Display the result
        System.out.println("Word Count Results:");
        for (Map.Entry<String, Integer> entry : reducedData.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
