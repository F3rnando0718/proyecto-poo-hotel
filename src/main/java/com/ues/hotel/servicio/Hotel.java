package com.ues.hotel.servicio;

import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.modelo.Habitacion;
import com.ues.hotel.modelo.Reserva;
import com.ues.hotel.modelo.TipoHabitacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.ues.hotel.modelo.EstadoHabitacion;

/**
 * Clase de servicio que agrupa los datos del hotel y las operaciones del caso de estudio:
 * registrar clientes y habitaciones, consultar disponibilidad, reservar, cancelar,
 * consultar reservas y calcular el costo.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class Hotel {

    private final String nombre;
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Habitacion> habitaciones = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();
    private int siguienteIdCliente = 1;
    private int siguienteIdReserva = 1;
    private RepositorioHotel repositorio;

    public Hotel(String nombre) {
    if (nombre == null || nombre.isBlank()) {
        throw new IllegalArgumentException(
                "El nombre del hotel es obligatorio.");
    }

    this.nombre = nombre.trim();
}

    public String getNombre() {
        return nombre;
    }

    /** Asocia el repositorio donde se guardarán los cambios (opcional). */
    public void setRepositorio(RepositorioHotel repositorio) {
        this.repositorio = repositorio;
    }

    private void persistir() throws HotelException {
        if (repositorio != null) {
            repositorio.guardar(this);
        }
    }

    /** Reemplaza los datos del hotel por los restaurados desde el repositorio. */
    public void restaurarDatos(List<Cliente> listaClientes, List<Habitacion> listaHabitaciones,
            List<Reserva> listaReservas) {
        if (listaClientes == null || listaHabitaciones == null || listaReservas == null) {
            throw new IllegalArgumentException("Las listas de datos no pueden ser nulas.");
        }
        clientes.clear();
        habitaciones.clear();
        reservas.clear();
        clientes.addAll(listaClientes);
        habitaciones.addAll(listaHabitaciones);
        reservas.addAll(listaReservas);
        siguienteIdCliente = clientes.stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
        siguienteIdReserva = reservas.stream().mapToInt(Reserva::getId).max().orElse(0) + 1;
    }

    // ---------------------------------------------------------------- Clientes

    public Cliente registrarCliente(String nombreCliente, String dui, String telefono, String correo)
            throws HotelException {
        Cliente nuevo;
        try {
            nuevo = new Cliente(siguienteIdCliente, nombreCliente, dui, telefono, correo);
        } catch (IllegalArgumentException e) {
            throw new HotelException(e.getMessage());
        }
        for (Cliente c : clientes) {
            if (c.getDui().equals(nuevo.getDui())) {
                throw new HotelException("Ya existe un cliente registrado con el DUI " + nuevo.getDui() + ".");
            }
        }
        siguienteIdCliente++;
        clientes.add(nuevo);
        persistir();
        return nuevo;
    }

    public Cliente buscarCliente(int id) throws HotelException {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new HotelException("No existe el cliente con id " + id + ".");
    }

    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }
    
    public void eliminarCliente(int idCliente) throws HotelException{
        
        Cliente cliente = buscarCliente(idCliente);
        
        for (Reserva reserva : reservas){
            if (reserva.getCliente().getId() == idCliente){
            throw new HotelException("No se puede eliminar el cleinte porque tiene reservas registradas.");
            }
        }
        
        clientes.remove(cliente);
        persistir();
    }
    // ----------------------------------------------------------- Habitaciones

    public Habitacion registrarHabitacion(int numero, TipoHabitacion tipo) throws HotelException {
        if (numero <= 0) {
            throw new HotelException("El número de habitación debe ser mayor que cero.");
        }
        for (Habitacion h : habitaciones) {
            if (h.getNumero() == numero) {
                throw new HotelException("La habitación " + numero + " ya está registrada.");
            }
        }
        Habitacion nueva;
        try {
            nueva = Habitacion.crear(numero, tipo);
        } catch (IllegalArgumentException e) {
            throw new HotelException(e.getMessage());
        }
        habitaciones.add(nueva);
        persistir();
        return nueva;
    }

    public Habitacion buscarHabitacion(int numero) throws HotelException {
    for (Habitacion h : habitaciones) {
        if (h.getNumero() == numero) {
            return h;
        }
    }
    throw new HotelException("No existe la habitación " + numero + ".");
}

