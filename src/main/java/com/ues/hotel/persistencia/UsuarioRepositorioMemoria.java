package com.ues.hotel.persistencia;

import com.ues.hotel.seguridad.Usuario;
import com.ues.hotel.seguridad.UsuarioRepositorio;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UsuarioRepositorioMemoria implements UsuarioRepositorio {
    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return Optional.ofNullable(usuarios.get(usuario));
    }

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getUsuario(), usuario);
    }
}