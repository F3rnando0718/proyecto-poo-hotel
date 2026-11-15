package com.ues.hotel.modelo;

import com.ues.hotel.servicio.Validador;
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
    private int capacidad;
    private double tarifaNoche;
    private EstadoHabitacion estado;

    protected Habitacion(int numero, int capacidad, double tarifaNoche) {
        this.numero = Validador.validarNumeroPositivo(
                numero, "número de habitación");
        this.capacidad = Validador.validarNumeroPositivo(
                capacidad, "capacidad");
        this.tarifaNoche = Validador.validarPrecio(tarifaNoche);
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    /**
     * Crea una habitación según el tipo seleccionado.
     */
    public static Habitacion crear(int numero, TipoHabitacion tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un tipo de habitación.");
        }

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

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = Validador.validarNumeroPositivo(
                capacidad, "capacidad");
    }

    public double getTarifaNoche() {
        return tarifaNoche;
    }

    public void setTarifaNoche(double tarifaNoche) {
        this.tarifaNoche = Validador.validarPrecio(tarifaNoche);
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado de la habitación es obligatorio.");
        }
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Hab. " + numero + " (" + getTipo().getEtiqueta() + ")";
    }
}