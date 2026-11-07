// Script de inicialización para hotel_db
db = db.getSiblingDB("hotel_db");

// Limpiar colecciones anteriores si existen
db.usuarios.drop();
db.clientes.drop();
db.habitaciones.drop();
db.reservas.drop();

// 1. Usuario administrador (Contraseña en texto plano para prueba: Hotel2026!)
db.usuarios.insertOne({
    _id: ObjectId(),
    nombreUsuario: "admin",
    correo: "admin@hotel.com",
    contrasenaHash: "1000:c3VwZXJzYWxhbmRhZG9hYmM=:8h3a... (hash PBKDF2)" // O asegúrate de generar el hash con tu clase HashContrasena
});

// 2. Clientes de prueba (6 registros mínimos)
db.clientes.insertMany([
    { _id: 1, codigo: "CLI-001", nombreCompleto: "Juan Pérez", documento: "01234567-8", telefono: "7000-1111", correo: "juan@example.com" },
    { _id: 2, codigo: "CLI-002", nombreCompleto: "María Gómez", documento: "01234567-9", telefono: "7000-2222", correo: "maria@example.com" },
    { _id: 3, codigo: "CLI-003", nombreCompleto: "Carlos López", documento: "01234567-0", telefono: "7000-3333", correo: "carlos@example.com" },
    { _id: 4, codigo: "CLI-004", nombreCompleto: "Ana Martínez", documento: "01234567-1", telefono: "7000-4444", correo: "ana@example.com" },
    { _id: 5, codigo: "CLI-005", nombreCompleto: "Luis Torres", documento: "01234567-2", telefono: "7000-5555", correo: "luis@example.com" },
    { _id: 6, codigo: "CLI-006", nombreCompleto: "Sofía Ramírez", documento: "01234567-3", telefono: "7000-6666", correo: "sofia@example.com" }
]);

// 3. Habitaciones de prueba
db.habitaciones.insertMany([
    { _id: 1, codigoHabitacion: "HAB-101", numero: 101, tipo: "SENCILLA", capacidad: 1, precioPorNoche: 25.0, estado: "DISPONIBLE" },
    { _id: 2, codigoHabitacion: "HAB-102", numero: 102, tipo: "DOBLE", capacidad: 2, precioPorNoche: 45.0, estado: "DISPONIBLE" },
    { _id: 3, codigoHabitacion: "HAB-103", numero: 103, tipo: "SUITE", capacidad: 4, precioPorNoche: 90.0, estado: "DISPONIBLE" }
]);

print("¡Base de datos hotel_db inicializada con éxito!");