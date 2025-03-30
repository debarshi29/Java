import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient1 {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        System.out.println("Connected to chat server");

        // Thread for reading messages from server
        new Thread(new MessageReader(socket)).start();

        // Main thread for sending messages
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {
            
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
            }
        }
    }

    private static class MessageReader implements Runnable {
        private Socket socket;

        public MessageReader(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }
    }
}
