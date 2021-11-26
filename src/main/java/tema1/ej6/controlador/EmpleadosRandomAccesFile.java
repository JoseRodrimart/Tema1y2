package tema1.ej6.controlador;

import tema1.ej6.modelo.Empleado;
import tema1.ej6.modelo.EmpleadoDNIException;
import tema1.ej6.modelo.EmpleadoNombreException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

/**
 * Clase que permite gestionar instanciar un gestor de ficheros de lectura aleatoria con gestión directa de objetos Empleado.
 * Introduce un banquillo para las repeticiones, el cual es indexado en memoria durante la creación de la instancia de clase.
 */
public class EmpleadosRandomAccesFile extends RandomAccessFile {

    private static final int ULTIMAS_CIFRAS_PARA_INDICE = 3;
    private static final File ficheroEmpleados = new File("empleados.bin");
    /**
     * Indice que guarda un mapa dni - posición en bytes en el fichero. Con esto obtendremos la posición de un usuario rápidamente si este se encuentra en el banquillo
     * El tamaño del indice, mucho menor que el area principal nos permite realizar esto.
     * (100.000 usuarios en banquillo serían unos 24 megas en memoria RAM.)
     */
    private Map<String,Long> indiceBanquillo;
    /**
     * Esta variable nos notifica al hilo principal de que el indexado ha sido completado.
     * *La palabra volatile es necesaria para que el contenido de esta variable sea accesible desde todos los hilos de ejecución de nuestro programa
     */
    private volatile boolean indiceCompleto = false;

    /**
     * Almacenamos en un Thread la función de indexado de banquillo
     */
    private final Thread indexaBanquilloThread = new Thread(this::indexaBanquillo);

    /**
     * Instancia un EmpleadoRandomAccesFile y lanza el hilo de indexado de banquillo
     *
     * @param mode the mode
     * @throws IOException the io exception
     */
    public EmpleadosRandomAccesFile(String mode) throws IOException {
        super(ficheroEmpleados, mode);
        if(ficheroEmpleados.length()<100000){ //Cuando creamos el fichero por primera vez, le damos un tamaño inicial de 100000 que será el tamaño mínimo para todos los DNIs (1000) sin repeticiones
            System.out.println("Creando el fichero de datos...");
            setLength(100000);
        }
        indexaBanquilloThread.start();
    }

