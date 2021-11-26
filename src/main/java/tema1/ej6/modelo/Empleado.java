package tema1.ej6.modelo;

/**
 * Clase Empleado. Los empleados disponen de DNI, Nombre y salario
 */
public class Empleado implements Comparable {

    /**
     * EL DNI va a tener exactamente 8 digitos
     * En memoria ocupará 18 bytes. 16 del String + 2 del UTF
     */
    public static final int DNI_EXTENSION = 8;
    /**
     * Extensión maxima que se va a permitir que ocupe el nombre, 38 caracteres
     * En memoria 78 bytes MAX. 76 del String + 2 del UTF
     */
    public static final int NOMBRE_MAX_EXTENSION = 38; ;
    /**
     * Tamaño máximo en bytes que van a ocupar los datos de un Empleado usando UTF8
     *  dni + nombre + salario + 4 bytes de longitud de los StringUTF
     */
    public static final int MAX_TAM_EMPLEADO_BYTES = ((DNI_EXTENSION*2) + (NOMBRE_MAX_EXTENSION*2) + 4 +  4);

    private String dni;
    private String nombre;
    private int salario;

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSalario() {
        return salario;
    }

    public void setSalario(int salario) {
        this.salario = salario;
    }

    /**
     * Establece el nombre del usuario, lanza una excepción si se trata de guardar con un tamaño mayor al permitido
     * @param nombre el nombre debe de ser menor de lo establecido
     * @throws EmpleadoNombreException Excepción de nombre mayor al permitido
     */
    public void setNombre(String nombre) throws EmpleadoNombreException {
        if(nombre.length()>NOMBRE_MAX_EXTENSION)
            throw new EmpleadoNombreException();
        else
            this.nombre = nombre;
    }

    //Para evitar problemas de integridad en los datos, pero con vistas a implementaciones futuras, establecemos como privado el constructor por defecto.
    private Empleado(){
    }

    /**
     * Instancia un nuevo empleado unicamente con su DNI.
     * @param dni the dni
     * @throws EmpleadoDNIException Excepcion lanzada si el DNI no es de 8 digitos
     */
    public Empleado(String dni) throws EmpleadoDNIException {
        if(!(dni.length() == 8)) //Expresion regular para verificar que el dni es de los dígitos indicados
            throw new EmpleadoDNIException();
        else
            this.dni = dni;
    }

    /**
     *  Instancia un nuevo empleado completo
     *
     * @param dni dni
     * @param nombre nombre
     * @param salario salario
     * @throws EmpleadoDNIException Excepcion lanzada si el DNI no es de 8 digitos
     * @throws EmpleadoNombreException Excepción de nombre mayor al permitido
     */
    public Empleado(String dni, String nombre, int salario) throws EmpleadoDNIException, EmpleadoNombreException {
        this(dni);

        if(nombre.length()>NOMBRE_MAX_EXTENSION)
            throw new EmpleadoNombreException();
        else
            this.nombre = nombre;

        this.salario = salario;
    }

    @Override
    public String toString() {
        return  System.lineSeparator() +
                "Empleado{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", salario=" + salario +
                '}';
    }

    //Establecemos un override de equals para establecee que dos empleados son iguales cuando sus tres últimas cifras del dni coinciden
    @Override
    public boolean equals(Object empleado) {
        if(empleado.getClass() == this.getClass()){
            Empleado otroEmpleado = (Empleado) empleado;
            return this.dni.substring(6).equals(otroEmpleado.dni.substring(6));
        }
        else return false;
    }

    //El criterio para ordenar y comparar empleados es comparando las tres últimas cifras de sus dnis
    @Override
    public int compareTo(Object empleado) {
        var otroEmpleado = (Empleado)empleado;
        return this.dni.substring(6).compareTo(otroEmpleado.dni.substring(6));
    }
}

