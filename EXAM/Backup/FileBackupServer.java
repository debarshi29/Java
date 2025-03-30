import java.io.*;
import java.net.*;

public class FileBackupServer {
    public static void main(String[] args) {
        int port = 5000;
        String backupDirectory = "C:\\Users\\DEBARSHI\\Documents\\Programs\\Java\\EXAM\\Backup"; // Change this to your desired backup location

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                try (
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                ) {
                    String fileName = dataInputStream.readUTF();
                    long fileSize = dataInputStream.readLong();

                    File outputFile = new File(backupDirectory + fileName);
                    try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        long totalBytesRead = 0;
                        while (totalBytesRead < fileSize && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalBytesRead))) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                        }
                    }

                    System.out.println("File " + fileName + " backed up successfully");
                } catch (IOException e) {
                    System.out.println("Error processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }
}