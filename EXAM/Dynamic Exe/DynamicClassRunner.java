import java.lang.reflect.Method;
import java.io.File; // Import the File class

public class DynamicClassRunner {
    public static void main(String[] args) {
        // Specify the path to the directory where the .class files are located
        File classpath = new File("/home/sysadm/Downloads");
        CustomClassLoader classLoader = new CustomClassLoader(classpath);

        try {
            // Load the class named "MyClass"
            Class<?> loadedClass = classLoader.loadClass("MyClass");

            // Optionally, create an instance of the class and invoke a method
            // For static methods, you can invoke them directly
            Method mainMethod = loadedClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
