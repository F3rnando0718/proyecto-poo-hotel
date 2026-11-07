package com.ues.hotel.modelo;

/**
 * Habitación para una persona.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class HabitacionSencilla extends Habitacion {

    private static final double TARIFA = 35.00;

    public HabitacionSencilla(int numero) {
        super(numero);
    }

    @Override
    public TipoHabitacion getTipo() {
        return TipoHabitacion.SENCILLA;
    }

    @Override
    public int getCapacidad() {
        return 1;
    }

    @Override
    public double getTarifaNoche() {
        return TARIFA;
    }
}
