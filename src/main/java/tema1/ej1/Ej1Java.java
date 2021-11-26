package tema1.ej1;

//1. Realiza una aplicación que muestre las propiedades del usuario en el sistema.
public class Ej1Java {
    public static void main(String[] args) {
        System.out.println("Mostrando las propiedades del sistema del usuario:");
        System
                .getProperties() //Obtiene las propiedades del sistema
                    .entrySet() //Extrae el EntrySet que contiene el Properties
                    .stream() //Flujo de datos
                    .filter((x)->x.getKey().toString().startsWith("user")) //Filtramos todas lasclaves que comienzan pur User
                    .forEach(System.out::println); //Las mostramos por pantalla
    }
}
