
package com.ues.hotel.modelo;

import com.ues.hotel.servicio.Validador;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Reserva de una habitación por un cliente en un rango de fechas.
 */
public class Reserva {

    public static final double IVA = 0.13;

    private final int id;
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final int cantidadHuespedes;
    private final LocalDate entrada;
    private final LocalDate salida;
    private EstadoReserva estado;

    public Reserva(int id, Cliente cliente, Habitacion habitacion,
            LocalDate entrada, LocalDate salida) {
        this(id, cliente, habitacion, 1, entrada, salida,
                EstadoReserva.ACTIVA);
    }

    public Reserva(int id, Cliente cliente, Habitacion habitacion,
            LocalDate entrada, LocalDate salida, EstadoReserva estado) {
        this(id, cliente, habitacion, 1, entrada, salida, estado);
    }

    public Reserva(int id, Cliente cliente, Habitacion habitacion,
            int cantidadHuespedes, LocalDate entrada, LocalDate salida) {
        this(id, cliente, habitacion, cantidadHuespedes,
                entrada, salida, EstadoReserva.ACTIVA);
    }

    /**
     * Constructor completo para crear o restaurar una reserva.
     */
    public Reserva(int id, Cliente cliente, Habitacion habitacion,
            int cantidadHuespedes, LocalDate entrada,
            LocalDate salida, EstadoReserva estado) {

        this.id = Validador.validarNumeroPositivo(
                id, "ID de reserva");

        if (cliente == null || habitacion == null
                || entrada == null || salida == null) {
            throw new IllegalArgumentException(
                    "Todos los datos de la reserva son obligatorios.");
        }

        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la de entrada.");
        }

        Validador.validarNumeroPositivo(
                cantidadHuespedes, "cantidad de huéspedes");

        if (cantidadHuespedes > habitacion.getCapacidad()) {
            throw new IllegalArgumentException(
                    "La cantidad de huéspedes supera la capacidad "
                    + "de la habitación.");
        }

        this.cliente = cliente;
        this.habitacion = habitacion;
        this.cantidadHuespedes = cantidadHuespedes;
        this.entrada = entrada;
        this.salida = salida;
        this.estado = estado == null
                ? EstadoReserva.ACTIVA : estado;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public int getCantidadHuespedes() {
        return cantidadHuespedes;
    }

    public LocalDate getEntrada() {
        return entrada;
    }

    public LocalDate getSalida() {
        return salida;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public boolean estaActiva() {
        return estado == EstadoReserva.ACTIVA;
    }

    public long getNoches() {
        return ChronoUnit.DAYS.between(entrada, salida);
    }

    public double calcularSubtotal() {
        return redondear(
                getNoches() * habitacion.getTarifaNoche());
    }

    public double calcularIva() {
        return redondear(calcularSubtotal() * IVA);
    }

    public double calcularTotal() {
        return redondear(calcularSubtotal() + calcularIva());
    }

    public void cancelar() {
        if (!estaActiva()) {
            throw new IllegalStateException(
                    "La reserva #" + id + " ya estaba cancelada.");
        }

        estado = EstadoReserva.CANCELADA;
    }

    public boolean seSolapaCon(
            LocalDate otraEntrada, LocalDate otraSalida) {

        if (otraEntrada == null || otraSalida == null
                || !otraSalida.isAfter(otraEntrada)) {
            throw new IllegalArgumentException(
                    "El rango de fechas proporcionado no es válido.");
        }

        return estaActiva()
                && otraEntrada.isBefore(salida)
                && otraSalida.isAfter(entrada);
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Reserva #" + id + " - "
                + cliente.getNombre() + " - " + habitacion;
    }
}
