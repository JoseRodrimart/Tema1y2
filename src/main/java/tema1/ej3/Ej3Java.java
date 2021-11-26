package tema1.ej3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * 3. Basándote en los apuntes y ejemplos de
 *         http://chuwiki.chuidiang.org/index.php?title=Leer_y_modificar_fichero_de_propiedades_en_java
 *         crea un fichero de propiedades con el siguiente contenido:
 *
 *         #Fichero de configuración de la aplicación X
 *         version=1.2.3
 *         lanzamiento=11/08/2021
 *         standalone=yes
 *         port=5858
 *
 *         Posteriormente el programa cargará estas propiedades, las modificará y actualizará a fichero modificando la fecha y la versión.
 */
public class Ej3Java {
    private final static String PUERTO = "5858";
    private final static String STANDALONE = "yes";
    private final static String VERSION = "1.2.3";
    private final static String VERSION_2 = "1.2.4 beta";
    private final static String COMENTARIO = "Fichero de configuración de la aplicación X";
    private final static String FICHERO = "ej3.props";

    public static void main(String[] args){
        var fichero = new File(FICHERO);
        final Properties propiedades;
        //String con la fecha actual formateada en dia-mes-año
        String fechaFormateada =
                new SimpleDateFormat("dd/MM/yyyy")
                        .format(new Date());

        if(!fichero.exists()){
            System.out.println("Fichero de propiedades no encontrado, se va a crear el siguiente fichero de propiedades:");

            propiedades = new Properties() {{ //Aprovechando las cualidades de Java 11, abrimos contexto en la instanciación del objeto y establecemos sus valores
                setProperty("version", VERSION);
                setProperty("lanzamiento", fechaFormateada);
                setProperty("standalone", STANDALONE);
                setProperty("port", PUERTO);
                list(System.out);
            }};

            System.out.println("Desea crear el fichero(N/s)?");
        }

        else {
            System.out.println("Estado actual del fichero de propiedades que se va a actualizar:");
            propiedades = new Properties(){{
                    try(var fis = new FileInputStream(fichero)){
                        load(fis);
                    }catch(IOException e){
                        e.printStackTrace();
                        System.err.println("Se ha producido un error en la carga del fichero, finalizando el programa...");
                        System.exit(0);
                    }
                    setProperty("lanzamiento", fechaFormateada);
                    setProperty("version",VERSION_2);
                    System.out.println("Estado del fichero tras la actualizacion: ");
                    list(System.out);
                }};
            System.out.println("Está de acuerdo con las modificaciones(N/s)?");
        }

        if(new Scanner(System.in).next().equalsIgnoreCase("s")){
            try(var fos = new FileOutputStream(fichero)){
                propiedades.store(fos,COMENTARIO);
                System.out.println("Completado, finalizando el programa...");
            }catch (IOException e) {
                e.printStackTrace();
                System.err.println("Se ha producido un error durante la escritura");
            }
        }

        else
            System.out.println("Cerrando el programa sin cambios...");

    }
}