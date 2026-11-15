package com.ues.hotel.modelo;

import com.ues.hotel.servicio.Validador;
/**
 * Huésped que realiza reservas en el hotel. Hereda de {@link Persona}.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class Cliente extends Persona {

    private final String dui;

    public Cliente(int id, String nombre, String dui,
                   String telefono, String correo) {

        super(id, nombre, telefono, correo);
        this.dui = Validador.validarDui(dui);
    }

    public String getDui() {
        return dui;
    }

    @Override
    public String getDescripcion() {
        return "Cliente: " + getNombre() + " (DUI " + dui + ")";
    }

    @Override
    public String toString() {
        return getNombre() + " (" + dui + ")";
    }
}