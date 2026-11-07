package com.ues.hotel.modelo;

/**
 * Clase abstracta que representa a una persona del sistema.
 * Aplica ABSTRACCIÓN, ENCAPSULAMIENTO (atributos privados) y sirve de base
 * para la HERENCIA (Cliente extiende Persona).
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public abstract class Persona {

    private final int id;
    private final String nombre;
    private final String telefono;
    private final String correo;

    protected Persona(int id, String nombre, String telefono, String correo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.telefono = telefono == null ? "" : telefono.trim();
        this.correo = correo == null ? "" : correo.trim();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    /** Cada subclase decide cómo se describe (POLIMORFISMO). */
    public abstract String getDescripcion();
}
