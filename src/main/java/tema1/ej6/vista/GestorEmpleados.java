package tema1.ej6.vista;

import tema1.ej6.controlador.EmpleadoService;
import tema1.ej6.modelo.Empleado;
import tema1.ej6.modelo.EmpleadoDNIException;
import tema1.ej6.modelo.EmpleadoNombreException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Esta clase es la vista de la aplicación, gestiona todos los eventos que ocurren en la terminal, y la lectura de datos.
 * La gestión de excepciones se aplaza en todas las clases inferiores y son gestionadas aquí, para poder reaccionar correctamente a ellas
 */
public class GestorEmpleados {
    private static Scanner sc;
    private static EmpleadoService es;
    public static void start(){
        try {
            es = EmpleadoService.empleadoService();
            System.out.println("Conexion con el fichero de datos correcta");
        } catch (IOException e) {
            System.err.println("Se ha producido un error inicializando el gestor de registro de usuarios.");
            e.printStackTrace();
        }
        System.out.println("Bienvenido al gestor de empleados V1.0");
        boolean fin = false;
        while (!fin) {
            sc = new Scanner(System.in);
            //Ofrecemos al usuario la posibilidad de realizar un CRUD completo sobre el sistema
            System.out.print(
                            "--------------------------------------\n" +
                            "Escoja la acción a realizar:\n" +
                            "1) Registrar un nuevo empleado.\n" + //Create
                            "2) Obtener los datos de un determinado empleado.\n" + //Read
                            "3) Editar los datos de un empleado. [EN CONSTRUCCIÓN] \n" + //Update
                            "4) Eliminar un empleado. [EN CONSTRUCCIÓN]\n" +
                            "5) Borrar el registro completo.\n" +
                            "6) Cerrar el gestor de empleados.\n" +
                            "Introduzca la opción deseada: "); //Delete
            
            switch (sc.nextInt()) {
                case 1:
                    registraUsuario();break;
                case 2:
                    muestraUsuario();break;
                case 3:
                    editaUsuario();break;
                case 4:
                    eliminaUsuario();break;
                case 5:
                    eliminaTodo();break;
                case 6:
                    System.out.println("Cerrando el programa...");
                    es.finalizaEmpleados(); //A efectos prácticos, supone realizar un Close de todas las referencias internas, liberando memoria y cerrando ficheros
                    fin=true;
                    break;
                default:
                    System.out.println("Opcion inválida");
            }
        }
    }

    private static void registraUsuario() {
        Empleado empleado;
        String dni, nombre;
        int salario;

        System.out.println("Introduzca el dni del usuario a registrar");
        sc.nextLine();
        dni = sc.nextLine();

        try { //Generamos el empleado con el dni indicado. Saltará excepción si el dni o el nombre no tienen el formato correcto.
            empleado = new Empleado(dni);
            if(es.compruebaDni(dni)){
                System.out.println("El dni "+dni+" ya se encuentra registrado en el sistema.");
            }
            else {
                System.out.println("Introduzca el nombre:");
                nombre = sc.nextLine();
                empleado.setNombre(nombre);
                System.out.println("Introduzca el salario:");
                salario = sc.nextInt();
                empleado.setSalario(salario);
                es.registra(empleado);
            }
            System.out.println("Usuario registrado correctamente");
        } catch (EmpleadoDNIException | EmpleadoNombreException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void muestraUsuario(){
        String dni;
        System.out.println("Introduzca el dni del empleado a buscar:");
        sc.nextLine();
        dni = sc.nextLine();
        try {
            es.buscaEmpleadoPorDni(dni).ifPresentOrElse(
                    System.out::println,
                    ()->System.out.println("El usuario no se encuentra registrado"));
        } catch (EmpleadoDNIException | EmpleadoNombreException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void editaUsuario(){
        String dni;

        System.out.println("Introduzca el dni del empleado a editar:");
        sc.nextLine();
        dni = sc.nextLine();
        try {
            es.buscaEmpleadoPorDni(dni).ifPresentOrElse(
                    empleado ->{
                        System.out.println("Los datos actuales del empleado son: "+empleado);
                        System.out.println("Introduzca el nuevo nombre:");
                        String nombre = sc.nextLine();
                        try {
                            empleado.setNombre(nombre);
                            System.out.println("Introduzca el nuevo salario:");
                            int salario = sc.nextInt();
                            empleado.setSalario(salario);
                            es.sobreescribe(empleado);
                        } catch (EmpleadoNombreException | EmpleadoDNIException | IOException e) {
                            e.printStackTrace();
                        }
                    },
                    ()->{
                        System.out.println("El usuario no se encuentra registrado");
                    });
        } catch (EmpleadoDNIException | IOException | EmpleadoNombreException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void eliminaUsuario() {
        String dni;

        System.out.println("Introduzca el dni del empleado a eliminar:");
        sc.nextLine();
        dni = sc.nextLine();
        try {
            es.buscaEmpleadoPorDni(dni).ifPresentOrElse(
                    empleado -> {
                        System.out.println("Los datos actuales del empleado son: "+empleado);
                        System.out.println("Está seguro de que desea eliminar al empleado del registro?s/N");
                        String decision = sc.nextLine();
                        if(decision.toLowerCase().equals("s")) {

                            try {
                                es.eliminaPorDni(dni);
                            } catch (EmpleadoDNIException | EmpleadoNombreException | IOException e) {
                                System.err.println(e.getMessage());
                            }

                            System.out.println("Usuario eliminado del registro.");
                        }
                        else System.out.println("Cancelando acción...");
                        },
                    ()->{
                        System.out.println("El usuario no se encuentra registrado");
                    });
        } catch (EmpleadoDNIException | EmpleadoNombreException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void eliminaTodo() {
        System.out.println("Se van a limpiar todas las entradas del registro, ¿está seguro que desea hacerlo?s/N");
        sc.nextLine();
        if(sc.nextLine().toLowerCase().equals("s")){
            try{
                es.eliminaTodo();
                System.out.println("Registro eliminado correctamente.");
            } catch (IOException e) {
                System.err.println("Se ha producido un error borrando la información");
                e.printStackTrace();
            }
        }
    }
}
