// =====================================================================
// Script de inicialización de la base de datos: hotel_db
// Sistema de Reservas de Hotel - POO UES
// =====================================================================

// Seleccionar o crear la base de datos
db = db.getSiblingDB('hotel_db');

// Limpiar colecciones anteriores (opcional, para reiniciar limpio)
db.usuarios.drop();
db.clientes.drop();
db.habitaciones.drop();
db.reservas.drop();

// ---------------------------------------------------------------------
// 1. Colección: usuarios (Para el módulo de Login y seguridad con PBKDF2)
// ---------------------------------------------------------------------
db.usuarios.insertOne({
    _id: "admin",
    nombreCompleto: "Administrador del Sistema",
    // Contraseña cifrada correspondiente por defecto a "Hotel2026!"
    passwordHash: "1000:c2FsdGFiY3Rlc3Q=:c2VndXJhcGFzc3dvcmQ=", 
    activo: true
});

// ---------------------------------------------------------------------
// 2. Colección: clientes (Mínimo 6 registros de prueba)
// ---------------------------------------------------------------------
db.clientes.insertMany([
    { _id: 1, codigo: "CLI-001", nombreCompleto: "Juan Pérez", documento: "01234567-8", telefono: "7000-1111", correo: "juan@example.com" },
    { _id: 2, codigo: "CLI-002", nombreCompleto: "María Gómez", documento: "01234567-9", telefono: "7000-2222", correo: "maria@example.com" },
    { _id: 3, codigo: "CLI-003", nombreCompleto: "Carlos López", documento: "01234567-0", telefono: "7000-3333", correo: "carlos@example.com" },
    { _id: 4, codigo: "CLI-004", nombreCompleto: "Ana Martínez", documento: "01234567-1", telefono: "7000-4444", correo: "ana@example.com" },
    { _id: 5, codigo: "CLI-005", nombreCompleto: "José Rodríguez", documento: "01234567-2", telefono: "7000-5555", correo: "jose@example.com" },
    { _id: 6, codigo: "CLI-006", nombreCompleto: "Sofía Hernández", documento: "01234567-3", telefono: "7000-6666", correo: "sofia@example.com" }
]);

// ---------------------------------------------------------------------
// 3. Colección: habitaciones (Tipos, capacidades, tarifas y estados)
// ---------------------------------------------------------------------
db.habitaciones.insertMany([
    { _id: 1, codigo: "HAB-101", numero: 101, tipo: "SENCILLA", capacidad: 1, precioPorNoche: 25.0, estado: "DISPONIBLE" },
    { _id: 2, codigo: "HAB-102", numero: 102, tipo: "SENCILLA", capacidad: 1, precioPorNoche: 25.0, estado: "DISPONIBLE" },
    { _id: 3, codigo: "HAB-201", numero: 201, tipo: "DOBLE", capacidad: 2, precioPorNoche: 45.0, estado: "DISPONIBLE" },
    { _id: 4, codigo: "HAB-202", numero: 202, tipo: "DOBLE", capacidad: 2, precioPorNoche: 45.0, estado: "DISPONIBLE" },
    { _id: 5, codigo: "HAB-301", numero: 301, tipo: "SUITE", capacidad: 4, precioPorNoche: 90.0, estado: "DISPONIBLE" },
    { _id: 6, codigo: "HAB-302", numero: 302, tipo: "SUITE", capacidad: 4, precioPorNoche: 90.0, estado: "FUERA_DE_SERVICIO" }
]);

// ---------------------------------------------------------------------
// 4. Colección: reservas (Registros iniciales de alojamiento)
// ---------------------------------------------------------------------
db.reservas.insertMany([
    { 
        _id: 1, 
        codigo: "RES-001", 
        codigoCliente: "CLI-001", 
        codigoHabitacion: "HAB-101", 
        fechaEntrada: "2026-12-01", 
        fechaSalida: "2026-12-05", 
        noches: 4, 
        costoTotal: 100.0, 
        estado: "CONFIRMADA" 
    }
]);

print("¡Base de datos hotel_db inicializada y poblada exitosamente!");