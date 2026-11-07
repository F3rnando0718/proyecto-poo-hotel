package com.ues.hotel.estructurado;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * ENFOQUE 1: PROGRAMACIÓN ESTRUCTURADA.
 * Datos globales en arreglos paralelos y procedimientos/funciones estáticas que los manipulan.
 * No hay objetos: cada "entidad" se reparte entre varios arreglos enlazados por posición.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public final class ProgramaEstructurado {

    private static final int MAX = 100;
    private static final double IVA = 0.13;

    // Tipos de habitación (códigos numéricos)
    private static final int SENCILLA = 0;
    private static final int DOBLE = 1;
    private static final int SUITE = 2;

    // Clientes (arreglos paralelos)
    private static String[] cliNombre = new String[MAX];
    private static String[] cliDui = new String[MAX];
    private static String[] cliTelefono = new String[MAX];
    private static String[] cliCorreo = new String[MAX];
    private static int cliTotal = 0;

    // Habitaciones
    private static int[] habNumero = new int[MAX];
    private static int[] habTipo = new int[MAX];
    private static int habTotal = 0;

    // Reservas
    private static int[] resCliente = new int[MAX];
    private static int[] resHabitacion = new int[MAX];
    private static LocalDate[] resEntrada = new LocalDate[MAX];
    private static LocalDate[] resSalida = new LocalDate[MAX];
    private static boolean[] resActiva = new boolean[MAX];
    private static int resTotal = 0;

    private ProgramaEstructurado() {
    }

    // ------------------------------------------------------------ Procedimientos

    static void reiniciar() {
        cliTotal = 0;
        habTotal = 0;
        resTotal = 0;
    }

    /** @return posición del cliente o -1 si el DUI ya existe. */
    static int registrarCliente(String nombre, String dui, String telefono, String correo) {
        for (int i = 0; i < cliTotal; i++) {
            if (cliDui[i].equals(dui)) {
                return -1;
            }
        }
        cliNombre[cliTotal] = nombre;
        cliDui[cliTotal] = dui;
        cliTelefono[cliTotal] = telefono;
        cliCorreo[cliTotal] = correo;
        return cliTotal++;
    }

    /** @return posición de la habitación o -1 si el número ya existe. */
    static int registrarHabitacion(int numero, int tipo) {
        for (int i = 0; i < habTotal; i++) {
            if (habNumero[i] == numero) {
                return -1;
            }
        }
        habNumero[habTotal] = numero;
        habTipo[habTotal] = tipo;
        return habTotal++;
    }

    static double tarifaNoche(int tipo) {
        switch (tipo) {
            case SENCILLA:
                return 35.00;
            case DOBLE:
                return 55.00;
            case SUITE:
                return Math.round(90.00 * 1.10 * 100.0) / 100.0;
            default:
                return 0;
        }
    }

    static String nombreTipo(int tipo) {
        switch (tipo) {
            case SENCILLA:
                return "Sencilla";
            case DOBLE:
                return "Doble";
            default:
                return "Suite";
        }
    }

    static boolean estaDisponible(int posHab, LocalDate entrada, LocalDate salida) {
        for (int i = 0; i < resTotal; i++) {
            if (resHabitacion[i] == posHab && resActiva[i]
                    && entrada.isBefore(resSalida[i]) && salida.isAfter(resEntrada[i])) {
                return false;
            }
        }
        return true;
    }

    static String listarDisponibles(LocalDate entrada, LocalDate salida) {
        StringBuilder lista = new StringBuilder();
        for (int i = 0; i < habTotal; i++) {
            if (estaDisponible(i, entrada, salida)) {
                if (lista.length() > 0) {
                    lista.append(", ");
                }
                lista.append(habNumero[i]);
            }
        }
        return lista.toString();
    }

    /** @return posición de la reserva, -1 cliente inexistente, -2 habitación inexistente, -3 ocupada. */
    static int reservar(int posCliente, int numeroHabitacion, LocalDate entrada, LocalDate salida) {
        if (posCliente < 0 || posCliente >= cliTotal) {
            return -1;
        }
        int posHab = -1;
        for (int i = 0; i < habTotal; i++) {
            if (habNumero[i] == numeroHabitacion) {
                posHab = i;
            }
        }
        if (posHab == -1) {
            return -2;
        }
        if (!estaDisponible(posHab, entrada, salida)) {
            return -3;
        }
        resCliente[resTotal] = posCliente;
        resHabitacion[resTotal] = posHab;
        resEntrada[resTotal] = entrada;
        resSalida[resTotal] = salida;
        resActiva[resTotal] = true;
        return resTotal++;
    }

    static boolean cancelar(int posReserva) {
        if (posReserva < 0 || posReserva >= resTotal || !resActiva[posReserva]) {
            return false;
        }
        resActiva[posReserva] = false;
        return true;
    }

    static long noches(int posReserva) {
        return ChronoUnit.DAYS.between(resEntrada[posReserva], resSalida[posReserva]);
    }

    static double subtotal(int posReserva) {
        double bruto = noches(posReserva) * tarifaNoche(habTipo[resHabitacion[posReserva]]);
        return Math.round(bruto * 100.0) / 100.0;
    }

    static double iva(int posReserva) {
        return Math.round(subtotal(posReserva) * IVA * 100.0) / 100.0;
    }

    static double total(int posReserva) {
        return Math.round((subtotal(posReserva) + iva(posReserva)) * 100.0) / 100.0;
    }

    // ------------------------------------------------------------------ Escenario

    /** Ejecuta el escenario de prueba y devuelve el resultado como texto. */
    public static String ejecutarEscenario() {
        reiniciar();
        StringBuilder out = new StringBuilder();
        LocalDate entrada = LocalDate.now().plusDays(10);
        LocalDate salida = LocalDate.now().plusDays(13);

        out.append("=== Escenario de prueba: sistema de reservas ===\n");
        int ana = registrarCliente("Ana Martínez", "01234567-8", "7000-0001", "ana@correo.com");
        int carlos = registrarCliente("Carlos Pérez", "07654321-0", "7000-0002", "carlos@correo.com");
        out.append("[1] Clientes registrados: ").append(cliTotal).append('\n');

        registrarHabitacion(101, SENCILLA);
        registrarHabitacion(102, DOBLE);
        registrarHabitacion(201, SUITE);
        out.append("[2] Habitaciones registradas: ").append(habTotal).append('\n');

        out.append("[3] Disponibles: ").append(listarDisponibles(entrada, salida)).append('\n');

        int reserva = reservar(ana, 201, entrada, salida);
        int posHab = resHabitacion[reserva];
        out.append("[4] Reserva #").append(reserva + 1).append(" creada: ").append(cliNombre[ana])
                .append(", Hab. ").append(habNumero[posHab]).append(" (").append(nombreTipo(habTipo[posHab]))
                .append("), ").append(noches(reserva)).append(" noche(s)\n");
        out.append(String.format(Locale.US, "    Subtotal $%.2f | IVA $%.2f | Total $%.2f%n",
                subtotal(reserva), iva(reserva), total(reserva)));

        int intento = reservar(carlos, 201, entrada, salida);
        if (intento < 0) {
            out.append("[5] Reserva de ").append(cliNombre[carlos])
                    .append(" en Hab. 201: RECHAZADA (habitación no disponible en esas fechas)\n");
        }

        out.append("[6] Disponibles tras la reserva: ").append(listarDisponibles(entrada, salida)).append('\n');

        if (cancelar(reserva)) {
            out.append("[7] Reserva #").append(reserva + 1).append(" cancelada\n");
        }
        out.append("[8] Disponibles tras cancelar: ").append(listarDisponibles(entrada, salida)).append('\n');
        return out.toString();
    }

    public static void main(String[] args) {
        System.out.println(ejecutarEscenario());
    }
}
