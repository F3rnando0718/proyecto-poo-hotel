package com.ues.hotel.ui;

import com.ues.hotel.modelo.Reserva;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Consulta de reservas, cancelación y cálculo del costo.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class PanelReservas extends JPanel implements Actualizable {

    private static final long serialVersionUID = 1L;

    private final Hotel hotel;
    private final Runnable alCambiar;
    private final JCheckBox chkSoloActivas = new JCheckBox("Solo reservas activas");
    private final DefaultTableModel modelo =
            Ui.modelo("ID", "Cliente", "Habitación", "Entrada", "Salida", "Noches", "Estado", "Total");
    private final JTable tabla = Ui.tabla(modelo);
    private final List<Reserva> mostradas = new ArrayList<>();

    public PanelReservas(Hotel hotel, Runnable alCambiar) {
        this.hotel = hotel;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        chkSoloActivas.addActionListener(e -> actualizar());
        acciones.add(chkSoloActivas);
        JButton btnCosto = new JButton("Ver costo");
        btnCosto.addActionListener(e -> verCosto());
        acciones.add(btnCosto);
        JButton btnCancelar = new JButton("Cancelar reserva");
        btnCancelar.addActionListener(e -> cancelar());
        acciones.add(btnCancelar);

        add(acciones, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        actualizar();
    }

    private Reserva seleccionada() throws HotelException {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            throw new HotelException("Seleccione una reserva de la tabla.");
        }
        return mostradas.get(fila);
    }

    private void verCosto() {
        try {
            Reserva r = seleccionada();
            Ui.info(this, r + "\n" + r.getNoches() + " noche(s) x " + Ui.dinero(r.getHabitacion().getTarifaNoche())
                    + "\nSubtotal: " + Ui.dinero(r.calcularSubtotal())
                    + "\nIVA (13%): " + Ui.dinero(r.calcularIva())
                    + "\nTotal: " + Ui.dinero(hotel.calcularCosto(r.getId())));
        } catch (HotelException ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private void cancelar() {
        try {
            Reserva r = seleccionada();
            hotel.cancelarReserva(r.getId());
            Ui.info(this, "La reserva #" + r.getId() + " fue cancelada.");
            alCambiar.run();
        } catch (HotelException ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    @Override
    public void actualizar() {
        mostradas.clear();
        mostradas.addAll(hotel.consultarReservas(chkSoloActivas.isSelected()));
        modelo.setRowCount(0);
        for (Reserva r : mostradas) {
            modelo.addRow(new Object[] {r.getId(), r.getCliente().getNombre(), r.getHabitacion(),
                r.getEntrada().format(Ui.FECHA), r.getSalida().format(Ui.FECHA), r.getNoches(),
                r.getEstado(), Ui.dinero(r.calcularTotal())});
        }
    }
}
