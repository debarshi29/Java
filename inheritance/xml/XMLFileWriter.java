import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class XMLFileWriter {

    // Method to write key-value pairs to an XML file
    public void writeXML(String filePath, Map<String, String> properties) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("<?xml version=\"1.0\"?>");
            bw.newLine();
            bw.write("<configuration>");
            bw.newLine();

            for (Map.Entry<String, String> entry : properties.entrySet()) {
                bw.write("    <property>");
                bw.newLine();
                bw.write("        <name>" + entry.getKey() + "</name>");
                bw.newLine();
                bw.write("        <value>" + entry.getValue() + "</value>");
                bw.newLine();
                bw.write("    </property>");
                bw.newLine();
            }

            bw.write("</configuration>");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        XMLFileWriter xmlWriter = new XMLFileWriter();

        // Example properties to write to the XML file
        Map<String, String> properties = Map.of(
            "fs.defaultFS", "hdfs://localhost:9000",
            "dfs.replication", "3",
            "dfs.permissions", "false"
        );

        String filePath = "core-site.xml";
        xmlWriter.writeXML(filePath, properties);

        System.out.println("XML configuration file created: " + filePath);
    }
}

