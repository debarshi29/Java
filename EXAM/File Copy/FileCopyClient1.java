import java.io.*;
import java.net.*;

public class FileCopyClient1 {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Read authentication information
            System.out.println(in.readLine());
            String username = consoleReader.readLine();
            out.println(username);

            System.out.println(in.readLine());
            String password = consoleReader.readLine();
            out.println(password);

            String response = in.readLine();
            System.out.println(response);

            if (response.equals("Authentication successful. Send file and number of copies.")) {
                // Send file name
                System.out.println("Enter the file name to be backed up:");
                String fileName = consoleReader.readLine();
                out.println(fileName);

                // Send number of copies
                System.out.println("Enter the number of copies:");
                String numCopies = consoleReader.readLine();
                out.println(numCopies);

                // Read server response
                System.out.println(in.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
