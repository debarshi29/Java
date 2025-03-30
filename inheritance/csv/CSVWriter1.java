import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVWriter1 extends CustomFileWriter {

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
        CSVWriter1 csvWriter = new CSVWriter1();
        Scanner scanner = new Scanner(System.in);

        // Define the file path where the CSV data will be written
        String filePath = "data.csv";

        // Ask the user for the number of columns
        System.out.println("Enter the number of columns for the CSV file:");
        int numColumns = Integer.parseInt(scanner.nextLine());

        // Collecting column headers from the user
        String[] header = new String[numColumns];
        System.out.println("Enter the column headers for the CSV file:");
        for (int i = 0; i < numColumns; i++) {
            System.out.print("Header " + (i + 1) + ": ");
            header[i] = scanner.nextLine();
        }

        // Write the header to the CSV file
        csvWriter.writeCSV(filePath, header);

        // Collecting rows of data from the user
        boolean moreRows = true;
        while (moreRows) {
            System.out.println("Enter a row of data (comma-separated), or type 'done' to finish:");
            String rowInput = scanner.nextLine();
            if ("done".equalsIgnoreCase(rowInput)) {
                moreRows = false;
            } else {
                String[] row = rowInput.split(",", -1); // Handle extra columns if the user enters more
                if (row.length == numColumns) {
                    csvWriter.writeCSV(filePath, row);
                } else {
                    System.out.println("Error: The number of columns in the row does not match the header. Please try again.");
                }
            }
        }

        System.out.println("CSV file created successfully at: " + filePath);

        // Close the scanner
        scanner.close();
    }
}
