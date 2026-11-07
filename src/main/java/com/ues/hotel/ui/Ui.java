package com.ues.hotel.ui;

import com.ues.hotel.servicio.HotelException;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Utilidades compartidas por los paneles de la interfaz.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
final class Ui {

    static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private Ui() {
    }

    static LocalDate leerFecha(String texto, String campo) throws HotelException {
        try {
            return LocalDate.parse(texto.trim(), FECHA);
        } catch (DateTimeParseException e) {
            throw new HotelException("La fecha de " + campo + " no es válida. Use el formato dd/mm/aaaa.");
        }
    }

    static String dinero(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }

    static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    static JTable tabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        return tabla;
    }
}
