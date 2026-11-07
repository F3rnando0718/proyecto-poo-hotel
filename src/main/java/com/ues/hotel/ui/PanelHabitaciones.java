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

    public PanelHabitaciones(Hotel hotel, Runnable alCambiar) {
        this.hotel = hotel;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        formulario.add(new JLabel("Número:"));
        formulario.add(txtNumero);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(cmbTipo);
        JButton btnRegistrar = new JButton("Registrar habitación");
        btnRegistrar.addActionListener(e -> registrar());
        formulario.add(btnRegistrar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(Ui.tabla(modelo)), BorderLayout.CENTER);
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
