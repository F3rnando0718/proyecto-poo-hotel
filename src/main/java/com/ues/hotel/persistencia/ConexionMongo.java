package com.ues.hotel.persistencia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongo{
    private static final String URI = "mongodb:localhost:27017";
    private static final String NOMBRE_BD = "hotel_db";
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    
    public static MongoDatabase obtenerInstancia (){
        if (database == null){
            try{
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(NOMBRE_BD);
                // Hacemos un ping para verificar que conecta
                database.runCommand(new org.bson.Document("ping", 1));
                System.out.println("¡Conexiòn exitosa a MongoDB!");
            } catch (Exception e){
                System.err.println("Error al conectar MongoDB: " + e.getMessage());
            }
        }
        return database;
    }

public static void cerrar(){
    if (mongoClient != null){
        mongoClient.close();
        database = null;
        System.out.println("Conexion a MongoDB Cerrada");
    }

}    

}
