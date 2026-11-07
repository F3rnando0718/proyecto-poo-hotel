package com.ues.hotel.modelo;

/**
 * Clase abstracta que modela una habitación del hotel.
 * Las subclases definen su tipo, capacidad y tarifa (POLIMORFISMO): el resto del
 * sistema trabaja con {@code Habitacion} sin conocer el tipo concreto.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public abstract class Habitacion {

    private final int numero;

    protected Habitacion(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de habitación debe ser positivo.");
        }
        this.numero = numero;
    }

    /** Fábrica: crea la subclase que corresponde al tipo indicado. */
    public static Habitacion crear(int numero, TipoHabitacion tipo) {
        return switch (tipo) {
            case SENCILLA -> new HabitacionSencilla(numero);
            case DOBLE -> new HabitacionDoble(numero);
            case SUITE -> new HabitacionSuite(numero);
        };
    }

    public int getNumero() {
        return numero;
    }

    public abstract TipoHabitacion getTipo();

    public abstract int getCapacidad();

    /** Tarifa por noche en dólares, antes de IVA. */
    public abstract double getTarifaNoche();

    @Override
    public String toString() {
        return "Hab. " + numero + " (" + getTipo().getEtiqueta() + ")";
    }
}
