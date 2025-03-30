import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CommandLoggerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to Command Logger Server");
            System.out.println("Enter commands to log. Type 'exit' to quit.");

            String command;
            while (true) {
                System.out.print("> ");
                command = scanner.nextLine();
                out.println(command);

                if ("exit".equalsIgnoreCase(command.trim())) {
                    break;
                }

                String response = in.readLine();
                System.out.println("Server: " + response);
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}