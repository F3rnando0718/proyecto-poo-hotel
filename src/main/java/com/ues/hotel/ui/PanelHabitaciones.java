package com.ues.hotel.ui;

import com.ues.hotel.modelo.Habitacion;
import com.ues.hotel.modelo.TipoHabitacion;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
        
/**
 * Registro y listado de habitaciones.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class PanelHabitaciones extends JPanel implements Actualizable {

    private static final long serialVersionUID = 1L;

    private final Hotel hotel;
    private final Runnable alCambiar;
    private final JTextField txtNumero = new JTextField(6);
    private final JComboBox<TipoHabitacion> cmbTipo = new JComboBox<>(TipoHabitacion.values());
    private final DefaultTableModel modelo = Ui.modelo("Número", "Tipo", "Capacidad", "Tarifa por noche");
    private final Color Azul = new Color(23, 43, 77);
    private final Color Gold = new Color(198, 161, 91);
    private final Color Crema = new Color(244, 241, 234);
    public PanelHabitaciones(Hotel hotel, Runnable alCambiar) {
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

        JLabel subtitulo = new JLabel("Administración de habitaciones");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(Color.WHITE);

        JPanel textos = new JPanel(new java.awt.GridLayout(2, 1));
        textos.setBackground(Azul);
        textos.add(titulo);
        textos.add(subtitulo);

        encabezado.add(textos, BorderLayout.CENTER);
 
        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setForeground(Azul);
        lblNumero.setFont(new Font("Arial", Font.BOLD, 13));
        formulario.add(lblNumero);
        formulario.add(txtNumero);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setForeground(Azul);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        formulario.add(lblTipo);
        formulario.add(cmbTipo);

        txtNumero.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbTipo.setFont(new Font("Arial", Font.PLAIN, 13));
        JButton btnRegistrar = new JButton("REGISTRAR HABITACIÓN");
        btnRegistrar.setBackground(Gold);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.addActionListener(e -> registrar());
        formulario.add(btnRegistrar);

        
        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBackground(Crema);

        contenido.add(formulario, BorderLayout.NORTH);
        contenido.add(new JScrollPane(Ui.tabla(modelo)), BorderLayout.CENTER);
        add(encabezado, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
        actualizar();
    }

    private void registrar() {
        try {
            int numero;
            try {
                numero = Integer.parseInt(txtNumero.getText().trim());
            } catch (NumberFormatException ex) {
                throw new HotelException("El número de habitación debe ser un entero.");
            }
            hotel.registrarHabitacion(numero, (TipoHabitacion) cmbTipo.getSelectedItem());
            txtNumero.setText("");
            alCambiar.run();
        } catch (HotelException ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    @Override
    public void actualizar() {
        modelo.setRowCount(0);
        for (Habitacion h : hotel.getHabitaciones()) {
            modelo.addRow(new Object[] {h.getNumero(), h.getTipo(), h.getCapacidad(), Ui.dinero(h.getTarifaNoche())});
        }
    }
}
