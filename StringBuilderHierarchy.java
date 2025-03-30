import java.lang.reflect.Modifier;

public class StringBuilderHierarchy {

    public static void main(String[] args) {
        // Class for which you want to print the hierarchy
        Class<?> clazz1 = StringBuilder.class;
        Class<?> clazz2 = Class.class;
        Class<?> clazz = java.util.Scanner.class;

        // Print the inheritance hierarchy
        printClassHierarchy(clazz);
    }

    public static void printClassHierarchy(Class<?> clazz) {
        while (clazz != null) {
            // Print the class name and its modifiers
            System.out.println(Modifier.toString(clazz.getModifiers()) + " " + clazz.getName());
            // Move to the superclass
            clazz = clazz.getSuperclass();
        }
    }
}

