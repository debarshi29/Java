import java.io.*;
import java.net.*;

public class FileCopyServer {
    private static final int SERVER_PORT = 1234;
    private static final int NUMBER_OF_COPIES = 5; // Number of copies to make

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started. Waiting for connections...");

            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                // Handle client in a new thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (InputStream in = clientSocket.getInputStream();
                 DataInputStream dataIn = new DataInputStream(in)) {

                // Read the file name and the file size from the client
                String fileName = dataIn.readUTF();
                long fileSize = dataIn.readLong();

                // Create a file to store the original file
                File originalFile = new File("server_" + fileName);
                try (FileOutputStream fileOut = new FileOutputStream(originalFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long totalRead = 0;

                    // Read file data from client and write it to the original file
                    while ((bytesRead = in.read(buffer)) != -1 && totalRead < fileSize) {
                        fileOut.write(buffer, 0, bytesRead);
                        totalRead += bytesRead;
                    }
                    System.out.println("File received: " + fileName);
                }

                // Make multiple copies of the received file
                for (int i = 1; i <= NUMBER_OF_COPIES; i++) {
                    File copiedFile = new File("copy_" + i + "_" + fileName);
                    copyFile(originalFile, copiedFile);
                    System.out.println("Created copy " + i + " of " + fileName);
                }

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

        // Method to copy a file
        private void copyFile(File source, File destination) throws IOException {
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(destination)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
