package com.ues.hotel.persistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ues.hotel.seguridad.HashContrasena;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class RepositorioMongo {

    private final MongoDatabase database;

    public RepositorioMongo() {
        this.database = ConexionMongo.obtenerInstancia();
    }

    // Método para validar el inicio de sesión del usuario
    public boolean validarLogin(String correoIngresado, String contrasenaIngresada) {
        try {
            MongoCollection<Document> coleccionUsuarios = database.getCollection("usuarios");
            Document usuario = coleccionUsuarios.find(eq("correo", correoIngresado)).first();

            if (usuario != null) {
                String hashAlmacenado = usuario.getString("contrasenaHash");
                // Verifica la contraseña ingresada contra el hash almacenado en MongoDB
                return HashContrasena.verificarContrasena(contrasenaIngresada, hashAlmacenado);
            }
        } catch (Exception e) {
            System.err.println("Error al validar el login: " + e.getMessage());
        }
        return false;
    }
}