package com.ues.hotel.modelo;

/**
 * Estados posibles de una reserva.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public enum EstadoReserva {
    ACTIVA("Activa"),
    CANCELADA("Cancelada");

    private final String etiqueta;

    EstadoReserva(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
