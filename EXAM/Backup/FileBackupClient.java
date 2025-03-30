import java.io.*;
import java.net.*;

public class FileBackupClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change this to the server's IP address if running on a different machine
        int serverPort = 5000;
        String filePath = "C:\\Users\\DEBARSHI\\Documents\\Programs\\Java\\EXAM\\Backup\\client_test.txt"; // Change this to the path of the file you want to backup

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            File file = new File(filePath);
            String fileName = file.getName();

            try (
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                FileInputStream fileInputStream = new FileInputStream(file);
            ) {
                dataOutputStream.writeUTF(fileName);
                dataOutputStream.writeLong(file.length());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("File " + fileName + " sent for backup");
            }
        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
        }
    }
}