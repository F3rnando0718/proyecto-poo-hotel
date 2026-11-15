
package com.ues.hotel.modelo;

/**
 * Habitación para una persona.
 */
public class HabitacionSencilla extends Habitacion {

    private static final int CAPACIDAD = 1;
    private static final double TARIFA = 35.00;

    public HabitacionSencilla(int numero) {
        super(numero, CAPACIDAD, TARIFA);
    }

    @Override
    public TipoHabitacion getTipo() {
        return TipoHabitacion.SENCILLA;
    }
}
