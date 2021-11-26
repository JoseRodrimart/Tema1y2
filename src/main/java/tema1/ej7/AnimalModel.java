package tema1.ej7;

import java.util.ArrayList;

public class AnimalModel {

    protected ArrayList<Animal> animales;
    protected static int i = 0;

    public AnimalModel() {
        animales = new ArrayList<Animal>();
    }

    public void posicion(String posicion) {
        switch(posicion) {
            case "|<" : i = 0; break;
            case "<" : if(i > 0)i--;break;
            case ">" : if(i < animales.size() - 1)i++; break;
            case ">|" : i = animales.size()-1;
        }
    }

    public void guardar(Animal animal) {
        animales.add(animal);
    }

    public void eliminar() {
        animales.remove(i);
    }

    public int animalExiste(String nombre) {
        int index = -1;
        for(Animal a:animales) {
            if(a.getNombre().equals(nombre))
                index = animales.indexOf(a);
        }
        return index;
    }

    public String nombre() {
        return animales.get(i).getNombre();
    }
    public String caracteristicas() {
        return animales.get(i).getCaracteristicas();
    }
    public String raza() {
        return animales.get(i).getRaza();
    }
    public double peso() {
        return animales.get(i).getPeso();
    }

}