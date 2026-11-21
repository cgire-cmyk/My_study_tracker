package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.Evaluacion;
import planificacion.Estudiante;

public class VentanaRegistroNota extends JFrame {

    private static final int ANCHO = 660;
    private static final int ALTO = 300;

    private Estudiante estudiante;
    private JComboBox<String> cmbCurso;
    private JComboBox<String> cmbEvaluacion;
    private JTextField txtNota;
    private JTextField txtObservacion;
    private JLabel lblNecesaria;
    private JButton btnRegistrar;

    public VentanaRegistroNota(Estudiante estudiante) {
        super("Registrar nota");
        this.estudiante = estudiante;
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        cargarEvaluaciones();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout());
        Listener listener = new Listener();

        JPanel formulario = new JPanel(new GridLayout(5, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        cmbCurso = new JComboBox<>();
        for (Curso c : estudiante.getCursos()) {
            cmbCurso.addItem(c.getCodigo() + " - " + c.getNombre());
        }
        cmbCurso.addActionListener(listener);
        cmbEvaluacion = new JComboBox<>();
        txtNota = new JTextField(5);
        txtNota.addActionListener(listener);   
        txtObservacion = new JTextField(20);
        lblNecesaria = new JLabel();

        formulario.add(new JLabel("Curso:"));
        formulario.add(cmbCurso);
        formulario.add(new JLabel("Evaluacion:"));
        formulario.add(cmbEvaluacion);
        formulario.add(new JLabel("Nota (" + (int) Calificacion.NOTA_MINIMA + " - "
                + (int) Calificacion.NOTA_MAXIMA + "):"));
        formulario.add(txtNota);
        formulario.add(new JLabel("Observacion (opcional):"));
        formulario.add(txtObservacion);
        formulario.add(new JLabel("Situacion del curso:"));
        formulario.add(lblNecesaria);
        add(formulario, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        btnRegistrar = new JButton("Registrar nota");
        btnRegistrar.addActionListener(listener);
        botones.add(btnRegistrar);
        add(botones, BorderLayout.SOUTH);
    }

    private Curso cursoSeleccionado() {
        return estudiante.getCursos().get(cmbCurso.getSelectedIndex());
    }

    private void cargarEvaluaciones() {
        Curso curso = cursoSeleccionado();
        cmbEvaluacion.removeAllItems();
        for (Evaluacion e : curso.getEvaluaciones()) {
            String estado = e.estaRendida() ? String.format("nota %.2f", e.getNota()) : "pendiente";
            cmbEvaluacion.addItem(e.getCodigo() + " - " + e.getNombre()
                    + " (" + (int) e.getPeso() + "%) - " + estado);
        }
        btnRegistrar.setEnabled(!curso.getEvaluaciones().isEmpty());
        mostrarSituacion(curso);
    }

    private void mostrarSituacion(Curso curso) {
        if (curso.getEvaluaciones().isEmpty()) {
            lblNecesaria.setText("Sin evaluaciones registradas");
            lblNecesaria.setForeground(Color.GRAY);
            return;
        }
        double necesaria = curso.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        if (necesaria > Calificacion.NOTA_MAXIMA) {
            lblNecesaria.setText("Ya no es posible aprobar");
        } else if (necesaria == 0) {
            lblNecesaria.setText("Aprobacion asegurada");
        } else {
            lblNecesaria.setText(String.format("Necesitas %.2f para aprobar", necesaria));
        }
        String riesgo = curso.obtenerNivelRiesgo();
        if (riesgo.equals("ALTO")) {
            lblNecesaria.setForeground(new Color(200, 40, 40));
        } else if (riesgo.equals("MEDIO")) {
            lblNecesaria.setForeground(new Color(190, 130, 0));
        } else {
            lblNecesaria.setForeground(new Color(30, 140, 60));
        }
    }

    private void registrar() {
        if (cmbEvaluacion.getSelectedIndex() < 0) {
            mostrarError("El curso no tiene evaluaciones registradas.");
            return;
        }
        double nota;
        try {
            nota = Double.parseDouble(txtNota.getText().trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa una nota numerica.");
            return;
        }
        if (!Calificacion.esNotaValida(nota)) {
            mostrarError("La nota debe estar entre " + (int) Calificacion.NOTA_MINIMA
                    + " y " + (int) Calificacion.NOTA_MAXIMA + ".");
            return;
        }
        Curso curso = cursoSeleccionado();
        int indice = cmbEvaluacion.getSelectedIndex();
        Evaluacion evaluacion = curso.getEvaluaciones().get(indice);
        String observacion = txtObservacion.getText().trim();

        boolean ok = observacion.isEmpty()
                ? evaluacion.registrarNota(nota)
                : evaluacion.registrarNota(nota, observacion);
        if (ok) {
            cargarEvaluaciones();
            cmbEvaluacion.setSelectedIndex(indice);
            txtNota.setText("");
            txtObservacion.setText("");
            JOptionPane.showMessageDialog(this, "Nota registrada en " + evaluacion.getCodigo()
                    + ".\n" + lblNecesaria.getText() + " (riesgo " + curso.obtenerNivelRiesgo() + ").",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrarError("No se pudo registrar la nota.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cmbCurso) {
                cargarEvaluaciones();
            } else {
                registrar();   
            }
        }
    }
}
