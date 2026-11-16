package com.ues.hotel.ui;

import com.ues.hotel.modelo.Cliente;
import com.ues.hotel.servicio.Hotel;
import com.ues.hotel.servicio.HotelException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
    private final JTextField txtBuscar = new JTextField(18);
    private final DefaultTableModel modelo = Ui.modelo("ID", "Nombre", "DUI", "Teléfono", "Correo");
    private final Color Azul = new Color (23, 43, 77);
    private final Color Gold = new Color (198, 161, 91);
    private final Color Crema = new Color (244, 241, 234);
    
    
    
    
    public PanelClientes(Hotel hotel, Runnable alCambiar) {
        this.hotel = hotel;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground (Crema);
        //encabezado del panel
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Azul);
        encabezado.setBorder(new EmptyBorder(15,20,15,20));
        
        JLabel titulo = new JLabel("GRAND HOTEL");
        titulo.setFont(new Font("Serif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);

        
        JLabel subtitulo = new JLabel("Administración de clientes");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(Color.WHITE);
        
        JPanel textos = new JPanel(new java.awt.GridLayout(2, 1));
        textos.setBackground(Azul);
        textos.add(titulo);
        textos.add(subtitulo);

        encabezado.add(textos, BorderLayout.CENTER);
        

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBackground(Crema);
        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 12));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(20, 15, 20, 15));
        formulario.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Azul);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
        formulario.add(lblNombre);
        formulario.add(txtNombre);
        JLabel lblDui = new JLabel("DUI:");
        lblDui.setForeground(Azul);
        lblDui.setFont(new Font("Arial", Font.BOLD, 13));
        formulario.add(lblDui);
        formulario.add(txtDui);
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(Azul);
        lblTelefono.setFont(new Font("Arial", Font.BOLD, 13));
        formulario.add(lblTelefono);
        formulario.add(txtTelefono);
        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(Azul);
        lblCorreo.setFont(new Font("Arial", Font.BOLD, 13));
        formulario.add(lblCorreo);
        formulario.add(txtCorreo);
        
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDui.setFont(new Font("Arial", Font.PLAIN, 13));
        txtTelefono.setFont(new Font("Arial", Font.PLAIN, 13));
        txtCorreo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton btnRegistrar = new JButton("REGISTRAR CLIENTE");
        btnRegistrar.setBackground(Gold);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.addActionListener(e -> registrar());
        formulario.add(btnRegistrar);
        
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Azul);
        lblBuscar.setFont(new Font("Arial", Font.BOLD, 13));
        formulario.add(lblBuscar);
        formulario.add(txtBuscar);
        
        JButton btnBuscar = new JButton("BUSCAR");
        btnBuscar.setBackground(Azul);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.addActionListener(e -> buscar());
        formulario.add(btnBuscar);
        
        JButton btnMostrarTodos = new JButton("MOSTRAR TODOS");
        btnMostrarTodos.setBackground(Gold);
        btnMostrarTodos.setForeground(Color.WHITE);
        btnMostrarTodos.setFont(new Font("Arial", Font.BOLD, 12));
        btnMostrarTodos.setFocusPainted(false);
        btnMostrarTodos.setBorderPainted(false);

        btnMostrarTodos.addActionListener(e -> {
    txtBuscar.setText("");
    actualizar();
});

        formulario.add(btnMostrarTodos);
        

        contenido.add(formulario, BorderLayout.NORTH);
        contenido.add(new JScrollPane(Ui.tabla(modelo)), BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
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
        
    private void buscar() {
    String texto = txtBuscar.getText().trim().toLowerCase();

    modelo.setRowCount(0);

    for (Cliente c : hotel.getClientes()) {
        if (c.getNombre().toLowerCase().contains(texto)
                || c.getDui().toLowerCase().contains(texto)) {

            modelo.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getDui(),
                c.getTelefono(),
                c.getCorreo()
            });
        }
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
