package com.ues.hotel.pruebas;

import com.ues.hotel.comparacion.EscenarioOrientadoAObjetos;
import com.ues.hotel.estructurado.ProgramaEstructurado;
import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.modelo.Reserva;
import com.ues.hotel.modelo.TipoHabitacion;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import com.ues.hotel.persistencia.RepositorioArchivo;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Casos de prueba ejecutables sin librerías externas.
 * Ejecutar con: clic derecho en el archivo > Run File (NetBeans).
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class PruebasHotel {

    private interface Accion {
        void ejecutar() throws Exception;
    }

    private static int total = 0;
    private static int aprobadas = 0;

    public static void main(String[] args) throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate e = hoy.plusDays(5);
        LocalDate s = hoy.plusDays(8);

        Hotel h = new Hotel("Prueba");
        Cliente ana = h.registrarCliente("Ana Martínez", "01234567-8", "7000-0001", "ana@correo.com");
        verificar("CP-01", "Registrar cliente válido", h.getClientes().size() == 1 && ana.getId() == 1);
        verificar("CP-02", "Rechazar DUI duplicado",
                rechaza(() -> h.registrarCliente("Otra", "01234567-8", "", "")));
        verificar("CP-03", "Rechazar DUI con formato inválido",
                rechaza(() -> h.registrarCliente("Luis", "123", "", "")));

        h.registrarHabitacion(101, TipoHabitacion.SENCILLA);
        h.registrarHabitacion(201, TipoHabitacion.SUITE);
        verificar("CP-04", "Rechazar habitación duplicada",
                rechaza(() -> h.registrarHabitacion(101, TipoHabitacion.DOBLE)));
        verificar("CP-05", "Sin reservas, todas las habitaciones están disponibles",
                h.consultarHabitacionesDisponibles(e, s).size() == 2);

        Reserva r = h.realizarReserva(ana.getId(), 201, e, s);
        verificar("CP-06", "Reserva exitosa (3 noches, suite)", r.getNoches() == 3 && r.estaActiva());
        verificar("CP-07", "Rechazar reserva con fechas solapadas",
                rechaza(() -> h.realizarReserva(ana.getId(), 201, e.plusDays(1), s.plusDays(1))));
        verificar("CP-08", "Permitir reserva que inicia el día de salida de otra",
                h.realizarReserva(ana.getId(), 201, s, s.plusDays(2)) != null);
        verificar("CP-09", "Rechazar salida igual o anterior a la entrada",
                rechaza(() -> h.realizarReserva(ana.getId(), 101, e, e)));
        verificar("CP-10", "Rechazar entrada anterior a hoy",
                rechaza(() -> h.realizarReserva(ana.getId(), 101, hoy.minusDays(1), hoy.plusDays(1))));
        verificar("CP-11", "Cancelar reserva libera la habitación", cancelaYLibera(h, r, e, s));
        verificar("CP-12", "Rechazar cancelar dos veces", rechaza(() -> h.cancelarReserva(r.getId())));

        Hotel h2 = new Hotel("Costos");
        Cliente c = h2.registrarCliente("Carlos", "07654321-0", "", "");
        h2.registrarHabitacion(101, TipoHabitacion.SENCILLA);
        h2.registrarHabitacion(201, TipoHabitacion.SUITE);
        Reserva suite = h2.realizarReserva(c.getId(), 201, e, s);
        Reserva sencilla = h2.realizarReserva(c.getId(), 101, e, e.plusDays(2));
        verificar("CP-13", "Costo suite 3 noches = $335.61 (297.00 + IVA 38.61)",
                suite.calcularSubtotal() == 297.00 && suite.calcularIva() == 38.61
                && h2.calcularCosto(suite.getId()) == 335.61);
        verificar("CP-14", "Costo sencilla 2 noches = $79.10", sencilla.calcularTotal() == 79.10);
        verificar("CP-15", "Consultar reservas de un cliente y solo activas",
                h2.consultarReservasDeCliente(c.getId()).size() == 2
                && h2.consultarReservas(true).size() == 2);
        verificar("CP-16", "Enfoque estructurado y orientado a objetos dan el mismo resultado",
                ProgramaEstructurado.ejecutarEscenario().equals(EscenarioOrientadoAObjetos.ejecutarEscenario()));


        // ---- Persistencia
        Path carpeta = Files.createTempDirectory("hotel-test");
        Path archivo = carpeta.resolve("datos").resolve("hotel.txt");
        RepositorioArchivo repo = new RepositorioArchivo(archivo);
        verificar("CP-17", "Cargar sin archivo previo devuelve vacío", repo.cargar().isEmpty());

        Hotel hp = new Hotel("Hotel | Persistente");
        hp.setRepositorio(repo);
        Cliente cp = hp.registrarCliente("María | Ñandú", "01234567-8", "7000-0001", "m@correo.com");
        hp.registrarHabitacion(101, TipoHabitacion.DOBLE);
        hp.registrarHabitacion(201, TipoHabitacion.SUITE);
        Reserva r1 = hp.realizarReserva(cp.getId(), 101, e, s);
        Reserva r2 = hp.realizarReserva(cp.getId(), 201, e, s);
        hp.cancelarReserva(r2.getId());
        Hotel cargado = repo.cargar().orElseThrow();
        verificar("CP-18", "Guardar y cargar conserva clientes, habitaciones y reservas",
                cargado.getNombre().equals("Hotel | Persistente")
                && cargado.getClientes().size() == 1
                && cargado.getClientes().get(0).getNombre().equals("María | Ñandú")
                && cargado.getHabitaciones().size() == 2
                && cargado.consultarReservas(false).size() == 2
                && cargado.consultarReservas(true).size() == 1
                && cargado.calcularCosto(r1.getId()) == r1.calcularTotal());
        Cliente nuevo = cargado.registrarCliente("Pedro", "07654321-0", "", "");
        Reserva r3 = cargado.realizarReserva(nuevo.getId(), 201, e, s);
        verificar("CP-19", "Tras cargar, los identificadores continúan sin repetirse",
                nuevo.getId() == 2 && r3.getId() == 3);
        Files.write(archivo, java.util.List.of("HOTEL|1|X", "R|1|9|9|2026-01-01|2026-01-02|ACTIVA"),
                StandardCharsets.UTF_8);
        verificar("CP-20", "Un archivo dañado se rechaza con un mensaje claro", rechaza(() -> repo.cargar()));

        System.out.println();
        System.out.println("Resultado: " + aprobadas + " de " + total + " pruebas aprobadas.");
        if (aprobadas != total) {
            System.exit(1);
        }
    }

    private static boolean cancelaYLibera(Hotel h, Reserva r, LocalDate e, LocalDate s) throws HotelException {
        h.cancelarReserva(r.getId());
        return h.consultarHabitacionesDisponibles(e, s).size() == 2;
    }

    private static boolean rechaza(Accion accion) {
        try {
            accion.ejecutar();
            return false;
        } catch (HotelException ex) {
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static void verificar(String id, String descripcion, boolean ok) {
        total++;
        if (ok) {
            aprobadas++;
        }
        System.out.println(String.format("%-6s %-65s %s", id, descripcion, ok ? "APROBADO" : "FALLÓ"));
    }
}
