package tema1.ej6.controlador;

import tema1.ej6.modelo.Empleado;
import tema1.ej6.modelo.EmpleadoDNIException;
import tema1.ej6.modelo.EmpleadoNombreException;

import java.io.IOException;
import java.util.Optional;

/**
 * Servicio de la clase empleado, sirve de abstracción para el programador de la vista, le abstrae de las tareas de gestión del RAF, haciendo uso de la clase EmpleadosRandomAccesFile.
 * Esta clase sigue el patrón singleton, para asegurarnos que solo se realiza una apertura/programa del fichero
 */
public final class EmpleadoService {

    private EmpleadosRandomAccesFile empleadosRaf = null; //En lugar de un RAF clásico, disponemos de una clase propia que nos permite trabajar directamente con Empleados.
    private static EmpleadoService empleadoService = null;

    public EmpleadoService() throws IOException { //Constructor por defecto
        empleadosRaf = new EmpleadosRandomAccesFile("rw");
    }

    public static EmpleadoService empleadoService() throws IOException {
        if(empleadoService == null) //Si es la primera llamada, o se ha limpiado una instancia anterior, creamos la referencia de esta clase, y por consecuente, se vuelve a abrir el RAF
            empleadoService = new EmpleadoService();
        return empleadoService;
    }

    //En este método se destruye la referencia actual del singleton, se cierra el RAF, y por lo tanto, se limpia el índice de memoria
    public void finalizaEmpleados(){
        try {
            empleadosRaf.close();
        } catch (IOException e) {
            System.err.println("Se ha producido un error cerrando la conexión con el fichero");
        }
        empleadosRaf = null;
            empleadoService = null;
    }

    public void registra(Empleado empleado) {
        try{
                empleadosRaf.writeEmpleado(empleado);
            }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public boolean compruebaDni(String dni) throws IOException, EmpleadoDNIException, EmpleadoNombreException {
        return empleadosRaf.compruebaEmpleadoPorDni(dni);
    }

    public Optional<Empleado> buscaEmpleadoPorDni(String dni) throws EmpleadoDNIException, EmpleadoNombreException, IOException {
        System.out.println("En buscaEmpleadoPorDni "+ dni );
        return empleadosRaf.readEmpleado(dni);
    }

    public void eliminaPorDni(String dni) throws EmpleadoDNIException, IOException, EmpleadoNombreException {
        empleadosRaf.eliminaEmpleado(dni);
    }

    public void eliminaTodo() throws IOException {
        empleadosRaf.eliminaTodo();
    }

    public void sobreescribe(Empleado empleado) throws EmpleadoDNIException, EmpleadoNombreException, IOException {
        empleadosRaf.sobreescribe(empleado);
    }
}
