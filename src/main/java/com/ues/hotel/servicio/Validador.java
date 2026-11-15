/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.hotel.servicio;

import java.util.regex.Pattern;
/**
 *
 * @author ferch
 */
public final class Validador {

    private static final Pattern DUI =
            Pattern.compile("\\d{8}-\\d");

    private static final Pattern TELEFONO =
            Pattern.compile("[267]\\d{3}-?\\d{4}");

    private static final Pattern CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Validador() {
        // Evita crear objetos de esta clase.
    }

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio.");
        }

        return nombre.trim();
    }

    public static String validarDui(String dui) {
        if (dui == null || !DUI.matcher(dui.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El DUI debe tener el formato 00000000-0.");
        }

        return dui.trim();
    }

    public static String validarTelefono(String telefono) {
        if (telefono == null || !TELEFONO.matcher(telefono.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El teléfono debe tener 8 dígitos y comenzar con 2, 6 o 7.");
        }

        return telefono.trim();
    }

    public static String validarCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return "";
        }

        if (!CORREO.matcher(correo.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El correo electrónico no tiene un formato válido.");
        }

        return correo.trim();
    }

    public static int validarNumeroPositivo(int numero, String campo) {
        if (numero <= 0) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " debe ser mayor que cero.");
        }

        return numero;
    }

    public static double validarPrecio(double precio) {
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser un número válido y no negativo.");
        }

        return precio;
    }
}

