package tema2.modelo;

/**
 * Clase Empleado. Los empleados disponen de DNI, Nombre y salario
 * Respecto a la implementación a través de RAFs, podemos prescindir de todos los atributos que nos limitaban el tamaño en memoria de cada empleado
 */
public class Empleado{

    //El dni seguimos limitandolo a 8 por motivos evidentes
    public static final int DNI_EXTENSION = 8;

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

    public void setNombre(String nombre) {
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
     */
    public Empleado(String dni, String nombre, int salario) throws EmpleadoDNIException {
        this(dni);

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

    //Establecemos un override de equals para establecee que dos empleados son iguales cuando sus dni coinciden
    @Override
    public boolean equals(Object empleado) {
        if(empleado.getClass() == this.getClass()){
            Empleado otroEmpleado = (Empleado) empleado;
            return this.dni.equals(otroEmpleado.dni);
        }
        else return false;
    }
}

