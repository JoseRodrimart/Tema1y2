package tema1.ej7;


//
//7. Completa el código para el programa de ejemplo de gestión de animales visto en los apuntes,
//   haciendo uso del patrón MVC.
public class Ej7Java {
    public static void main(String args[]) {
        AnimalView animalView = new AnimalView();
        AnimalModel animalModel = new AnimalModel();
        AnimalController animalController = new AnimalController(animalView, animalModel);
    }
}
