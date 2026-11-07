package com.ues.hotel.modelo;

/**
 * Tipos de habitación que ofrece el hotel.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public enum TipoHabitacion {
    SENCILLA("Sencilla"),
    DOBLE("Doble"),
    SUITE("Suite");

    private final String etiqueta;

    TipoHabitacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
