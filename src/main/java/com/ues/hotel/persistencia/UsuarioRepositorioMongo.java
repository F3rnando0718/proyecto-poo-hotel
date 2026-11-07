package com.ues.hotel.persistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.ues.hotel.seguridad.Usuario;
import com.ues.hotel.seguridad.UsuarioRepositorio;
import java.util.Optional;
import org.bson.Document;

public class UsuarioRepositorioMongo implements UsuarioRepositorio {
    private final MongoCollection<Document> usuariosCollection;

    public UsuarioRepositorioMongo(MongoDatabase database) {
        this.usuariosCollection = database.getCollection("usuarios");
    }

    @Override
    public Optional<Usuario> buscarPorUsuario(String usuario) throws Exception {
        Document doc = usuariosCollection.find(Filters.eq("_id", usuario)).first();
        if (doc == null) {
            return Optional.empty();
        }
        Usuario u = new Usuario(
            doc.getString("_id"),
            doc.getString("nombreCompleto"),
            doc.getString("passwordHash"),
            !Boolean.FALSE.equals(doc.getBoolean("activo"))
        );
        return Optional.of(u);
    }

    @Override
    public void guardar(Usuario usuario) throws Exception {
        Document doc = new Document("_id", usuario.getUsuario())
                .append("nombreCompleto", usuario.getNombreCompleto())
                .append("passwordHash", usuario.getPasswordHash())
                .append("activo", usuario.isActivo());
        
        usuariosCollection.replaceOne(
            Filters.eq("_id", usuario.getUsuario()), 
            doc, 
            new ReplaceOptions().upsert(true)
        );
    }
}