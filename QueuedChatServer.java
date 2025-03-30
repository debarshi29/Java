import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class QueuedChatServer {
    private static final int SERVER_PORT = 1234;
    private static final int MAX_ACTIVE_USERS = 3;  // Maximum active users
    private static final int QUEUE_CAPACITY = 5;    // Maximum waiting queue capacity

    // Executor service to handle client threads
    private static final ExecutorService clientPool = Executors.newFixedThreadPool(MAX_ACTIVE_USERS);
    
    // Queue to hold clients waiting to connect when the limit is reached
    private static final BlockingQueue<Socket> waitingQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Chat server started...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                // If the thread pool is at capacity, add the client to the queue
                if (clientPool.isShutdown() || ((ThreadPoolExecutor) clientPool).getActiveCount() >= MAX_ACTIVE_USERS) {
                    // Add to the queue if there's room, or reject the connection
                    if (!waitingQueue.offer(clientSocket)) {
                        rejectConnection(clientSocket);
                    } else {
                        System.out.println("Client added to the waiting queue.");
                    }
                } else {
                    // Handle the client immediately if there's space
                    clientPool.execute(new ClientHandler(clientSocket));
                }

                // Process the queue if there are available threads
                processWaitingQueue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to reject clients when the server is full
    private static void rejectConnection(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("Server is full. Please try again later.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to process the waiting queue when space becomes available
    private static void processWaitingQueue() {
        if (((ThreadPoolExecutor) clientPool).getActiveCount() < MAX_ACTIVE_USERS && !waitingQueue.isEmpty()) {
            Socket clientSocket = waitingQueue.poll();
            if (clientSocket != null) {
                clientPool.execute(new ClientHandler(clientSocket));
                System.out.println("Client from the queue is now connected.");
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                out.println("You are connected to the chat server.");
                String message;
                
                // Simulate a chat interaction by reading client input
                while ((message = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                    System.out.println("Client says: " + message);
                    out.println("Server: " + message);
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

                // Process queue when a client disconnects
                processWaitingQueue();
            }
        }
    }
}
