import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVWriter extends CustomFileWriter {

    // Method to escape special characters in CSV format
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // Method to write a row of data to a CSV file
    public void writeCSV(String filePath, String[] data) {
        StringBuilder sb = new StringBuilder();
        
        // Construct a CSV line from the data array
        for (int i = 0; i < data.length; i++) {
            sb.append(escapeCSV(data[i]));
            if (i < data.length - 1) {
                sb.append(",");  // Add a comma between elements
            }
        }

        // Use the inherited method to write the CSV line to the file
        writeToFile(filePath, sb.toString());
    }

    public static void main(String[] args) {
        CSVWriter csvWriter = new CSVWriter();
        Scanner scanner = new Scanner(System.in);

        // Define the file path where the CSV data will be written
        String filePath = "data.csv";

        // Collecting header information from the user
        System.out.println("Enter the column headers for the CSV file (comma-separated):");
        String headerInput = scanner.nextLine();
        String[] header = headerInput.split(",");

        // Write the header to the CSV file
        csvWriter.writeCSV(filePath, header);

        // Collecting rows of data from the user
        boolean moreData = true;
        while (moreData) {
            System.out.println("Enter a row of data (comma-separated), or type 'done' to finish:");
            String rowInput = scanner.nextLine();
            if ("done".equalsIgnoreCase(rowInput)) {
                moreData = false;
            } else {
                String[] row = rowInput.split(",");
                csvWriter.writeCSV(filePath, row);
            }
        }

        System.out.println("CSV file created successfully at: " + filePath);

        // Close the scanner
        scanner.close();
    }
}
