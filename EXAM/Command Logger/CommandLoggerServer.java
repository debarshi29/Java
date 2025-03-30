import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class CommandLoggerServer {
    private static final int PORT = 5000;
    private static final String LOG_FILE = "command_log.txt";
    private static final ExecutorService pool = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Command Logger Server is running on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } finally {
            pool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                System.out.println("New connection from " + clientAddress);

                String command;
                while ((command = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(command.trim())) {
                        break;
                    }
                    logCommand(clientAddress, command);
                    out.println("Command logged: " + command);
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private void logCommand(String clientAddress, String command) {
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String logEntry = String.format("[%s] %s: %s%n", timestamp, clientAddress, command);
            
            try (FileWriter fw = new FileWriter(LOG_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.print(logEntry);
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
            
            System.out.print(logEntry);  // Also print to console
        }
    }
}