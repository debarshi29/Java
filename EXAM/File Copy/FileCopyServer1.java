import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class FileCopyServer1 {
    private static final int SERVER_PORT = 1234;

    // Predefined users with their passwords
    private static final Map<String, String> users = new HashMap<>();

    static {
        users.put("user1", "password1");
        users.put("user2", "password2");
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                InputStream clientInput = clientSocket.getInputStream();
                OutputStream clientOutput = clientSocket.getOutputStream()
            ) {
                // Authenticate user
                out.println("Enter username: ");
                String username = in.readLine();

                out.println("Enter password: ");
                String password = in.readLine();

                if (!authenticate(username, password)) {
                    out.println("Authentication failed.");
                    return;
                }

                out.println("Authentication successful. Send file and number of copies.");

                // Read file name
                String fileName = in.readLine();
                File file = new File(fileName);

                // Read number of copies
                int numCopies;
                try {
                    numCopies = Integer.parseInt(in.readLine());
                } catch (NumberFormatException e) {
                    out.println("Invalid number of copies.");
                    return;
                }

                // Process file and make copies
                if (file.exists()) {
                    makeCopies(file, numCopies);
                    out.println("File copied successfully.");
                } else {
                    out.println("File not found.");
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

        private boolean authenticate(String username, String password) {
            return password.equals(users.get(username));
        }

        private void makeCopies(File file, int numCopies) {
            for (int i = 1; i <= numCopies; i++) {
                File backupFile = new File("copy_" + i + "_" + file.getName());
                try (
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(backupFile)
                ) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fileInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
