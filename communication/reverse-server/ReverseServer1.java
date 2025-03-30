import java.io.*;
import java.net.*;

/**
 * This program demonstrates a simple TCP/IP socket server that sends predefined
 * messages to the client.
 * This server is single-threaded.
 *
 * @author www.codejava.net
 */
public class ReverseServer1 {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a port number.");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String text;

                // Send predefined messages to the client
                while (true) {
                    text = reader.readLine(); // Read message from the client
                    if (text.equals("bye")) {
                        System.out.println("Client disconnected.");
                        break; // Exit the loop if the client sends "bye"
                    }

                    // Send a response message from the server to the client
                    String responseMessage = "Server: Hello! You said: " + text;
                    writer.println(responseMessage); // Send message to the client
                }

                socket.close(); // Close the socket after communication ends
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}