package com.ues.hotel.servicio;

/**
 * Error de negocio del sistema de reservas (datos inválidos, conflictos, etc.).
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class HotelException extends Exception {

    private static final long serialVersionUID = 1L;

    public HotelException(String mensaje) {
        super(mensaje);
    }
}
