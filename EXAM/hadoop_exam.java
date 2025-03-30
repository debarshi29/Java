//1 63
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class CommandServer {
    private static final int PORT = 12345;
    private static final int MAX_THREADS = 10; // Limit the number of concurrent threads
    private static final BlockingQueue<Socket> requestQueue = new LinkedBlockingQueue<>();
    
    public static void main(String[] args) {
        // Start the server socket
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            
            // Create a fixed thread pool
            ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);
            
            // Continuously accept incoming connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                // Add client socket to the queue
                requestQueue.add(clientSocket);
                
                // Process the request from the queue using a thread pool
                threadPool.execute(() -> {
                    try {
                        handleRequest(requestQueue.take());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void handleRequest(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);
                out.println("Echo: " + line); // Echo the received message
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
}
//2   127
import java.io.*;
import java.net.*;
import javax.net.ssl.*;

public class CommandClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        // Trust all certificates (for testing purposes only)
        // In production, use a proper TrustStore
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            } }, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            try (SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(SERVER_ADDRESS, SERVER_PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

                // Start SSL handshake
                socket.startHandshake();

                // Read welcome message
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println("Server: " + serverMessage);
                    if (serverMessage.endsWith("Username: ")) {
                        String username = userInput.readLine();
                        out.println(username);
                    } else if (serverMessage.endsWith("Password: ")) {
                        String password = userInput.readLine();
                        out.println(password);
                    } else if (serverMessage.contains("You can now send commands.")) {
                        break;
                    } else if (serverMessage.contains("Connection will be terminated.")) {
                        return;
                    }
                }

                // After authentication, send commands
                String command;
                System.out.println("Enter commands (type 'exit' to quit):");
                while (!(command = userInput.readLine()).equalsIgnoreCase("exit")) {
                    out.println(command);
                    String response = in.readLine();
                    System.out.println("Server: " + response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//3   mapper class
// File: src/main/java/com/example/WordCountMapper.java
package com.example;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    /**
     * The map method processes one line at a time.
     *
     * @param key     The byte offset of the current line in the file.
     * @param value   The content of the line.
     * @param context The Context allows the Mapper to interact with the rest of the Hadoop system.
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Convert the line to lowercase to make the count case-insensitive
        String line = value.toString().toLowerCase();

        // Use StringTokenizer to split the line into words
        StringTokenizer tokenizer = new StringTokenizer(line);
        while (tokenizer.hasMoreTokens()) {
            String currentWord = tokenizer.nextToken().replaceAll("[^a-zA-Z]", ""); // Remove punctuation
            if (!currentWord.isEmpty()) {
                word.set(currentWord);
                context.write(word, one);
            }
        }
    }
}
//reducer
// File: src/main/java/com/example/WordCountReducer.java
package com.example;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private IntWritable totalCount = new IntWritable();

    /**
     * The reduce method aggregates counts for each word.
     *
     * @param key     The word.
     * @param values  An iterable of counts (all are 1 in this example).
     * @param context The Context allows the Reducer to interact with the rest of the Hadoop system.
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        // Sum all counts for the current word
        for (IntWritable val : values) {
            sum += val.get();
        }
        totalCount.set(sum);
        context.write(key, totalCount);
    }
}
//driver
// File: src/main/java/com/example/WordCountDriver.java
package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountDriver {

    /**
     * The main method sets up the job configuration and initiates the MapReduce job.
     *
     * @param args Command-line arguments: input path and output path.
     * @throws Exception If the job fails.
     */
    public static void main(String[] args) throws Exception {
        // Check if the correct number of arguments is provided
        if (args.length != 2) {
            System.err.println("Usage: WordCountDriver <input path> <output path>");
            System.exit(-1);
        }

        // Create a new Job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Word Count");

        // Specify the JAR file that contains the driver, mapper, and reducer
        job.setJarByClass(WordCountDriver.class);

        // Set the Mapper and Reducer classes
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // Set the output key and value types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // Set the input and output paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Submit the job and wait for its completion
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
//4 FileBackUp. 
// File: src/main/java/com/example/backup/BackupUtility.java
package com.example.backup;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackupUtility {

    private final Path sourceDir;
    private final Path destinationDir;
    private final boolean incremental;
    private final LoggerUtil logger;
    private final ExecutorService executor;

    /**
     * Constructor to initialize the BackupUtility.
     *
     * @param sourceDir      Source directory to back up.
     * @param destinationDir Destination directory for backups.
     * @param incremental    If true, perform incremental backups.
     * @param logger         Logger utility for logging operations.
     */
    public BackupUtility(String sourceDir, String destinationDir, boolean incremental, LoggerUtil logger) {
        this.sourceDir = Paths.get(sourceDir);
        this.destinationDir = Paths.get(destinationDir);
        this.incremental = incremental;
        this.logger = logger;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Starts the backup process.
     */
    public void startBackup() {
        logger.log("Backup started at " + LocalDateTime.now());
        try {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = destinationDir.resolve(sourceDir.relativize(dir));
                    if (!Files.exists(targetDir)) {
                        Files.createDirectories(targetDir);
                        logger.log("Created directory: " + targetDir.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    executor.submit(() -> {
                        try {
                            Path targetFile = destinationDir.resolve(sourceDir.relativize(file));
                            if (incremental) {
                                if (Files.exists(targetFile)) {
                                    // Compare last modified times
                                    FileTime sourceTime = Files.getLastModifiedTime(file);
                                    FileTime targetTime = Files.getLastModifiedTime(targetFile);
                                    if (sourceTime.compareTo(targetTime) <= 0) {
                                        logger.log("Skipped (not modified): " + file.toString());
                                        return;
                                    }
                                }
                            }
                            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                            logger.log("Copied: " + file.toString() + " to " + targetFile.toString());
                        } catch (IOException e) {
                            logger.log("Error copying file: " + file.toString() + " - " + e.getMessage());
                        }
                    });
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.log("Backup failed: " + e.getMessage());
        } finally {
            shutdownExecutor();
            logger.log("Backup completed at " + LocalDateTime.now());
        }
    }

    /**
     * Shuts down the executor service gracefully.
     */
    private void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                    logger.log("Executor did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}


