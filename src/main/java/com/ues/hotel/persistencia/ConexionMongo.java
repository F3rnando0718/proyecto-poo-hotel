package com.ues.hotel.persistencia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ConexionMongo {
    private static final String URI = "mongodb://localhost:27017";
    private static final String NOMBRE_BD = "hotel_db";
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase obtenerInstancia() {
        if (database == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(NOMBRE_BD);
                database.runCommand(new Document("ping", 1));
                System.out.println("¡Conexión exitosa a MongoDB!");
            } catch (Exception e) {
                System.err.println("Error al conectar con MongoDB: " + e.getMessage());
            }
        }
        return database;
    }

    public static void cerrar() {
        if (mongoClient != null) {
            mongoClient.close();
            database = null;
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }
}