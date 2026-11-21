package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.Evaluacion;
import planificacion.Estudiante;

public class VentanaResumen extends JFrame {

    private static final int ANCHO = 760;
    private static final int ALTO = 560;

    private Estudiante estudiante;
    private JTextArea areaResumen;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public VentanaResumen(Estudiante estudiante) {
        super("Resumen de cursos");
        this.estudiante = estudiante;
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        mostrarResumen();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout());
        areaResumen = new JTextArea();
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(areaResumen), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        Listener listener = new Listener();
        btnActualizar = new JButton("Actualizar");
        btnCerrar = new JButton("Cerrar");
        btnActualizar.addActionListener(listener);
        btnCerrar.addActionListener(listener);
        botones.add(btnActualizar);
        botones.add(btnCerrar);
        add(botones, BorderLayout.SOUTH);
    }

    private void mostrarResumen() {
        if (estudiante.getCursos().isEmpty()) {
            areaResumen.setText("Aun no hay cursos registrados.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        for (Curso c : estudiante.getCursos()) {
            texto.append(c.getCodigo()).append(" - ").append(c.getNombre())
                 .append(" (").append(c.getCreditos()).append(" creditos)\n");
            for (Evaluacion e : c.getEvaluaciones()) {
                String nota = e.estaRendida() ? String.format("%.2f", e.getNota()) : "pendiente";
                texto.append(String.format("   %-6s %-22s %-20s %5.1f%%  nota: %s%n",
                        e.getCodigo(), e.getNombre(), e.getTipo(), e.getPeso(), nota));
            }
            if (!c.pesosCompletos()) {
                texto.append(String.format("   AVISO: los pesos suman %.1f%%, deberian sumar 100%%.%n",
                        c.calcularSumaPesos()));
            }
            texto.append(String.format("   Puntaje acumulado: %.2f | Promedio actual: %.2f | Riesgo: %s%n",
                    c.calcularPuntajeAcumulado(), c.calcularPromedioActual(), c.obtenerNivelRiesgo()));
            texto.append("   ").append(textoNotaNecesaria(c)).append("\n\n");
        }
        texto.append(String.format("Total de creditos: %d | Promedio ponderado actual: %.2f",
                estudiante.calcularTotalCreditos(), estudiante.calcularPromedioPonderadoActual()));
        areaResumen.setText(texto.toString());
        areaResumen.setCaretPosition(0);
    }

    private String textoNotaNecesaria(Curso c) {
        double necesaria = c.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        if (necesaria > Calificacion.NOTA_MAXIMA) {
            return "Para aprobar: ya no es posible.";
        }
        if (necesaria == 0) {
            return "Para aprobar: ya esta asegurado.";
        }
        return String.format("Para aprobar necesitas %.2f en lo pendiente.", necesaria);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnActualizar) {
                mostrarResumen();
            } else {
                dispose();
            }
        }
    }
}
