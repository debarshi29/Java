import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

class Person {
    private String name;

    private Person(String name) {
        this.name = name;
    }

    private void printName() {
        System.out.println("Name: " + name);
    }
}

public class ReflectionExample1 {
    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("Person");
            
            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            Object person = constructor.newInstance("John Doe");
            
            Field field = clazz.getDeclaredField("name");
            field.setAccessible(true);
            System.out.println("Name: " + field.get(person));
            
            Method method = clazz.getDeclaredMethod("printName");
            method.setAccessible(true);
            method.invoke(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

