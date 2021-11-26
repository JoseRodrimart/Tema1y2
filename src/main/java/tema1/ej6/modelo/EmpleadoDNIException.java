package tema1.ej6.modelo;

public class EmpleadoDNIException extends Exception{
    public EmpleadoDNIException() {
        super("El formato del DNI es incorrecto, debe constar únicamente de "+Empleado.DNI_EXTENSION+" dígitos");
    }
}
