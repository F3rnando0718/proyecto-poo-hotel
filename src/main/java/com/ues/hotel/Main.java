package com.ues.hotel;

import com.ues.hotel.persistencia.RepositorioArchivo;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import com.ues.hotel.ui.VentanaPrincipal;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación de escritorio.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class Main {

    /** Archivo de datos, relativo a la carpeta desde donde se ejecuta la aplicación. */
    private static final Path ARCHIVO_DATOS = Path.of("datos", "hotel.txt");

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no está disponible se usa el aspecto por defecto.
        }
        SwingUtilities.invokeLater(() -> {
            Hotel hotel = abrirHotel(new RepositorioArchivo(ARCHIVO_DATOS));
            new VentanaPrincipal(hotel).setVisible(true);
        });
    }

    /** Carga los datos guardados; si no existen (primera ejecución) crea datos de ejemplo. */
    static Hotel abrirHotel(RepositorioArchivo repositorio) {
        Hotel hotel = null;
        try {
            hotel = repositorio.cargar().orElse(null);
        } catch (HotelException e) {
            respaldarArchivoDanado(repositorio.getArchivo(), e.getMessage());
        }
        if (hotel == null) {
            hotel = new Hotel("Hotel Pequeño");
            hotel.cargarDatosDemo();
        }
        hotel.setRepositorio(repositorio);
        try {
            repositorio.guardar(hotel);
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        return hotel;
    }

    private static void respaldarArchivoDanado(Path archivo, String motivo) {
        Path respaldo = archivo.resolveSibling(archivo.getFileName() + ".danado");
        try {
            Files.move(archivo, respaldo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            // Si no se puede respaldar, se continúa de todas formas.
        }
        JOptionPane.showMessageDialog(null, motivo + "\nSe conservó una copia en " + respaldo
                + " y se iniciará con datos de ejemplo.", "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
