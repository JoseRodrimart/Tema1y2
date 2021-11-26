package tema1.ej7;

//import javax.swing.*;

import javax.swing.*;

public class AnimalController {

    protected AnimalView animalView;
    protected AnimalModel animalModel;


    public AnimalController(AnimalView animalView, AnimalModel animalModel) {
        this.animalView = animalView;
        this.animalModel = animalModel;

        animalView.bPrimero.addActionListener(x -> { posicion(animalView.bPrimero.getText()); });
        animalView.bUltimo.addActionListener(x -> { posicion(animalView.bUltimo.getText()); });
        animalView.bAnterior.addActionListener(x -> { posicion(animalView.bAnterior.getText()); });
        animalView.bSiguiente.addActionListener(x -> { posicion(animalView.bSiguiente.getText()); });

        animalView.bNuevo.addActionListener(x -> { clear(); });
        animalView.bGuardar.addActionListener(x -> { nuevo(); });
        animalView.bModificar.addActionListener(x -> { modificar(); });
        animalView.bEliminar.addActionListener(x -> { eliminar(); });

    }

    private void posicion(String posicion) {
        if(animalModel.animales.isEmpty()) {
            JOptionPane.showMessageDialog(animalView, "Aún no hay animales registrados. ");
            return;
        }
        animalModel.posicion(posicion);
        animalView.tfNombre.setText(animalModel.nombre());
        animalView.tfRaza.setText(animalModel.raza());
        animalView.tfCaracs.setText(animalModel.caracteristicas());
        animalView.tfPeso.setText(Double.toString(animalModel.peso()));
    }

    private void clear() {
        animalView.tfNombre.setText("");
        animalView.tfRaza.setText("");
        animalView.tfCaracs.setText("");
        animalView.tfPeso.setText("");
    }

    private void nuevo() {
        String nombre, raza, caracteristicas;
        double peso;

        if(animalView.tfNombre.getText().isEmpty()
                || animalView.tfRaza.getText().isEmpty()
                || animalView.tfCaracs.getText().isEmpty()
                || animalView.tfPeso.getText().isEmpty()) {
            Util.InfoMessage("Es necesario introducir todos los campos","Campos requeridos");
            //JOptionPane.showMessageDialog(animalView, "Es obligatorio introducir todos los campos.");
        } else {
            if(Util.isNumber(animalView.tfPeso.getText())) {
                nombre = animalView.tfNombre.getText();
                raza = animalView.tfRaza.getText();
                caracteristicas = animalView.tfCaracs.getText();
                peso = Double.parseDouble(animalView.tfPeso.getText());
                Animal animal = new Animal(nombre, raza, caracteristicas, peso);
                animalModel.guardar(animal);
                JOptionPane.showMessageDialog(animalView, "Registro realizado.");
            } else {
                JOptionPane.showMessageDialog(animalView, "El peso no puede contener caracteres.");
            }
        }
        clear();
    }

    private void modificar() {
        if(animalModel.animales.isEmpty()) {
            JOptionPane.showMessageDialog(animalView, "Registro vacío. ");
            return;
        }
        String respuesta = JOptionPane.showInputDialog(animalView, "Introduce el nombre del animal");
        int busqueda = animalModel.animalExiste(respuesta);
        if(busqueda != -1) {
            JOptionPane.showMessageDialog(animalView, "Modifica los datos y guarda los cambios.");
            AnimalModel.i = busqueda;
            animalView.tfNombre.setText(animalModel.nombre());
            animalView.tfRaza.setText(animalModel.raza());
            animalView.tfCaracs.setText(animalModel.caracteristicas());
            animalView.tfPeso.setText(Double.toString(animalModel.peso()));
            animalModel.eliminar();
        } else {
            JOptionPane.showMessageDialog(animalView, "El animal no existe");
        }
    }

    private void eliminar() {
        if(animalModel.animales.isEmpty()) {
            JOptionPane.showMessageDialog(animalView, "Aún no hay animales registrados. ");
            return;
        }
        int resultado = JOptionPane.showConfirmDialog(animalView,
                "¿Quieres eliminar el animal " +
                        animalModel.animales.get(AnimalModel.i).getNombre() + "?" );

        if (resultado == 0) {
            animalModel.eliminar();
            JOptionPane.showMessageDialog(animalView, "Animal eliminado");
            clear();
        }

    }

}