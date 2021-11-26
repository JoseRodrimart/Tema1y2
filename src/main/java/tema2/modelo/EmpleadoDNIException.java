package tema2.modelo;
//Excepción que se lanza cuando se intenta crear un empleado con DNI de formato incorrecto
public class EmpleadoDNIException extends Exception{
    public EmpleadoDNIException() {
        super("El formato del DNI es incorrecto, debe constar únicamente de "+Empleado.DNI_EXTENSION+" dígitos");
    }
}
