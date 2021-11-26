package tema1.ej7;


import javax.swing.*;
import java.awt.*;

public class AnimalView extends JFrame {

    protected JPanel
            jPanel1,
            jPanel2;

    protected JTextField tfNombre,
            tfRaza,
            tfCaracs,
            tfPeso;

    protected JLabel jlNombre,
            jlRaza,
            jlCaracteristicas,
            jlPeso;

    protected JButton bGuardar,
            bNuevo,
            bModificar,
            bEliminar,
            bPrimero,
            bUltimo,
            bAnterior,
            bSiguiente;

    public AnimalView(){
        initializeView();
    }

    public void initializeView() {
        this.setTitle("Gestor de Animales");
        this.setLayout(new GridLayout(0,1));

        jPanel1 = new JPanel(new GridLayout(4,2));
        this.add(jPanel1);
        jlNombre = new JLabel("Nombre: ");
        jlRaza = new JLabel("Raza: ");
        jlCaracteristicas = new JLabel("Caracteristicas: ");
        jlPeso = new JLabel("Peso: ");

        tfNombre = new JTextField();
        tfRaza = new JTextField();
        tfCaracs = new JTextField();
        tfPeso = new JTextField();

        jPanel1.add(jlNombre);
        jPanel1.add(tfNombre);
        jPanel1.add(jlRaza);
        jPanel1.add(tfRaza);
        jPanel1.add(jlCaracteristicas);
        jPanel1.add(tfCaracs);
        jPanel1.add(jlPeso);
        jPanel1.add(tfPeso);

        jPanel2 = new JPanel(new GridLayout(2,4));

        bGuardar = new JButton("Guardar");
        bNuevo = new JButton("Nuevo");
        bModificar = new JButton("Modificar");
        bEliminar = new JButton("Eliminar");
        bPrimero = new JButton("|<");
        bAnterior = new JButton("<");
        bSiguiente = new JButton(">");
        bUltimo = new JButton(">|");

        jPanel2.add(bGuardar);
        jPanel2.add(bNuevo);
        jPanel2.add(bModificar);
        jPanel2.add(bEliminar);
        jPanel2.add(bPrimero);
        jPanel2.add(bAnterior);
        jPanel2.add(bSiguiente);
        jPanel2.add(bUltimo);

        this.add(jPanel2);
        this.setSize(400, 200);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}