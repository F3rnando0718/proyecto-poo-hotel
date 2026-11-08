package com.ues.hotel.seguridad;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class HashContrasena {

    private static final int ITERACIONES = 1000;
    private static final int LONGITUD_CLAVE = 256;

    public static String generarHash(String contrasena) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] sal = new byte[16];
            random.nextBytes(sal);

            KeySpec spec = new PBEKeySpec(contrasena.toCharArray(), sal, ITERACIONES, LONGITUD_CLAVE);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return ITERACIONES + ":" + Base64.getEncoder().encodeToString(sal) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }

    public static boolean verificarContrasena(String contrasenaIngresada, String contrasenaAlmacenada) {
        try {
            String[] partes = contrasenaAlmacenada.split(":");
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] sal = Base64.getDecoder().decode(partes[1]);
            byte[] hashAlmacenado = Base64.getDecoder().decode(partes[2]);

            KeySpec spec = new PBEKeySpec(contrasenaIngresada.toCharArray(), sal, iteraciones, LONGITUD_CLAVE);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashIngresado = factory.generateSecret(spec).getEncoded();

            if (hashAlmacenado.length != hashIngresado.length) {
                return false;
            }
            for (int i = 0; i < hashAlmacenado.length; i++) {
                if (hashAlmacenado[i] != hashIngresado[i]) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}