import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer2 {
    private static final int PORT = 5000;
    private static Map<String, PrintWriter> clientWriters = new HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Chat Server is running...");
        ServerSocket serverSocket = new ServerSocket(PORT);
        
        try {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Ask for and set the username
                out.println("Enter your username:");
                username = in.readLine();
                System.out.println(username + " has joined the chat.");
                broadcast(username + " has joined the chat.");

                synchronized (clientWriters) {
                    clientWriters.put(username, out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.isEmpty()) {
                        System.out.println(username + ": " + message);
                        broadcast(username + ": " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (username != null) {
                    synchronized (clientWriters) {
                        clientWriters.remove(username);
                    }
                    broadcast(username + " has left the chat.");
                    System.out.println(username + " has left the chat.");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }

        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters.values()) {
                    writer.println(message);
                    writer.flush();
                }
            }
        }
    }
}
