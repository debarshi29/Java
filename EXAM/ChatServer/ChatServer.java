import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int SERVER_PORT = 1234;
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Chat server started...");

            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                // Create a new client handler for the client and start the thread
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcastMessage(String message, ClientHandler excludeUser) {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                if (clientHandler != excludeUser) {
                    clientHandler.sendMessage(message);
                }
            }
        }
    }

    // Remove a client when they disconnect
    public static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Client disconnected.");
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Setup input and output streams
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Request and set client's name
            out.println("Enter your name: ");
            clientName = in.readLine();
            System.out.println(clientName + " has joined the chat.");
            ChatServer.broadcastMessage(clientName + " has joined the chat!", this);

            String message;
            // Read messages from client and broadcast them
            while ((message = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
                System.out.println(clientName + ": " + message);
                ChatServer.broadcastMessage(clientName + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cleanup when client disconnects
            try {
                ChatServer.removeClient(this);
                clientSocket.close();
                ChatServer.broadcastMessage(clientName + " has left the chat.", this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send message to this client
    public void sendMessage(String message) {
        out.println(message);
    }
}
