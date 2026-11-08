package com.ues.hotel;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ues.hotel.persistencia.ConexionMongo;
import com.ues.hotel.persistencia.RepositorioMongo;
import com.ues.hotel.seguridad.HashContrasena;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class TestLogin {
    public static void main(String[] args) {
        MongoDatabase db = ConexionMongo.obtenerInstancia();
        MongoCollection<Document> usuarios = db.getCollection("usuarios");

        // 1. Generamos el hash real usando PBKDF2 para la contraseña "Hotel2026!"
        String hashReal = HashContrasena.generarHash("Hotel2026!");

        // 2. Insertamos o actualizamos el usuario admin en MongoDB con su hash correcto
        Document existing = usuarios.find(eq("correo", "admin@hotel.com")).first();
        if (existing == null) {
            Document nuevoAdmin = new Document("nombreUsuario", "admin")
                    .append("correo", "admin@hotel.com")
                    .append("contrasenaHash", hashReal);
            usuarios.insertOne(nuevoAdmin);
            System.out.println("✔ Usuario admin creado con hash válido.");
        } else {
            usuarios.updateOne(eq("correo", "admin@hotel.com"), new Document("$set", new Document("contrasenaHash", hashReal)));
            System.out.println("✔ Usuario admin actualizado con hash válido.");
        }

        // 3. Probamos la validación del login
        RepositorioMongo repo = new RepositorioMongo();
        boolean resultado = repo.validarLogin("admin@hotel.com", "Hotel2026!");

        if (resultado) {
            System.out.println(" ¡ÉXITO! El inicio de sesión es correcto.");
        } else {
            System.out.println("❌ ERROR: Las credenciales no coinciden.");
        }
    }
}