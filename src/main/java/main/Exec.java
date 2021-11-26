package main;

import java.util.Scanner;

public class Exec {
    public static void main(String[] args) {
        while(true){
            System.out.print(
                    "------------------------------------------------------------------------------------------------------------------------------\n" +
                    "Ejercicios de Acceso A Datos Tema 1 y 2.\n" +
                    "Programas disponibles:\n" +
                    "1. Realiza una aplicación que muestre las propiedades del usuario en el sistema.\n" +
                    "2. Investiga la viabilidad e inconvenientes del cambio de las propiedades del sistema desde nuestras aplicaciones Java.\n" +
                    "3. Crea un fichero con propiedades.\n" +
                    "4. Realiza la aplicación que genere un fichero XML con datos de personas como en el ejercicio 1.\n" +
                    "5. Realiza la aplicación que lea y muestre los datos del fichero XML creado por la anterior aplicación.\n" +
                    "6. Realiza una aplicación que, mediante RandomAccessFile, genere un fichero con datos de empleados de una empresa.\n" +
                    "7. Gestor de animales haciendo uso del patrón MVC.\n" +
                    "8. Gestor de empleados haciendo uso de JDBC.\n" +
                    "9. Salir del programa.\n" +
                    "------------------------------------------------------------------------------------------------------------------------------\n" +
                    "Escoja la opción deseada:");
            Scanner sc = new Scanner(System.in);
            switch (sc.nextLine()){
                case "1": tema1.ej1.Ej1Java.main(new String[0]);break;
                case "2": tema1.ej2.Ej2Java.main(new String[0]);break;
                case "3": tema1.ej3.Ej3Java.main(new String[0]);break;
                case "4": tema1.ej4.Ej4Java.main(new String[0]);break;
                case "5": tema1.ej5.Ej5Java.main(new String[0]);break;
                case "6": tema1.ej6.Ej6Java.main(new String[0]);break;
                case "7": tema1.ej7.Ej7Java.main(new String[0]);break;
                case "8": tema2.ejJDBCJava.main(new String[0]);break;
                case "9":
                    System.out.println("Cerrando el programa...");
                    System.exit(0);
                default:
                    System.out.println("Opción invalida.");
            }
        }
    }
}
