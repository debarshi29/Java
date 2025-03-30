import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandLogger {
    private static final String HISTORY_FILE = "command_history.txt";

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Command Logger is running...");
            String command;
            while (true) {
                System.out.println("Enter a command (or type 'exit' to quit):");
                command = reader.readLine();
                
                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }
                
                logCommandWithTimestamp(command);
                System.out.println("Command logged.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logCommandWithTimestamp(String command) {
        try (PrintWriter historyWriter = new PrintWriter(new FileWriter(HISTORY_FILE, true))) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            historyWriter.println(timestamp + " - " + command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
