package com.ues.hotel.persistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.modelo.Habitacion;
import com.ues.hotel.modelo.Reserva;
import com.ues.hotel.modelo.TipoHabitacion;
import com.ues.hotel.modelo.EstadoReserva;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import com.ues.hotel.servicio.RepositorioHotel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.mongodb.client.model.ReplaceOptions;
import static com.mongodb.client.model.Filters.eq;
import java.util.Map;
import java.util.Optional;
import org.bson.Document;

/**

 
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */



public class RepositorioHotelMongo implements RepositorioHotel {

    private final MongoCollection<Document> clientes;
    private final MongoCollection<Document> habitaciones;
    private final MongoCollection<Document> reservas;

    public RepositorioHotelMongo(MongoDatabase db) {
        clientes = db.getCollection("clientes");
        habitaciones = db.getCollection("habitaciones");
        reservas = db.getCollection("reservas");
    }

    @Override
public void guardar(Hotel hotel) throws HotelException {
    try {
        for (Cliente c : hotel.getClientes()) {
            Document documento = new Document("_id", c.getId())
                    .append("codigo", "CLI-" + String.format("%03d", c.getId()))
                    .append("nombreCompleto", c.getNombre())
                    .append("documento", c.getDui())
                    .append("telefono", c.getTelefono())
                    .append("correo", c.getCorreo());

            clientes.replaceOne(eq("_id", c.getId()), documento,
                    new ReplaceOptions().upsert(true));
        }

        for (Habitacion h : hotel.getHabitaciones()) {
            Document documento = new Document("_id", h.getNumero())
                    .append("codigo", "HAB-" + h.getNumero())
                    .append("numero", h.getNumero())
                    .append("tipo", h.getTipo().name())
                    .append("capacidad", h.getCapacidad())
                    .append("precioPorNoche", h.getTarifaNoche())
                    .append("estado", "DISPONIBLE");

            habitaciones.replaceOne(eq("_id", h.getNumero()), documento,
                    new ReplaceOptions().upsert(true));
        }

        for (Reserva r : hotel.consultarReservas(false)) {
            Document documento = new Document("_id", r.getId())
                    .append("codigo", "RES-" + String.format("%03d", r.getId()))
                    .append("codigoCliente", "CLI-" + String.format("%03d", r.getCliente().getId()))
                    .append("codigoHabitacion", "HAB-" + r.getHabitacion().getNumero())
                    .append("fechaEntrada", r.getEntrada().toString())
                    .append("fechaSalida", r.getSalida().toString())
                    .append("noches", r.getNoches())
                    .append("costoTotal", r.calcularTotal())
                    .append("estado", r.getEstado().name());

            reservas.replaceOne(eq("_id", r.getId()), documento,
                    new ReplaceOptions().upsert(true));
        }

    } catch (Exception e) {
        throw new HotelException("Error al guardar en MongoDB: " + e.getMessage());
    }
}

    @Override
    public Optional<Hotel> cargar() throws HotelException {
        try {
            List<Cliente> listaClientes = new ArrayList<>();
            List<Habitacion> listaHabitaciones = new ArrayList<>();
            List<Reserva> listaReservas = new ArrayList<>();

            Map<Integer, Cliente> clientesPorId = new HashMap<>();
            Map<Integer, Habitacion> habitacionesPorNumero = new HashMap<>();

            for (Document d : clientes.find()) {
                Cliente c = new Cliente(
                        d.getInteger("_id"),
                        d.getString("nombreCompleto"),
                        d.getString("documento"),
                        d.getString("telefono"),
                        d.getString("correo"));
                listaClientes.add(c);
                clientesPorId.put(c.getId(), c);
            }

            for (Document d : habitaciones.find()) {
                Habitacion h = Habitacion.crear(
                        d.getInteger("numero"),
                        TipoHabitacion.valueOf(d.getString("tipo")));
                listaHabitaciones.add(h);
                habitacionesPorNumero.put(h.getNumero(), h);
            }

            for (Document d : reservas.find()) {
                int idCliente = Integer.parseInt(
                        d.getString("codigoCliente").replace("CLI-", ""));
                int numeroHabitacion = Integer.parseInt(
                        d.getString("codigoHabitacion").replace("HAB-", ""));

                Cliente c = clientesPorId.get(idCliente);
                Habitacion h = habitacionesPorNumero.get(numeroHabitacion);

                if (c != null && h != null) {
                    
                    String estadoMongo = d.getString("estado");

        EstadoReserva estado = estadoMongo.equals("CONFIRMADA")
        ? EstadoReserva.ACTIVA
        : EstadoReserva.valueOf(estadoMongo);
                    
                    listaReservas.add(new Reserva(
                            d.getInteger("_id"),
                            c,
                            h,
                            LocalDate.parse(d.getString("fechaEntrada")),
                            LocalDate.parse(d.getString("fechaSalida")),
                            estado));
                }
            
            }

            Hotel hotel = new Hotel("Grand Hotel");
            hotel.restaurarDatos(listaClientes, listaHabitaciones, listaReservas);
            return Optional.of(hotel);

        } catch (Exception e) {
            throw new HotelException("Error al cargar desde MongoDB: " + e.getMessage());
        }
    }
}
