import java.io.*;
import java.net.*;

public class FileCopyClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        // File to send
        File file = new File("client_test.txt");

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             FileInputStream fileIn = new FileInputStream(file);
             OutputStream out = socket.getOutputStream();
             DataOutputStream dataOut = new DataOutputStream(out)) {

            // Send file name and file size to the server
            dataOut.writeUTF(file.getName());
            dataOut.writeLong(file.length());

            // Send the file data to the server
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent: " + file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
