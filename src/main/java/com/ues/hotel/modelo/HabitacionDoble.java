package com.ues.hotel.modelo;

/**
 * Habitación para dos personas.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class HabitacionDoble extends Habitacion {

    private static final double TARIFA = 55.00;

    public HabitacionDoble(int numero) {
        super(numero);
    }

    @Override
    public TipoHabitacion getTipo() {
        return TipoHabitacion.DOBLE;
    }

    @Override
    public int getCapacidad() {
        return 2;
    }

    @Override
    public double getTarifaNoche() {
        return TARIFA;
    }
}
