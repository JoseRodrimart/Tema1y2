package tema1.ej2;

//2. Investiga la viabilidad e inconvenientes del cambio de las propiedades del sistema desde nuestras aplicaciones Java.
public class Ej2Java {
    public static void main(String[] args) {
        System.out.println("Java nos permite acceder a multitud de sistemas del sistema en función del sistema operativo en el que se esté " +
                "ejecutando la JVM, pero la mayor parte de ellos están protegidos y no son editables, pero tal y como se indica en la documentación de java " +
                "(https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html)," +
                "es posible obtener permisos de edición de muchos de ellos. Java recomienda extremar la precaución al hacer estas ediciones," +
                "ya que podría tener consecuencias completamente inesperadas en el comportamiento del sistema operativo. \n" +
                "*Para ver como realizar estas ediciones se recomienda estudiar como funciona el Security Manager de la JVM: https://docs.oracle.com/javase/tutorial/essential/environment/security.html");
    }
}
