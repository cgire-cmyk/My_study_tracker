package gui;

import analisis.Alerta;
import analisis.GestorAlertas;
import analisis.GestorRecomendaciones;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import planificacion.Estudiante;

public class VentanaAlertas extends JFrame {

    private static final int ANCHO = 820;
    private static final int ALTO = 560;

    private Estudiante estudiante;
    private GestorAlertas gestorAlertas;
    private GestorRecomendaciones gestorRecomendaciones;
    private JLabel lblResumen;
    private JTextArea areaAlertas;
    private JTextArea areaRecomendaciones;
    private JButton btnActualizar;

    public VentanaAlertas(Estudiante estudiante) {
        super("Alertas y recomendaciones");
        this.estudiante = estudiante;
        this.gestorAlertas = new GestorAlertas();
        this.gestorRecomendaciones = new GestorRecomendaciones();
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        mostrarDatos();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout(5, 5));

        lblResumen = new JLabel("", JLabel.CENTER);
        lblResumen.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblResumen.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblResumen, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        areaAlertas = crearArea();
        areaAlertas.setRows(5);
        areaAlertas.setForeground(new Color(170, 30, 30));
        areaRecomendaciones = crearArea();
        centro.add(crearSeccion("Alertas de riesgo", areaAlertas), BorderLayout.NORTH);
        centro.add(crearSeccion("Recomendaciones", areaRecomendaciones), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new Listener());
        botones.add(btnActualizar);
        add(botones, BorderLayout.SOUTH);
    }

    private JTextArea crearArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return area;
    }

    private JScrollPane crearSeccion(String titulo, JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createTitledBorder(titulo));
        return scroll;
    }

    private void mostrarDatos() {
        ArrayList<Alerta> alertas = gestorAlertas.generarAlertas(estudiante.getCursos());
        int altos = gestorAlertas.contarAlertasPorNivel("ALTO");
        int medios = gestorAlertas.contarAlertasPorNivel("MEDIO");
        if (alertas.isEmpty()) {
            lblResumen.setText("Ningun curso en riesgo");
            lblResumen.setForeground(new Color(30, 140, 60));
            areaAlertas.setText("No hay alertas.");
        } else {
            lblResumen.setText("Cursos en riesgo: " + altos + " ALTO, " + medios + " MEDIO");
            lblResumen.setForeground(altos > 0 ? new Color(200, 40, 40) : new Color(190, 130, 0));
            StringBuilder texto = new StringBuilder();
            for (Alerta a : alertas) {
                texto.append("- ").append(a).append("\n");
            }
            areaAlertas.setText(texto.toString());
        }

        ArrayList<String> recomendaciones = gestorRecomendaciones.generarRecomendaciones(estudiante);
        StringBuilder texto = new StringBuilder();
        for (String r : recomendaciones) {
            texto.append("- ").append(r).append("\n");
        }
        areaRecomendaciones.setText(recomendaciones.isEmpty()
                ? "No hay recomendaciones por ahora." : texto.toString());
        areaAlertas.setCaretPosition(0);
        areaRecomendaciones.setCaretPosition(0);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mostrarDatos();
        }
    }
}
