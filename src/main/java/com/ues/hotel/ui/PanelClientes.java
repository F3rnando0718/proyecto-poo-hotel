package com.ues.hotel.ui;

import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Registro y listado de clientes.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class PanelClientes extends JPanel implements Actualizable {

    private static final long serialVersionUID = 1L;

    private final Hotel hotel;
    private final Runnable alCambiar;
    private final JTextField txtNombre = new JTextField(14);
    private final JTextField txtDui = new JTextField(9);
    private final JTextField txtTelefono = new JTextField(9);
    private final JTextField txtCorreo = new JTextField(14);
    private final DefaultTableModel modelo = Ui.modelo("ID", "Nombre", "DUI", "Teléfono", "Correo");

    public PanelClientes(Hotel hotel, Runnable alCambiar) {
        this.hotel = hotel;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("DUI:"));
        formulario.add(txtDui);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(txtTelefono);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);
        JButton btnRegistrar = new JButton("Registrar cliente");
        btnRegistrar.addActionListener(e -> registrar());
        formulario.add(btnRegistrar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(Ui.tabla(modelo)), BorderLayout.CENTER);
        actualizar();
    }

    private void registrar() {
        try {
            hotel.registrarCliente(txtNombre.getText(), txtDui.getText(), txtTelefono.getText(), txtCorreo.getText());
            txtNombre.setText("");
            txtDui.setText("");
            txtTelefono.setText("");
            txtCorreo.setText("");
            alCambiar.run();
        } catch (HotelException ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    @Override
    public void actualizar() {
        modelo.setRowCount(0);
        for (Cliente c : hotel.getClientes()) {
            modelo.addRow(new Object[] {c.getId(), c.getNombre(), c.getDui(), c.getTelefono(), c.getCorreo()});
        }
    }
}
