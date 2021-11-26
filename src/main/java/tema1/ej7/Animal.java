package tema1.ej7;

public class Animal {

    public Animal() {
    }

    public Animal(String nombre, String raza, String caracteristicas, double peso) {
        this.caracteristicas = caracteristicas;
        this.nombre = nombre;
        this.raza = raza;
        this.peso = peso;
    }

    private String nombre;
    private String raza;
    private String caracteristicas;
    private double peso;

    public String getNombre() {
        return nombre;
    }
    public String getRaza() {
        return raza;
    }
    public String getCaracteristicas() {
        return caracteristicas;
    }
    public double getPeso() {
        return peso;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "nombre='" + nombre + '\'' +
                ", raza='" + raza + '\'' +
                ", caracteristicas='" + caracteristicas + '\'' +
                ", peso=" + peso +
                '}';
    }
}
