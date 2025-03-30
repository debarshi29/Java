import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVFileWriter extends CustomFileWriter {

    // Method to write a row of data to a CSV file
    public void writeCSV(String filePath, String[] data) {
        StringBuilder sb = new StringBuilder();
        
        // Construct a CSV line from the data array
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (i < data.length - 1) {
                sb.append(",");  // Add a comma between elements
            }
        }

        // Use the inherited method to write the CSV line to the file
        writeToFile(filePath, sb.toString());
    }

    public static void main(String[] args) {
        CSVFileWriter csvWriter = new CSVFileWriter();
        
        // Define the file path where the CSV data will be written
        String filePath = "data.csv";

        // Sample data to write to the CSV file
        String[] header = {"Name", "Age", "City"};
        String[] row1 = {"John Doe", "30", "New York"};
        String[] row2 = {"Jane Smith", "25", "Los Angeles"};
        String[] row3 = {"Emily Johnson", "35", "Chicago"};

        // Writing data to the CSV file
        csvWriter.writeCSV(filePath, header);
        csvWriter.writeCSV(filePath, row1);
        csvWriter.writeCSV(filePath, row2);
        csvWriter.writeCSV(filePath, row3);

        System.out.println("CSV file created successfully at: " + filePath);
    }
}