    private void indexaBanquillo() { //En esta funcion SI gestionamos las excepciones para poder ejecutarla en un hilo, por simplicidad, cerramos el programa en caso de fallo al generar el índice
        try {
            indiceBanquillo = new Hashtable<String, Long>();
            seek(100000); //Nos posicionamos en la primera posicion del banquillo
            int dni;
            //AtomicBoolean fin = new AtomicBoolean(false); //Este tipo de booleano es necesario para poder gestionar su valor dentro de una lambda
            while(getFilePointer()!=length()){
                    var pos = getFilePointer();
                    var empleado = readEmpleadoRAF();
                //Si hay un siguiente empleado lo guardamos en el índice
                empleado.ifPresent(value -> indiceBanquillo.put(value.getDni(), pos));
                }
            indiceCompleto = true;
        } catch (EmpleadoDNIException | EmpleadoNombreException | IOException e) {
            System.err.println("Se ha producido un error generando el índice del área de excepción, cerrando el programa...");
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Escribe un empleado, bien en el área de RAF si está disponible o bien en el área de banquillo si existe una coincidencia.
     *
     * @param empleado Empleado a guardar
     * @throws IOException Error de escritura o lectura en el fichero
     */
    public void writeEmpleado(Empleado empleado) throws IOException {
        if(!compruebaEmpleadoEnRaf(empleado.getDni())){ //Si no existe el usuario en el área de RAF, pasamos a comprobar si está disponible la posición correspondiente
            seek(getPosicionCorrespondienteEnRaf(empleado.getDni())); //vamos a la posición correspondiente
            if(readUTF().isBlank()){ //Si ese espacio está disponible, lo guardamos ahí
                seek(getPosicionCorrespondienteEnRaf(empleado.getDni()));
                writeEmpleadoRAF(empleado);
            }
            else
                writeEmpleadoBanquillo(empleado); //Si ese espacio está ocupado, acudimos al área de banquillo
        }
    }

    //Funcion privada, guarda un empleado en la posición actual del cursor.
    private void writeEmpleadoRAF(Empleado empleado) throws IOException {
        var posInicial = getFilePointer();
        writeUTF(empleado.getDni());
        writeInt(empleado.getSalario());
        writeUTF(empleado.getNombre());
        var posFinal = getFilePointer();
    }

    //Funcion privada, guarda un empleado en la posición libre siguiente del baquillo.
    private void writeEmpleadoBanquillo(Empleado empleado) throws IOException {
        esperaIndiceCompleto(); //Para escribir en el banquillo, tenemos que asegurarnos de que el hilo de gestión del banquillo no está trabajando.
        seek(ficheroEmpleados.length());
        writeEmpleadoRAF(empleado);
        indiceBanquillo.put(empleado.getDni(),getFilePointer()); //Guardamos el nuevo empleado en el índice de banquillo
    }

    /**
     * Busca y devuelve un Optional de empleado. Si se encuentra en el sistema, tanto en el área de RAF, como en el banquillo, contendrá su valor, si no, un NULL.
     *
     * @param dni del usuario a buscar.
     * @return Opcional con el empleado o un null si no está.
     * @throws IOException             Ha ocurrido un problema leyendo el fichero
     * @throws EmpleadoDNIException    El dni del usuario solicitado está mal registrado en el fichero
     * @throws EmpleadoNombreException El nombre del empleado está mal guardado en el fichero
     */
    public Optional<Empleado> readEmpleado(String dni) throws IOException, EmpleadoDNIException, EmpleadoNombreException {
        Optional<Empleado> empleadoReturn = Optional.empty();
        var pos = getPosicionCorrespondienteEnRaf(dni);
        seek(getPosicionCorrespondienteEnRaf(dni));
        empleadoReturn = readEmpleadoRAF();
        if(empleadoReturn.isEmpty()) //Si nos devuelve un usario vacío desde RAF, no puede haber en el banquillo, devolvemos Optional vacío
            return empleadoReturn; //Se lo devolvemos vacío
        else{ //si nos devuelve usuario hay dos opciones, que sea el que buscamos o uno que coinciden sus ultimas cifras
            if(!empleadoReturn.get().getDni().equals(dni)){ //si no es el que buscamos, acudimos al banquillo.
                empleadoReturn = readEmpleadoBanquillo(dni);
            }
            else
                return empleadoReturn; //si coinciden, es que tenemos el que buscabamos.
        }
        return empleadoReturn;
    }

    //Lee un empleado (mediante lectura aleatoria) en la actual posición del cursor, si es que lo hay
    private Optional<Empleado> readEmpleadoRAF() throws IOException, EmpleadoDNIException, EmpleadoNombreException {
        Optional<Empleado>empleado = Optional.empty();
System.out.println("en readEmpleadoRaf"+ getFilePointer());
        String dni = readUTF();
        if(dni.matches("^[0-9]{"+Empleado.DNI_EXTENSION+"}$")) { //Regex que verifica que ha extraido un dni (solo 9 dígitos)
            int salario = readInt();
            String nombre = readUTF();
            empleado = Optional.of(new Empleado(dni.toString(), nombre, salario));
        }
        return empleado;
    }

    //Lee un empledo en el indice, si está ahí
    private Optional<Empleado> readEmpleadoBanquillo(String dni) throws EmpleadoDNIException, EmpleadoNombreException, IOException {
        while (!indiceCompleto) { //El hilo principal queda bloqueado mientras se finaliza el indexado del banquillo
            Thread.onSpinWait();
        }
        if(compruebaEmpleadoEnBanquillo(dni)){
                seek(getPosicionEnBanquillo(dni).orElseThrow(()->new IOException("Se ha producido un error leyendo el banquillo")));
                return readEmpleadoRAF();
        }
        return Optional.empty();
    }

    /**
     * Elimina empleado.
     *
     * @param dni the dni
     * @throws IOException          the io exception
     * @throws EmpleadoDNIException the empleado dni exception
     */
    public void eliminaEmpleado(String dni) throws IOException, EmpleadoDNIException, EmpleadoNombreException {
        var empleado = new Empleado(dni);
        if(compruebaEmpleadoEnRaf(empleado.getDni())) { //Si ese usuario está en la zona de RAF, lo eliminamos
            seek(getPosicionCorrespondienteEnRaf(empleado.getDni()));
            eliminaEmpleado();
            devuelveBanquillosARaf(dni); //Si hay algún candidato en el banquillo a ocupar la posición liberada, lo traemos del banquillo al RAF.
        }
        else //Si no, lo buscamos en la zona de banquillo. //Gracias al indice es más rápido que leer el usuario de la posición de Raf, y nos evita posibles inconsistencias con desfase banquillo - zona RAF
            if(compruebaEmpleadoEnBanquillo(dni)) {
                eliminaEmpleadoBanquillo(dni);
            }
    }

    //Funcion privada para eliminar a un usario que se encuentra en el banquillo
    private void eliminaEmpleadoBanquillo(String dni) throws IOException, EmpleadoDNIException, EmpleadoNombreException {
        esperaIndiceCompleto();
        var posicionEnBanquillo = getPosicionEnBanquillo(dni);
        if(posicionEnBanquillo.isPresent()) {
            seek(posicionEnBanquillo.get());
            //eliminaEmpleado();//No necesitamos eliminarlo, el reordenamiento lo va a destruir1
            indiceBanquillo.remove(dni); //Retiramos el usuario del banquillo
            reordenaBanquillo(posicionEnBanquillo.get()); //reordena el fichero, eliminado los espacios vacíos en el banquillo, para que se genere correctamente el índice e la siguiente ejecución
        }
    }

    //Función que se ejecuta tras el borrado de un empleado del area de RAF, para desplazar posibles dnis del área de banquillo que puedan ocupar esta plaza.
    private void devuelveBanquillosARaf(String dni) {
        esperaIndiceCompleto();
        String dniCifrasCoincidentes = getCifrasNecesariasDni(dni);
        indiceBanquillo
                .keySet()
                .stream()
                .filter(x-> getCifrasNecesariasDni(x).equals(dniCifrasCoincidentes)) //Filtra el banquillo y se queda con los posibles dnis que coinciden
                .findAny()
                .ifPresent(dniDesplazar ->{
                    Optional<Empleado> emp = Optional.empty();
                    try {
                        emp = readEmpleadoBanquillo(dniDesplazar);
                    } catch (EmpleadoDNIException | EmpleadoNombreException | IOException e) {
                        e.printStackTrace();
                    }
                    emp.ifPresent(empleado -> {
                        try {
                            writeEmpleadoRAF(empleado);
                            System.out.println(dniDesplazar);
                            eliminaEmpleadoBanquillo(dniDesplazar); //Eliminamos el empleado del banquillo
                            reordenaBanquillo(indiceBanquillo.get(dniDesplazar)); //Reordenamos el banquillo
                            indiceBanquillo.remove(dniDesplazar); //lo eliminamos del indice
                        } catch (IOException | EmpleadoNombreException | EmpleadoDNIException e) {
                            e.printStackTrace();
                        }
                    });
                });
    }

    /**
    *       Compactamos la memoria correspondiente del banquillo, eliminando el espacio vacío dejado por un Empleado eliminado en el mismo
    */
    private void reordenaBanquillo(Long posInicioEmpEliminar) throws IOException, EmpleadoDNIException, EmpleadoNombreException {
        seek(posInicioEmpEliminar);
        readEmpleadoRAF();
        //Ahora nos encontramos donde acaba el empleado borrado
        var posFinalEmpEliminar = getFilePointer();
        //Aquí almacenamos todos los bytes restantes hasta el final
        byte[] datosAMover = new byte[(int)(length()-posFinalEmpEliminar)];
        read(datosAMover);
        //Nos posicionamos donde comenzaba el empleado borrado
        seek(posInicioEmpEliminar);
        //Escribimos el array de bytes guardado
        write(datosAMover);
        //Redimensionamos para que elimine las posiciones innecesarias desde el punto actual hasta el final
        setLength(getFilePointer());
    }

    //Funcion privada que escribe tantos bytes en 0 cómo ocupa un empleado en la posición actual
    private void eliminaEmpleado() throws IOException {
        write(new byte[Empleado.MAX_TAM_EMPLEADO_BYTES]);
    }

    /**
     * Comprueba si un DNI ya se encuentra registrado en el sistema.
     *
     * @param dni  dni
     * @return booleano si está o no registrado ese DNI
     * @throws IOException             the io exception
     * @throws EmpleadoDNIException    the empleado dni exception
     * @throws EmpleadoNombreException the empleado nombre exception
     */
    public boolean compruebaEmpleadoPorDni(String dni) throws IOException, EmpleadoDNIException, EmpleadoNombreException {
        return readEmpleado(dni).isPresent();
    }

    //Devuelve true si ese dni ya está en espacio de RAF;
    private boolean compruebaEmpleadoEnRaf(String dni) throws IOException {
        seek(getPosicionCorrespondienteEnRaf(dni));
        var contenido = readUTF();
        return contenido.equals(dni);
    }

    private boolean compruebaEmpleadoEnBanquillo(String dni){
        esperaIndiceCompleto();
        return indiceBanquillo.containsKey(dni);
    }

    private long getPosicionCorrespondienteEnRaf(String dni) {
        return (long) Integer.parseInt(getCifrasNecesariasDni(dni)) * Empleado.MAX_TAM_EMPLEADO_BYTES;
    }

    private Optional<Long> getPosicionEnBanquillo(String dni){
        esperaIndiceCompleto();
        return Optional.of(indiceBanquillo.get(dni));
    }

    private String getCifrasNecesariasDni(String dni){
        return dni.substring(Empleado.DNI_EXTENSION-ULTIMAS_CIFRAS_PARA_INDICE);
    }

    //Función que lanzan las funciones que trabajan con el banquillo, para asegurarse de que el indice está íntegro
    private void esperaIndiceCompleto(){
        while (!indiceCompleto) { //El hilo principal queda bloqueado mientras se finaliza el indexado del banquillo
            Thread.onSpinWait();
        }
    }

    public void eliminaTodo() throws IOException {
        setLength(100000);
        seek(0);
        write(new byte[100000]);
        indiceBanquillo = new Hashtable<String,Long>(); //limpiamos el índice del banquillo
    }

    public void sobreescribe(Empleado empleado) throws EmpleadoDNIException, EmpleadoNombreException, IOException {
        if(compruebaEmpleadoPorDni(empleado.getDni())){
            if(compruebaEmpleadoEnRaf(empleado.getDni())){
                seek(getPosicionCorrespondienteEnRaf(empleado.getDni()));
                writeEmpleadoRAF(empleado);
            }
            else{
                eliminaEmpleadoBanquillo(empleado.getDni());
                writeEmpleadoBanquillo(empleado);
            }
        }
    }
}