/**
 * Busca habitaciones según su tipo.
 */
public List<Habitacion> buscarHabitacionesPorTipo(
        TipoHabitacion tipo) throws HotelException {

    if (tipo == null) {
        throw new HotelException(
                "Debe seleccionar un tipo de habitación.");
    }

    List<Habitacion> resultado = new ArrayList<>();

    for (Habitacion habitacion : habitaciones) {
        if (habitacion.getTipo() == tipo) {
            resultado.add(habitacion);
        }
    }

    return Collections.unmodifiableList(resultado);
}
    
    

    public List<Habitacion> getHabitaciones() {
        return Collections.unmodifiableList(habitaciones);
    }
    
/**
 * Modifica la capacidad y la tarifa de una habitación.
 */
public void modificarHabitacion(int numero, int capacidad,
        double tarifaNoche) throws HotelException {

    Habitacion habitacion = buscarHabitacion(numero);

    try {
        // Validamos ambos valores antes de modificarlos.
        Validador.validarNumeroPositivo(capacidad, "capacidad");
        Validador.validarPrecio(tarifaNoche);

        habitacion.setCapacidad(capacidad);
        habitacion.setTarifaNoche(tarifaNoche);

    } catch (IllegalArgumentException e) {
        throw new HotelException(e.getMessage());
    }

    persistir();
}

/**
 * Cambia el estado de una habitación.
 */
public void cambiarEstadoHabitacion(int numero,
        EstadoHabitacion estado) throws HotelException {

    Habitacion habitacion = buscarHabitacion(numero);

    if (estado == null) {
        throw new HotelException(
                "Debe seleccionar un estado para la habitación.");
    }

    habitacion.setEstado(estado);
    persistir();
}

    
    public void eliminarHabitacion(int numero) throws HotelException {

    Habitacion habitacion = buscarHabitacion(numero);

    for (Reserva reserva : reservas) {
        if (reserva.getHabitacion().getNumero() == numero) {
            throw new HotelException(
                    "No se puede eliminar la habitación porque tiene reservas registradas.");
        }
    }

    habitaciones.remove(habitacion);
    persistir();
}

    // ------------------------------------------------------------ Disponibilidad

    public List<Habitacion> consultarHabitacionesDisponibles(LocalDate entrada, LocalDate salida)
            throws HotelException {
        validarRango(entrada, salida);
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (estaDisponible(h, entrada, salida)) {
                disponibles.add(h);
            }
        }
        return disponibles;
    }

    
private boolean estaDisponible(Habitacion habitacion,
        LocalDate entrada, LocalDate salida) {

    if (habitacion == null || entrada == null || salida == null
            || !salida.isAfter(entrada)) {
        return false;
    }

    if (habitacion.getEstado() != EstadoHabitacion.DISPONIBLE) {
    return false;
    
    }

    for (Reserva r : reservas) {
        if (r.getHabitacion().getNumero() == habitacion.getNumero()
                && r.seSolapaCon(entrada, salida)) {
            return false;
        }
    }

    return true;
}


    // ------------------------------------------------------------------ Reservas

    
/**
 * Mantiene compatibilidad con las llamadas anteriores.
 */
public Reserva realizarReserva(int idCliente, int numeroHabitacion,
        LocalDate entrada, LocalDate salida) throws HotelException {

    return realizarReserva(idCliente, numeroHabitacion, 1,
            entrada, salida);
}

/**
 * Registra una reserva validando la cantidad de huéspedes.
 */
