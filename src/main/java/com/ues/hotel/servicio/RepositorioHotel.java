package com.ues.hotel.servicio;

import java.util.Optional;

/**
 * Contrato de persistencia del hotel. {@link Hotel} depende de esta interfaz y no de una
 * tecnología concreta: se puede guardar en archivo, base de datos, etc. sin cambiar el servicio.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public interface RepositorioHotel {

    /** Guarda el estado completo del hotel. */
    void guardar(Hotel hotel) throws HotelException;

    /** Carga el hotel guardado, o vacío si todavía no existe información almacenada. */
    Optional<Hotel> cargar() throws HotelException;
}
