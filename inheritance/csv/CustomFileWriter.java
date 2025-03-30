import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomFileWriter {

    public void writeToFile(String filePath, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(content);
            bw.newLine();  // Add a new line after writing the content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