public Reserva realizarReserva(int idCliente, int numeroHabitacion,
        int cantidadHuespedes, LocalDate entrada, LocalDate salida)
        throws HotelException {

    validarRango(entrada, salida);

    if (entrada.isBefore(LocalDate.now())) {
        throw new HotelException(
                "La fecha de entrada no puede ser anterior a hoy.");
    }

    Cliente cliente = buscarCliente(idCliente);
    Habitacion habitacion = buscarHabitacion(numeroHabitacion);

    if (cantidadHuespedes <= 0) {
        throw new HotelException(
                "La cantidad de huéspedes debe ser mayor que cero.");
    }

    if (cantidadHuespedes > habitacion.getCapacidad()) {
        throw new HotelException(
                "La habitación admite solamente "
                + habitacion.getCapacidad() + " huéspedes.");
    }

    if (!estaDisponible(habitacion, entrada, salida)) {
        throw new HotelException(
                "La habitación " + numeroHabitacion
                + " no está disponible en las fechas solicitadas.");
    }

    Reserva reserva;

    try {
        reserva = new Reserva(siguienteIdReserva, cliente,
                habitacion, cantidadHuespedes, entrada, salida);
    } catch (IllegalArgumentException e) {
        throw new HotelException(e.getMessage());
    }

    siguienteIdReserva++;
    reservas.add(reserva);
    persistir();

    return reserva;
}


    public void cancelarReserva(int idReserva) throws HotelException {
        Reserva reserva = buscarReserva(idReserva);
        if (!reserva.estaActiva()) {
            throw new HotelException("La reserva #" + idReserva + " ya estaba cancelada.");
        }
        reserva.cancelar();
        persistir();
    }

    public Reserva buscarReserva(int idReserva) throws HotelException {
        for (Reserva r : reservas) {
            if (r.getId() == idReserva) {
                return r;
            }
        }
        throw new HotelException("No existe la reserva #" + idReserva + ".");
    }

    public List<Reserva> consultarReservas(boolean soloActivas) {
        List<Reserva> resultado = new ArrayList<>();
        
        for (Reserva r : reservas) {
            if (!soloActivas || r.estaActiva()) {
            resultado.add(r);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<Reserva> consultarReservasDeCliente(int idCliente) {
        List<Reserva> resultado = new ArrayList<>();
        
        for (Reserva r : reservas) {
            if (r.getCliente().getId() == idCliente) {
                resultado.add(r);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public double calcularCosto(int idReserva) throws HotelException {
        return buscarReserva(idReserva).calcularTotal();
    }

    // ---------------------------------------------------------------- Utilidades

    private void validarRango(LocalDate entrada, LocalDate salida)
        throws HotelException {
        if (entrada == null || salida == null) {
            throw new HotelException("Debe indicar las fechas de entrada y salida.");
        }
        
        if (!salida.isAfter(entrada)) {
            throw new HotelException("La fecha de salida debe ser posterior a la fecha de entrada.");
        }
        
        if (entrada.isBefore(LocalDate.now())) {
            throw new HotelException("La fecha de entrada no puede ser anterior a hoy.");
        }
    }

    /** Carga datos de ejemplo para poder probar la aplicación de inmediato. */
    public void cargarDatosDemo() {
        try {
            registrarCliente("Ana Martínez", "01234567-8", "7000-0001", "ana@correo.com");
            registrarCliente("Carlos Pérez", "07654321-0", "7000-0002", "carlos@correo.com");
            registrarHabitacion(101, TipoHabitacion.SENCILLA);
            registrarHabitacion(102, TipoHabitacion.SENCILLA);
            registrarHabitacion(201, TipoHabitacion.DOBLE);
            registrarHabitacion(202, TipoHabitacion.DOBLE);
            registrarHabitacion(301, TipoHabitacion.SUITE);
            realizarReserva(1, 201, LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        } catch (HotelException e) {
            throw new IllegalStateException("Error al cargar datos de ejemplo: " + e.getMessage(), e);
        }
    }
}
