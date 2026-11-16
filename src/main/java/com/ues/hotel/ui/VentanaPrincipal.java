package com.ues.hotel.ui;

import com.ues.hotel.servicio.Hotel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

/**
 * Ventana principal: una pestaña por cada función del caso de estudio.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private final List<Actualizable> paneles = new ArrayList<>();

    public VentanaPrincipal(Hotel hotel) {
        super("Sistema de reservas - " + hotel.getNombre());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Runnable refrescar = this::refrescarTodo;
        PanelClientes clientes = new PanelClientes(hotel, refrescar);
        PanelHabitaciones habitaciones = new PanelHabitaciones(hotel, refrescar);
        PanelReservar reservar = new PanelReservar(hotel, refrescar);
        PanelReservas reservas = new PanelReservas(hotel, refrescar);
        paneles.add(clientes);
        paneles.add(habitaciones);
        paneles.add(reservar);
        paneles.add(reservas);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Arial", Font.BOLD, 13));
        pestanas.setBackground(new Color(23, 43, 77));
        pestanas.setForeground(Color.WHITE);
        pestanas.setTabPlacement(JTabbedPane.TOP);
        pestanas.setOpaque(true);
        pestanas.setBackground(new Color(23, 43, 77));
        pestanas.addTab("Clientes", clientes);
        pestanas.addTab("Habitaciones", habitaciones);
        pestanas.addTab("Disponibilidad y reserva", reservar);
        pestanas.addTab("Reservas y costo", reservas);
        
        add(pestanas);
        pestanas.addChangeListener(e -> refrescarTodo());

        setMinimumSize(new Dimension(900, 520));
        setSize(1050, 620);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void refrescarTodo() {
        for (Actualizable p : paneles) {
            p.actualizar();
        }
    }
}
