import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileBackup {

    public static void main(String[] args) {
        // Specify the source file (file to back up) and destination file (backup location)
        String sourceFilePath = "source.txt"; // Change this to the file you want to backup
        String backupFilePath = "backup.txt"; // Change this to where you want to store the backup

        // Create File objects for both source and backup files
        File sourceFile = new File(sourceFilePath);
        File backupFile = new File(backupFilePath);

        // Perform the file backup
        backupFile(sourceFile, backupFile);
    }

    public static void backupFile(File source, File backup) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            // Check if the source file exists
            if (!source.exists()) {
                System.out.println("Source file does not exist: " + source.getPath());
                return;
            }

            // Create input and output streams for copying the file
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(backup);

            byte[] buffer = new byte[1024];
            int length;

            // Read data from source file and write it to the backup file
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            System.out.println("File backup successful! Backup file created at: " + backup.getPath());

        } catch (IOException e) {
            System.err.println("Error occurred while backing up the file: " + e.getMessage());
        } finally {
            // Close the streams
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing file streams: " + e.getMessage());
            }
        }
    }
}
