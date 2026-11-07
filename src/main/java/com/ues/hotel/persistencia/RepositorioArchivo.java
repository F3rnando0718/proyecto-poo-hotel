package com.ues.hotel.persistencia;

import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.modelo.EstadoReserva;
import com.ues.hotel.modelo.Habitacion;
import com.ues.hotel.modelo.Reserva;
import com.ues.hotel.modelo.TipoHabitacion;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import com.ues.hotel.servicio.RepositorioHotel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Persistencia en un archivo de texto UTF-8 legible, sin librerías externas.
 * Un registro por línea, campos separados por «|»:
 * <pre>
 * HOTEL|1|nombre
 * C|id|nombre|dui|telefono|correo
 * H|numero|TIPO
 * R|id|idCliente|numeroHabitacion|entrada|salida|ESTADO
 * </pre>
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class RepositorioArchivo implements RepositorioHotel {

    private static final String ENCABEZADO = "HOTEL";
    private static final String VERSION = "1";

    private final Path archivo;

    public RepositorioArchivo(Path archivo) {
        this.archivo = archivo;
    }

    public Path getArchivo() {
        return archivo;
    }

    // ------------------------------------------------------------------ guardar

    @Override
    public void guardar(Hotel hotel) throws HotelException {
        List<String> lineas = new ArrayList<>();
        lineas.add(ENCABEZADO + "|" + VERSION + "|" + escapar(hotel.getNombre()));
        for (Cliente c : hotel.getClientes()) {
            lineas.add(String.join("|", "C", String.valueOf(c.getId()), escapar(c.getNombre()),
                    escapar(c.getDui()), escapar(c.getTelefono()), escapar(c.getCorreo())));
        }
        for (Habitacion h : hotel.getHabitaciones()) {
            lineas.add(String.join("|", "H", String.valueOf(h.getNumero()), h.getTipo().name()));
        }
        for (Reserva r : hotel.consultarReservas(false)) {
            lineas.add(String.join("|", "R", String.valueOf(r.getId()), String.valueOf(r.getCliente().getId()),
                    String.valueOf(r.getHabitacion().getNumero()), r.getEntrada().toString(),
                    r.getSalida().toString(), r.getEstado().name()));
        }
        try {
            Path carpeta = archivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            // Se escribe primero un temporal y luego se reemplaza, para no dejar el archivo a medias.
            Path temporal = archivo.resolveSibling(archivo.getFileName() + ".tmp");
            Files.write(temporal, lineas, StandardCharsets.UTF_8);
            Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new HotelException("No se pudieron guardar los datos en " + archivo + ": " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------- cargar

    @Override
    public Optional<Hotel> cargar() throws HotelException {
        if (!Files.exists(archivo)) {
            return Optional.empty();
        }
        List<String> lineas;
        try {
            lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new HotelException("No se pudo leer el archivo de datos " + archivo + ": " + e.getMessage());
        }

        Hotel hotel = null;
        List<Cliente> clientes = new ArrayList<>();
        List<Habitacion> habitaciones = new ArrayList<>();
        List<Reserva> reservas = new ArrayList<>();
        Map<Integer, Cliente> clientePorId = new HashMap<>();
        Map<Integer, Habitacion> habitacionPorNumero = new HashMap<>();

        int numeroLinea = 0;
        try {
            for (String linea : lineas) {
                numeroLinea++;
                if (linea.isBlank()) {
                    continue;
                }
                String[] f = linea.split("\\|", -1);
                switch (f[0]) {
                    case ENCABEZADO -> {
                        if (!VERSION.equals(f[1])) {
                            throw new IllegalArgumentException("versión de archivo no soportada");
                        }
                        hotel = new Hotel(desescapar(f[2]));
                    }
                    case "C" -> {
                        Cliente c = new Cliente(Integer.parseInt(f[1]), desescapar(f[2]), desescapar(f[3]),
                                desescapar(f[4]), desescapar(f[5]));
                        clientes.add(c);
                        clientePorId.put(c.getId(), c);
                    }
                    case "H" -> {
                        Habitacion h = Habitacion.crear(Integer.parseInt(f[1]), TipoHabitacion.valueOf(f[2]));
                        habitaciones.add(h);
                        habitacionPorNumero.put(h.getNumero(), h);
                    }
                    case "R" -> {
                        Cliente c = clientePorId.get(Integer.parseInt(f[2]));
                        Habitacion h = habitacionPorNumero.get(Integer.parseInt(f[3]));
                        if (c == null || h == null) {
                            throw new IllegalArgumentException("la reserva hace referencia a datos inexistentes");
                        }
                        reservas.add(new Reserva(Integer.parseInt(f[1]), c, h, LocalDate.parse(f[4]),
                                LocalDate.parse(f[5]), EstadoReserva.valueOf(f[6])));
                    }
                    default -> throw new IllegalArgumentException("tipo de registro desconocido");
                }
            }
        } catch (RuntimeException e) {
            throw new HotelException("El archivo de datos está dañado (línea " + numeroLinea + "): " + e.getMessage());
        }
        if (hotel == null) {
            throw new HotelException("El archivo de datos está dañado: falta el encabezado.");
        }
        hotel.restaurarDatos(clientes, habitaciones, reservas);
        return Optional.of(hotel);
    }

    // ---------------------------------------------------------------- utilidades

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("|", "\\p").replace("\n", "\\n").replace("\r", "");
    }

    private static String desescapar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '\\' && i + 1 < texto.length()) {
                char s = texto.charAt(++i);
                sb.append(s == 'p' ? '|' : s == 'n' ? '\n' : s);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
