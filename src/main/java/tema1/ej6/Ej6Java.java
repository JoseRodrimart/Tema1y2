package tema1.ej6;

import tema1.ej6.vista.GestorEmpleados;

/**
 * <h3>Programa que permite la gestión de empleados a traves de un sencillo programa de terminal.</h3>
 * <p>Los datos son persistidos en un fichero binario de acceso aleatorio, el cual dispone de un area de "banquillo" para lidiar con las colisiones. La implementación se ha realizado con el fin de profundizar y estudiar los siguientes puntos:</p>
 * <li>Implementación del patrón de diseño MVC</li>
 * <li>Implementación de los patrones de diseño repositorio - servicio: El repositorio gestiona el acceso al modelo, y el servicio expone las funciones que usará la vista para manipular de manera sencilla el repositorio</li>
 * <li>Ampliación de clases bases de Java IO preparandolas para trabajar con objetos propios: La clase EmpleadosRandomAccesFile es una extension de RandomAccesFile que nos permite directamente manipulas Empleados</li>
 * <li>Gestión de procesos secundarios de nuestro programa en Hilos o Threads para aligerar la carga del hilo principal: Para optimizar el programa, la zona de excedencias es indexada en un Hashtable al inicio del programa para facilitar su busqueda durante la ejecución, y para evitar que el usuario tenga que esperar a que se complete el indexado, esto lo realizamos en un hilo de fondo. El usuario solo podrá notar este proceso si se hace una consulta rápidamente al inicio de la ejecución del programa que casualmente corresponda con la zona de excedencias.</li>
 * <li>Gestión de excepciones propias: tendremos una excepción que se lanzará cuando se trate de crear un usario con DNI incorrecto y otra con un nombre demasiado largo</li>
 * <p>Nota: La implementación de este programa tiene una compolejidad superior a la necesaria para la resolución directa del mismo, pero se ha realizado teniendo muy en cuenta su modularidad y extensibilidad para futuras versiones</p>
 */
//Punto de entrada de la aplicación
// TO-DO:
//    ESTADO DEL CRUD: Create: OK READ: OK UPDATE: OK+- (requiere que el delete funcione bien) DELETE: NOOOOOOOO
// -cuando se compacta el banquillo tras eliminar un empleado de este, se elimina el empleado siguiente en lugar del requerido
// -No funciona aún bien el devolver empleados desde el banquillo hasta el RAF cuando se elimina en el RAF uno con repeticion
public class Ej6Java {

    public static void main(String[] args){
        //La vista se ejecuta en el hilo principal
        GestorEmpleados.start();
    }
}
