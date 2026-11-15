
package com.ues.hotel.modelo;

/**
 * Suite con tarifa base más un 10 % por servicios adicionales.
 */
public class HabitacionSuite extends Habitacion {

    private static final int CAPACIDAD = 4;
    private static final double TARIFA_BASE = 90.00;
    private static final double RECARGO_SERVICIOS = 0.10;

    public HabitacionSuite(int numero) {
        super(numero, CAPACIDAD,
                TARIFA_BASE * (1 + RECARGO_SERVICIOS));
    }

    @Override
    public TipoHabitacion getTipo() {
        return TipoHabitacion.SUITE;
    }
}
