import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingFileWriter extends CustomFileWriter {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public void log(String filePath, String message) {
        String timestamp = dtf.format(LocalDateTime.now());
        String logMessage = timestamp + " - " + message;
        writeToFile(filePath, logMessage);  // Reusing the method from FileWritingExample
    }

    public static void main(String[] args) {
        LoggingFileWriter logger = new LoggingFileWriter();
        logger.log("logs.txt", "Application started.");
        logger.log("logs.txt", "An error occurred.");
    }
}

