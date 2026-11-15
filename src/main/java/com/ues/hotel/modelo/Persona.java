package com.ues.hotel.modelo;

import com.ues.hotel.servicio.Validador;
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
        this.id = Validador.validarNumeroPositivo(id, "ID");
        this.nombre = Validador.validarNombre(nombre);

        if (telefono == null || telefono.isBlank()) {
            this.telefono = "";
        } else {
            this.telefono = Validador.validarTelefono(telefono);
        }

        this.correo = Validador.validarCorreo(correo);
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