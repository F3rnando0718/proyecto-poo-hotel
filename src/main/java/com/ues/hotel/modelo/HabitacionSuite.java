package com.ues.hotel.modelo;

/**
 * Suite: tarifa base más un 10 % por servicios adicionales.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class HabitacionSuite extends Habitacion {

    private static final double TARIFA_BASE = 90.00;
    private static final double RECARGO_SERVICIOS = 0.10;

    public HabitacionSuite(int numero) {
        super(numero);
    }

    @Override
    public TipoHabitacion getTipo() {
        return TipoHabitacion.SUITE;
    }

    @Override
    public int getCapacidad() {
        return 4;
    }

    @Override
    public double getTarifaNoche() {
        return Math.round(TARIFA_BASE * (1 + RECARGO_SERVICIOS) * 100.0) / 100.0;
    }
}
