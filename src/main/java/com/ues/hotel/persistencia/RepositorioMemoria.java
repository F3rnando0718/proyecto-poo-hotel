package com.ues.hotel.persistencia;

import java.util.HashMap;
import java.util.Map;

public class RepositorioMemoria {
    // Almacenamiento genérico auxiliar en memoria si se requiere para pruebas rápidas
    private final Map<String, Object> cache = new HashMap<>();

    public void guardar(String clave, Object valor) {
        cache.put(clave, valor);
    }

    public Object obtener(String clave) {
        cache.get(clave);
        return cache.get(clave);
    }
}