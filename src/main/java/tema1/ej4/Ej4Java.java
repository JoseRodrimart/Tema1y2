package tema1.ej4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

//Realiza la aplicación que genere un fichero XML con datos de personas como en el ejercicio 1.
//Dado que en el ej1 se extrae la configuración del usuario en el sistema operativo, en este ej vamos a volcar esos datos en un XML
public class Ej4Java {
    public static void main(String[] args) {
        try(var fos = new FileOutputStream("propiedades.xml")){
            new Properties(){{
                putAll(System
                        .getProperties()
                        .entrySet()
                        .stream()
                        .filter((x)->x.getKey().toString().startsWith("user"))
                        .collect(
                                Collectors.toMap(
                                        e -> (String) e.getKey(),
                                        e -> (String) e.getValue()
                                )
                        )
                );
                storeToXML(fos,"Propiedades del sistema del usuario"); //Hacemos uso de la función Store to XML de las properties que nos vuelca las Entries del Map de un Properties un un fichero XML
            }};
            System.out.println("Fichero generado correctamente.");
        } catch (IOException e) {
            System.err.println("Se ha producido un error generando el fichero.");
        }
    }
}
