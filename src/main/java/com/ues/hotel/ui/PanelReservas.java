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
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;


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
    private final JCheckBox chkSoloActivas = new JCheckBox("SOLO RESERVAS ACTIVAS");
    private final DefaultTableModel modelo =
            Ui.modelo("ID", "Cliente", "Habitación", "Entrada", "Salida", "Noches", "Estado", "Total");
    private final JTable tabla = Ui.tabla(modelo);
    private final Color Azul = new Color(23, 43, 77);
    private final Color Gold = new Color(198, 161, 91);
    private final Color Crema = new Color(244, 241, 234);
    private final List<Reserva> mostradas = new ArrayList<>();

    public PanelReservas(Hotel hotel, Runnable alCambiar) {
        this.hotel = hotel;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        setBackground(Crema);

// Encabezado del panel
JPanel encabezado = new JPanel(new BorderLayout());
encabezado.setBackground(Azul);
encabezado.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("GRAND HOTEL");
        titulo.setFont(new Font("Serif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Administración de reservas");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(Color.WHITE);

        JPanel textos = new JPanel(new java.awt.GridLayout(2, 1));
        textos.setBackground(Azul);
        textos.add(titulo);
        textos.add(subtitulo);

        encabezado.add(textos, BorderLayout.CENTER);

        
        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBackground(Crema);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        acciones.setBackground(Color.WHITE);
        acciones.setBorder(new EmptyBorder(15, 15, 15, 15));
        chkSoloActivas.setForeground(Azul);
        chkSoloActivas.setFont(new Font("Arial", Font.BOLD, 13));
        chkSoloActivas.setBackground(Color.WHITE);
        chkSoloActivas.addActionListener(e -> actualizar());
        acciones.add(chkSoloActivas);
        JButton btnCosto = new JButton("VER COSTO");
        btnCosto.setBackground(Azul);
        btnCosto.setForeground(Color.WHITE);
        btnCosto.setFont(new Font("Arial", Font.BOLD, 12));
        btnCosto.setFocusPainted(false);
        btnCosto.setBorderPainted(false);
        btnCosto.addActionListener(e -> verCosto());
        acciones.add(btnCosto);

        JButton btnCancelar = new JButton("CANCELAR RESERVA");
        btnCancelar.setBackground(Gold);
        btnCancelar.setForeground(Color.WHITE);     
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(e -> cancelar());
        acciones.add(btnCancelar);

        contenido.add(acciones, BorderLayout.NORTH);
        contenido.add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
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
