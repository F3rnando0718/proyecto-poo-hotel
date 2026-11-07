package com.ues.hotel.modelo;

import java.util.regex.Pattern;

/**
 * Huésped que realiza reservas en el hotel. Hereda de {@link Persona}.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class Cliente extends Persona {

    private static final Pattern FORMATO_DUI = Pattern.compile("\\d{8}-\\d");

    private final String dui;

    public Cliente(int id, String nombre, String dui, String telefono, String correo) {
        super(id, nombre, telefono, correo);
        if (dui == null || !FORMATO_DUI.matcher(dui.trim()).matches()) {
            throw new IllegalArgumentException("El DUI debe tener el formato 00000000-0.");
        }
        this.dui = dui.trim();
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
