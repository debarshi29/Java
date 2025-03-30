import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWritingExample {

    public static void main(String[] args) {
        String filePath = "ile.txt";  // Replace with your file path
        String content = "This is an example of writing to a file in Java.";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
            bw.newLine();  // Add a new line
            bw.write("This is another line.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

