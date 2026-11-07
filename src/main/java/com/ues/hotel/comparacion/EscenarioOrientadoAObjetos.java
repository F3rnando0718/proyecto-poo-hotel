package com.ues.hotel.comparacion;

import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.modelo.Habitacion;
import com.ues.hotel.modelo.Reserva;
import com.ues.hotel.modelo.TipoHabitacion;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * ENFOQUE 2: DISEÑO ORIENTADO A OBJETOS.
 * Ejecuta exactamente el mismo escenario que {@code ProgramaEstructurado}, pero usando
 * las clases del modelo (Cliente, Habitacion, Reserva) y el servicio Hotel.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public final class EscenarioOrientadoAObjetos {

    private EscenarioOrientadoAObjetos() {
    }

    public static String ejecutarEscenario() {
        StringBuilder out = new StringBuilder();
        LocalDate entrada = LocalDate.now().plusDays(10);
        LocalDate salida = LocalDate.now().plusDays(13);
        Hotel hotel = new Hotel("Hotel Demo");

        try {
            out.append("=== Escenario de prueba: sistema de reservas ===\n");
            Cliente ana = hotel.registrarCliente("Ana Martínez", "01234567-8", "7000-0001", "ana@correo.com");
            Cliente carlos = hotel.registrarCliente("Carlos Pérez", "07654321-0", "7000-0002", "carlos@correo.com");
            out.append("[1] Clientes registrados: ").append(hotel.getClientes().size()).append('\n');

            hotel.registrarHabitacion(101, TipoHabitacion.SENCILLA);
            hotel.registrarHabitacion(102, TipoHabitacion.DOBLE);
            hotel.registrarHabitacion(201, TipoHabitacion.SUITE);
            out.append("[2] Habitaciones registradas: ").append(hotel.getHabitaciones().size()).append('\n');

            out.append("[3] Disponibles: ").append(disponibles(hotel, entrada, salida)).append('\n');

            Reserva reserva = hotel.realizarReserva(ana.getId(), 201, entrada, salida);
            out.append("[4] Reserva #").append(reserva.getId()).append(" creada: ")
                    .append(reserva.getCliente().getNombre()).append(", ").append(reserva.getHabitacion())
                    .append(", ").append(reserva.getNoches()).append(" noche(s)\n");
            out.append(String.format(Locale.US, "    Subtotal $%.2f | IVA $%.2f | Total $%.2f%n",
                    reserva.calcularSubtotal(), reserva.calcularIva(), reserva.calcularTotal()));

            try {
                hotel.realizarReserva(carlos.getId(), 201, entrada, salida);
            } catch (HotelException e) {
                out.append("[5] Reserva de ").append(carlos.getNombre())
                        .append(" en Hab. 201: RECHAZADA (habitación no disponible en esas fechas)\n");
            }

            out.append("[6] Disponibles tras la reserva: ").append(disponibles(hotel, entrada, salida)).append('\n');

            hotel.cancelarReserva(reserva.getId());
            out.append("[7] Reserva #").append(reserva.getId()).append(" cancelada\n");
            out.append("[8] Disponibles tras cancelar: ").append(disponibles(hotel, entrada, salida)).append('\n');
        } catch (HotelException e) {
            out.append("ERROR: ").append(e.getMessage()).append('\n');
        }
        return out.toString();
    }

    private static String disponibles(Hotel hotel, LocalDate entrada, LocalDate salida) throws HotelException {
        List<Habitacion> lista = hotel.consultarHabitacionesDisponibles(entrada, salida);
        return lista.stream().map(h -> String.valueOf(h.getNumero())).collect(Collectors.joining(", "));
    }
}
