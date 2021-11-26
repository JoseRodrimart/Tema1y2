package tema1.ej7;

import javax.swing.*;

public class Util {
    public static void ErrorMessage(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }
    public static void InfoMessage(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean isNumber(String numero) {
        try {
            Double.parseDouble(numero);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}

