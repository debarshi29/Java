import java.io.*;
import java.net.*;

public class AuthClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Connected to the server.");

            // Setup input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Read username from server
            System.out.println(in.readLine());
            String username = consoleReader.readLine();
            out.println(username);

            // Read password from server
            System.out.println(in.readLine());
            String password = consoleReader.readLine();
            out.println(password);

            // Print server response (authentication result)
            System.out.println(in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
