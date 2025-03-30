import java.lang.reflect.*;

public class ReflectionExample {
    public static void main(String[] args) {
        try {
            // Get the Class object associated with String
            Class<?> clazz = Class.forName("java.lang.String");

            // Get the declared methods of the class
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println("Method: " + method.getName());
            }

            // Get the constructors of the class
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                System.out.println("Constructor: " + constructor);
            }

            // Get the fields of the class
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                System.out.println("Field: " + field.getName());
            }

            // Create a new instance of the class
            Constructor<?> constructor = clazz.getConstructor(String.class);
            Object strInstance = constructor.newInstance("Hello Reflection");

            // Invoke a method on the instance
            Method method = clazz.getMethod("toUpperCase");
            Object result = method.invoke(strInstance);
            System.out.println("Result: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

