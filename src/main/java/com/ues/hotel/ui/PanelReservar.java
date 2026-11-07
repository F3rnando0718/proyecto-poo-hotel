package com.ues.hotel.ui;

import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.modelo.Habitacion;
import com.ues.hotel.modelo.Reserva;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Consulta de disponibilidad y realización de reservas.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class PanelReservar extends JPanel implements Actualizable {

    private static final long serialVersionUID = 1L;

    private final Hotel hotel;
    private final Runnable alCambiar;
    private final JTextField txtEntrada = new JTextField(LocalDate.now().plusDays(1).format(Ui.FECHA), 9);
    private final JTextField txtSalida = new JTextField(LocalDate.now().plusDays(3).format(Ui.FECHA), 9);
    private final DefaultTableModel modelo = Ui.modelo("Número", "Tipo", "Capacidad", "Tarifa por noche");
    private final JTable tabla = Ui.tabla(modelo);
    private final JComboBox<Cliente> cmbCliente = new JComboBox<>();
    private final List<Habitacion> mostradas = new ArrayList<>();
    private boolean hayBusqueda = false;

    public PanelReservar(Hotel hotel, Runnable alCambiar) {
        this.hotel = hotel;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel fechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        fechas.add(new JLabel("Entrada (dd/mm/aaaa):"));
        fechas.add(txtEntrada);
        fechas.add(new JLabel("Salida (dd/mm/aaaa):"));
        fechas.add(txtSalida);
        JButton btnBuscar = new JButton("Consultar disponibles");
        btnBuscar.addActionListener(e -> buscar(true));
        fechas.add(btnBuscar);

        JPanel reservar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        reservar.add(new JLabel("Cliente:"));
        reservar.add(cmbCliente);
        JButton btnReservar = new JButton("Reservar habitación seleccionada");
        btnReservar.addActionListener(e -> reservar());
        reservar.add(btnReservar);

        add(fechas, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(reservar, BorderLayout.SOUTH);
        actualizar();
    }

    private void buscar(boolean avisar) {
        try {
            LocalDate entrada = Ui.leerFecha(txtEntrada.getText(), "entrada");
            LocalDate salida = Ui.leerFecha(txtSalida.getText(), "salida");
            mostradas.clear();
            mostradas.addAll(hotel.consultarHabitacionesDisponibles(entrada, salida));
            modelo.setRowCount(0);
            for (Habitacion h : mostradas) {
                modelo.addRow(new Object[] {h.getNumero(), h.getTipo(), h.getCapacidad(),
                    Ui.dinero(h.getTarifaNoche())});
            }
            hayBusqueda = true;
        } catch (HotelException ex) {
            if (avisar) {
                Ui.error(this, ex.getMessage());
            }
        }
    }

    private void reservar() {
        try {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                throw new HotelException("Primero consulte la disponibilidad y seleccione una habitación.");
            }
            Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
            if (cliente == null) {
                throw new HotelException("Debe registrar al menos un cliente antes de reservar.");
            }
            LocalDate entrada = Ui.leerFecha(txtEntrada.getText(), "entrada");
            LocalDate salida = Ui.leerFecha(txtSalida.getText(), "salida");
            Habitacion habitacion = mostradas.get(fila);
            Reserva reserva = hotel.realizarReserva(cliente.getId(), habitacion.getNumero(), entrada, salida);
            Ui.info(this, "Reserva #" + reserva.getId() + " creada para " + cliente.getNombre() + ".\n"
                    + habitacion + " | " + reserva.getNoches() + " noche(s)\n"
                    + "Subtotal: " + Ui.dinero(reserva.calcularSubtotal())
                    + "  IVA: " + Ui.dinero(reserva.calcularIva())
                    + "  Total: " + Ui.dinero(reserva.calcularTotal()));
            alCambiar.run();
        } catch (HotelException ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    @Override
    public void actualizar() {
        Object seleccionado = cmbCliente.getSelectedItem();
        cmbCliente.removeAllItems();
        for (Cliente c : hotel.getClientes()) {
            cmbCliente.addItem(c);
        }
        if (seleccionado != null) {
            cmbCliente.setSelectedItem(seleccionado);
        }
        if (hayBusqueda) {
            buscar(false);
        }
    }
}
