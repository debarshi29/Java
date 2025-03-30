import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class AuthServer {
    private static final int SERVER_PORT = 1234;

    // Predefined users with their passwords
    private static Map<String, String> users = new HashMap<>();

    static {
        users.put("user1", "password1");
        users.put("user2", "password2");
        users.put("admin", "admin123");
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Authentication server started...");

            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                // Handle the client in a separate thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle client authentication
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
                // Request username and password
                out.println("Enter username: ");
                String username = in.readLine();

                out.println("Enter password: ");
                String password = in.readLine();

                // Validate credentials
                if (authenticate(username, password)) {
                    out.println("Authentication successful! Welcome, " + username + ".");
                } else {
                    out.println("Authentication failed. Invalid username or password.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Method to authenticate the client
        private boolean authenticate(String username, String password) {
            String validPassword = users.get(username);
            return validPassword != null && validPassword.equals(password);
        }
    }
}
