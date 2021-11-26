package tema2;


import tema2.modelo.EmpleadoDNIException;
import tema2.vista.GestorEmpleados;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Ejercicio de JDBC, donde se ha empleado exactamente la misma estructura de proyecto que el ejercicio 6 del tema 1
 * El proyecto se ha realizado con la intención de estudiar los siguiente
 * <li>comprobar como la persistencia de datos en un gestor de base de datos simplifica enormemente el código frente al uso de acceso directo en ficheros binarios</li>
 * <li>Demostrar que la correcta implementación del modelo visto controlador ofrece un completo desacoplamiento entre la lógica de negocio y la de presentación, permitiendonos reutilizar el código de la vista con tan solo unos pequeños cambios en la intercepción de excepciones</li>
 */
public class ejJDBCJava {
    public static void main(String[] args){
        GestorEmpleados.start();
    }
}
