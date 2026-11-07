package com.ues.hotel.seguridad;

public class ServicioAutenticacion {
    private final UsuarioRepositorio repositorio;

    public ServicioAutenticacion(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public boolean autenticar(String nombreUsuario, String passwordPlano) {
        try {
            Optional<Usuario> optUsuario = repositorio.buscarPorUsuario(nombreUsuario);
            if (optUsuario.isPresent()) {
                Usuario u = optUsuario.get();
                if (u.isActivo()) {
                    return HashContrasena.verificarContrasena(passwordPlano, u.getPasswordHash());
                }
            }
        } catch (Exception e) {
            System.err.println("Error durante la autenticación: " + e.getMessage());
        }
        return false;
    }
}