
package com.ues.hotel.modelo;

/**
 * Habitación para dos personas.
 */
public class HabitacionDoble extends Habitacion {

    private static final int CAPACIDAD = 2;
    private static final double TARIFA = 55.00;

    public HabitacionDoble(int numero) {
        super(numero, CAPACIDAD, TARIFA);
    }

    @Override
    public TipoHabitacion getTipo() {
        return TipoHabitacion.DOBLE;
    }
}
