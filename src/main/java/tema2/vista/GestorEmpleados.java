package tema2.vista;

import tema2.controlador.Repositorio;
import tema2.modelo.Empleado;
import tema2.modelo.EmpleadoDNIException;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Esta clase es la vista de la aplicación, gestiona todos los eventos que ocurren en la terminal, y la lectura de datos.
 * La gestión de excepciones se aplaza en todas las clases inferiores y son gestionadas aquí, para poder reaccionar correctamente a ellas
 */
public class GestorEmpleados {
    private static Scanner sc;
    private static Repositorio es;

    public static void start(){

        System.out.println("Bienvenido al gestor de empleados V2.0");
        System.out.println("Para el correcto funcionamiento del programa, su BBDD PostgreSQL deberá estar disponible en localhost y disponer de la base de datos 'empresa'.\n"
        +"Introduzca el usuario de su BBDD:\n");
        sc = new Scanner(System.in);
        String usuario = sc.nextLine();
        System.out.println("Introduzca la contraseña");
        String pass = sc.nextLine();

        Repositorio.setCredenciales(usuario,pass);
        es = Repositorio.empleadoService();

        boolean fin = false;
        while (!fin) {
            //Ofrecemos al usuario la posibilidad de realizar un CRUD completo sobre el sistema
            System.out.print(
                            "--------------------------------------\n" +
                            "Escoja la acción a realizar:\n" +
                            "1) Registrar un nuevo empleado.\n" + //Create
                            "2) Obtener los datos de un determinado empleado.\n" + //Read
                            "3) Editar los datos de un empleado. \n" + //Update
                            "4) Eliminar un empleado.\n" +
                            "5) Borrar el registro completo.\n" +
                            "6) Cerrar el gestor.\n" +
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
                    System.out.println("Opción inválida");
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

        try { //Generamos el empleado con el dni indicado. Saltará excepción si el dni no tiene el formato correcto.
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
        } catch (EmpleadoDNIException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Se ha producido un error accediendo a la BBDD");
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
        } catch (SQLException throwables) {
            System.err.println("Se ha producido un error accediendo a la BBDD");
        } catch (EmpleadoDNIException e) {
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
                        } catch (SQLException e) {
                            System.err.println("Se ha producido un error accediendo a la BBDD");
                        }
                    },
                    ()->{
                        System.out.println("El usuario no se encuentra registrado");
                    });
        } catch (SQLException throwables) {
            System.err.println("Se ha producido un error accediendo a la BBDD");
        } catch (EmpleadoDNIException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void eliminaUsuario(){
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
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Usuario eliminado del registro.");
                        }
                        else System.out.println("Cancelando acción...");
                        },
                    ()->{
                        System.out.println("El usuario no se encuentra registrado");
                    });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (EmpleadoDNIException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void eliminaTodo() {
        System.out.println("Se van a limpiar todas las entradas del registro, ¿está seguro que desea hacerlo?s/N");
        sc.nextLine();
        if(sc.nextLine().toLowerCase().equals("s")){
            try{
                es.eliminaTodo();
            } catch (SQLException e) {
                System.err.println("Se ha producido un error borrando la información en la BBDD");
            }
        }
    }

}
