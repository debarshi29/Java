import java.io.*;
import java.util.*;

public class WordCountDriver {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java WordCountDriver <input file> <output file>");
            System.exit(1);
        }

        String inputFilePath = args[0];
        String outputFilePath = args[1];

        WordCountMapper mapper = new WordCountMapper();
        WordCountReducer reducer = new WordCountReducer();

        List<Map.Entry<String, Integer>> mappedData = new ArrayList<>();
        // Read the input file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                mappedData.addAll(mapper.map(line));  // Map the input
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        }

        // Reduce the mapped data
        Map<String, Integer> reducedData = reducer.reduce(mappedData);

        // Write the output to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (Map.Entry<String, Integer> entry : reducedData.entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Word count completed. Output written to " + outputFilePath);
    }
}
