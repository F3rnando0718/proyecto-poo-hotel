package com.ues.hotel.seguridad;

public class Usuario{
    private String usuario;
    private String nombreCompleto;
    private String passwordHash;
    private boolean activo;
    
    public Usuario(String usuario, String nombreCompleto, String paswordHash, boolean activo){
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.passwordHash = passwordHash;
        this.activo = activo;
    }
    
    public String getUsuario(){
        return usuario;
    }
    
    public String getNombreCompleto(){
        return nombreCompleto;
    }
    
    public String getPasswordHash(){
        return passwordHash;
    }
    
    public boolean isActivo(){
        return activo;
    }

}
        