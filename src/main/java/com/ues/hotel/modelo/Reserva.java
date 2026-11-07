package com.ues.hotel.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Reserva de una habitación por un cliente en un rango de fechas.
 * Encapsula las reglas propias de una reserva: noches, costo y solapamiento.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class Reserva {

    public static final double IVA = 0.13;

    private final int id;
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final LocalDate entrada;
    private final LocalDate salida;
    private EstadoReserva estado;

    public Reserva(int id, Cliente cliente, Habitacion habitacion, LocalDate entrada, LocalDate salida) {
        this(id, cliente, habitacion, entrada, salida, EstadoReserva.ACTIVA);
    }

    /** Constructor usado al restaurar una reserva guardada (puede estar cancelada). */
    public Reserva(int id, Cliente cliente, Habitacion habitacion, LocalDate entrada, LocalDate salida,
            EstadoReserva estado) {
        if (cliente == null || habitacion == null || entrada == null || salida == null) {
            throw new IllegalArgumentException("Todos los datos de la reserva son obligatorios.");
        }
        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la de entrada.");
        }
        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.entrada = entrada;
        this.salida = salida;
        this.estado = estado == null ? EstadoReserva.ACTIVA : estado;
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
        return redondear(getNoches() * habitacion.getTarifaNoche());
    }

    public double calcularIva() {
        return redondear(calcularSubtotal() * IVA);
    }

    public double calcularTotal() {
        return redondear(calcularSubtotal() + calcularIva());
    }

    /** Marca la reserva como cancelada. */
    public void cancelar() {
        if (!estaActiva()) {
            throw new IllegalStateException("La reserva #" + id + " ya estaba cancelada.");
        }
        estado = EstadoReserva.CANCELADA;
    }

    /**
     * Indica si esta reserva (activa) ocupa la habitación en el rango dado.
     * El día de salida no cuenta como ocupado: otro huésped puede entrar ese día.
     */
    public boolean seSolapaCon(LocalDate otraEntrada, LocalDate otraSalida) {
        return estaActiva() && otraEntrada.isBefore(salida) && otraSalida.isAfter(entrada);
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Reserva #" + id + " - " + cliente.getNombre() + " - " + habitacion;
    }
}
