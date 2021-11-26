package tema1.ej6.modelo;

public class EmpleadoNombreException extends Exception{
    public EmpleadoNombreException() {
        super("El nombre de un empleado no puede tener más de " + Empleado.NOMBRE_MAX_EXTENSION + " caracteres.");
    }
}
