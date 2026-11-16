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
import com.mongodb.client.MongoDatabase;
import com.ues.hotel.persistencia.ConexionMongo;
import com.ues.hotel.persistencia.UsuarioRepositorioMongo;
import com.ues.hotel.seguridad.ServicioAutenticacion;
import com.ues.hotel.ui.LoginFrame;
import com.ues.hotel.servicio.RepositorioHotel;
import com.ues.hotel.persistencia.RepositorioHotelMongo;

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

        // Obtener conexión con MongoDB
        MongoDatabase database = ConexionMongo.obtenerInstancia();

        if (database == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar con MongoDB. Verifique que el servicio esté iniciado.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Crear el repositorio y el servicio de autenticación
        UsuarioRepositorioMongo repositorioUsuarios =
                new UsuarioRepositorioMongo(database);

        ServicioAutenticacion autenticacion =
                new ServicioAutenticacion(repositorioUsuarios);

        // Cargar los datos del hotel
Hotel hotel = abrirHotel(new RepositorioHotelMongo(database));          
        // Mostrar pantalla de inicio de sesión
        LoginFrame login = new LoginFrame(autenticacion, hotel);
        login.setVisible(true);
    });
}
    /** Carga los datos guardados; si no existen (primera ejecución) crea datos de ejemplo. */
    static Hotel abrirHotel(RepositorioHotel repositorio) {
        Hotel hotel = null;
        try {
            hotel = repositorio.cargar().orElse(null);
        } catch (HotelException e) {
             JOptionPane.showMessageDialog(
            null, e.getMessage(), "Error al cargar",
            JOptionPane.ERROR_MESSAGE
    );
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
