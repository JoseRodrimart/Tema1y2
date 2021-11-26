package tema1.ej5;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Ej5Java {
    public static void main(String[] args) {
        File file = new File("propiedades.xml");
        if(file.exists()){
            try(var fis = new FileInputStream(file)){
                new Properties(){{
                    loadFromXML(fis);
                    System.out.println("Las Propiedades del Usuario del sistema son las siguientes:");
                    entrySet()
                            .stream()
                            .filter((x)->x.getKey().toString().startsWith("user"))
                            .forEach(System.out::println);
                }};
            } catch (InvalidPropertiesFormatException e) {
                System.err.println("El fichero de propiedades tiene un formato inadecuado");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Se ha producido un error leyendo el fichero de propiedades");
                e.printStackTrace();
            }
        }
        else
            System.out.println("Fichero de propiedades no encontrado.");
    }
}
