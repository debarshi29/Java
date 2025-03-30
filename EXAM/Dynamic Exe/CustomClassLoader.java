import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomClassLoader extends ClassLoader {
    private final File classpath;

    public CustomClassLoader(File classpath) {
        this.classpath = classpath;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        // Convert the class name to a file path
        String path = classpath.getAbsolutePath() + File.separator + name.replace('.', File.separatorChar) + ".class";
        File classFile = new File(path);

        if (!classFile.exists()) {
            throw new ClassNotFoundException("Class not found: " + name);
        }

        try {
            byte[] classData = loadClassData(classFile);
            return defineClass(name, classData, 0, classData.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load class data", e);
        }
    }

    private byte[] loadClassData(File classFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(classFile);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
}
