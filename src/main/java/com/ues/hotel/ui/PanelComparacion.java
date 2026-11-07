package com.ues.hotel.ui;

import com.ues.hotel.comparacion.EscenarioOrientadoAObjetos;
import com.ues.hotel.estructurado.ProgramaEstructurado;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Ejecuta el mismo escenario con el enfoque estructurado y con el orientado a objetos.
 *
 * @author Genesis Abigail García Flores
 * @author Fernando Josué López Recinos
 * @author Ernesto Isaí Figueroa Moran
 */
public class PanelComparacion extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextArea areaEstructurado = crearArea();
    private final JTextArea areaObjetos = crearArea();
    private final JLabel lblResultado = new JLabel(" ");

    public PanelComparacion() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnEjecutar = new JButton("Ejecutar el mismo escenario en ambos enfoques");
        btnEjecutar.addActionListener(e -> ejecutar());
        JPanel norte = new JPanel(new BorderLayout());
        norte.add(btnEjecutar, BorderLayout.WEST);
        add(norte, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 8, 8));
        centro.add(conTitulo(areaEstructurado, "Enfoque 1: programación estructurada"));
        centro.add(conTitulo(areaObjetos, "Enfoque 2: diseño orientado a objetos"));
        add(centro, BorderLayout.CENTER);
        add(lblResultado, BorderLayout.SOUTH);
    }

    private void ejecutar() {
        String estructurado = ProgramaEstructurado.ejecutarEscenario();
        String objetos = EscenarioOrientadoAObjetos.ejecutarEscenario();
        areaEstructurado.setText(estructurado);
        areaObjetos.setText(objetos);
        areaEstructurado.setCaretPosition(0);
        areaObjetos.setCaretPosition(0);
        lblResultado.setText(estructurado.equals(objetos)
                ? "Ambos enfoques producen exactamente el mismo resultado funcional."
                : "Los resultados difieren: revise las implementaciones.");
    }

    private static JTextArea crearArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        return area;
    }

    private static JPanel conTitulo(JTextArea area, String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }
}
