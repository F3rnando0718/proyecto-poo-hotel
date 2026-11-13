
package com.ues.hotel.ui;

/**
 * Pantalla principal  que permite al usuario ingresas a la aplicacion
 * @author Genesis Abigail Garcia F
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;



public class LoginFrame extends JFrame {
    
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    
    private final Color Azul = new Color (23, 43, 77);
    private final Color Gold = new Color (198, 161, 91);
    private final Color Crema  = new Color (244, 241, 234);
    
    public LoginFrame(){
    
    setTitle ("Grand Hotel  -  Inicio de sesión");
    setSize (500,580);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
    
    inicializarComponentes();
    }
    
    private void inicializarComponentes(){
    
    JPanel fondo = new JPanel (new GridBagLayout());
    fondo.setBackground(Azul);
    JPanel tarjeta = new JPanel ();
    
    tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
    tarjeta.setBackground(Color.WHITE);
    tarjeta.setBorder(new EmptyBorder(30, 40, 30, 40));
    tarjeta.setPreferredSize(new Dimension(370, 440));

    JLabel icono = new JLabel("♜");
    icono.setFont(new Font("Serif", Font.PLAIN, 45));
    icono.setForeground(Gold);
    icono.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel titulo = new JLabel("GRAND HOTEL");
    titulo.setFont(new Font("Serif", Font.BOLD, 27));
    titulo.setForeground(Azul);
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitulo = new JLabel("Sistema de gestión de reservas");
    subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
    subtitulo.setForeground(Color.GRAY);
    subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JLabel lblBienvenida = new JLabel("Bienvenido");
    lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));
    lblBienvenida.setForeground(Azul);
    lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    tarjeta.add(icono);
    tarjeta.add(Box.createVerticalStrut(5));
    tarjeta.add(titulo);
    tarjeta.add(Box.createVerticalStrut(5));
    tarjeta.add(subtitulo);
    tarjeta.add(Box.createVerticalStrut(25));
    tarjeta.add(lblBienvenida);
    tarjeta.add(Box.createVerticalStrut(25));
    
    JLabel lblUsuario = new JLabel("Usuario");
    estilizarEtiqueta(lblUsuario);
    tarjeta.add(lblUsuario);
    tarjeta.add(Box.createVerticalStrut(7));
    
    
    txtUsuario = new JTextField();
    estilizarCampo(txtUsuario);
    tarjeta.add(txtUsuario);

    tarjeta.add(Box.createVerticalStrut(18));

    JLabel lblContrasena = new JLabel("Contraseña");
    estilizarEtiqueta(lblContrasena);
    tarjeta.add(lblContrasena);
    tarjeta.add(Box.createVerticalStrut(7));

    txtContrasena = new JPasswordField();
    estilizarCampo(txtContrasena);
    tarjeta.add(txtContrasena);
    
    tarjeta.add(Box.createVerticalStrut(28));
    
    btnIniciarSesion = new JButton("INICIAR SESIÓN");
        btnIniciarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setBackground(Gold);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setBorderPainted(false);
        btnIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciarSesion.setMaximumSize(new Dimension(290, 42));
        btnIniciarSesion.setPreferredSize(new Dimension(290, 42));
    
    
    tarjeta.add(btnIniciarSesion);
    tarjeta.add(Box.createVerticalStrut(22));
    
    
    JLabel pie = new JLabel("Tu comodidad es nuestra prioridad");
    pie.setFont(new Font("Arial", Font.ITALIC, 11));
    pie.setForeground(Color.GRAY);
    pie.setAlignmentX(Component.CENTER_ALIGNMENT);
    tarjeta.add(pie);

    fondo.add(tarjeta);
    add(fondo);
    }
    
    private void estilizarEtiqueta(JLabel etiqueta) {
        etiqueta.setFont(new Font("Arial", Font.BOLD, 13));
        etiqueta.setForeground(Azul);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
     private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setMaximumSize(new Dimension(290, 38));
        campo.setPreferredSize(new Dimension(290, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }
    }


    
    
    

