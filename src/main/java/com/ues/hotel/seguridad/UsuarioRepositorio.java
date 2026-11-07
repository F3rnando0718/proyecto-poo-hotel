package com.ues.hotel.seguridad;

import java.util.Optional;

public interface UsuarioRepositorio {
    Optional<Usuario> buscarPorUsuario(String usuario) throws Exception;
    void guardar(Usuario usuario) throws Exception;
}