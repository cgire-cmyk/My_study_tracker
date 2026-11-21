package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import nucleo.Calificacion;
import nucleo.Curso;
import planificacion.Estudiante;
import planificacion.Meta;
import planificacion.MetaAprobacion;
import planificacion.MetaPromedio;

public class VentanaMetas extends JFrame {

    private static final int ANCHO = 640;
    private static final int ALTO = 560;
    private static final String[] CATEGORIAS = {
        "Tercio superior", "Quinto superior", "Beca", "Otra meta de promedio"
    };

    private Estudiante estudiante;
    private JRadioButton rbAprobacion;
    private JRadioButton rbPromedio;
    private JComboBox<String> cmbCurso;
    private JComboBox<String> cmbCategoria;
    private JTextField txtObjetivo;
    private JButton btnRegistrar;
    private JTextArea areaMetas;

    public VentanaMetas(Estudiante estudiante) {
        super("Metas academicas");
        this.estudiante = estudiante;
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        actualizarCamposSegunTipo();
        mostrarMetas();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout(5, 5));
        Listener listener = new Listener();

        JPanel formulario = new JPanel(new GridLayout(5, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva meta"));

        rbAprobacion = new JRadioButton("Nota en un curso", true);
        rbPromedio = new JRadioButton("Promedio general");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbAprobacion);
        grupoTipo.add(rbPromedio);
        rbAprobacion.addActionListener(listener);
        rbPromedio.addActionListener(listener);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTipo.add(rbAprobacion);
        panelTipo.add(rbPromedio);
        formulario.add(new JLabel("Tipo de meta:"));
        formulario.add(panelTipo);

        cmbCurso = new JComboBox<>();
        for (Curso c : estudiante.getCursos()) {
            cmbCurso.addItem(c.getCodigo() + " - " + c.getNombre());
        }
        formulario.add(new JLabel("Curso:"));
        formulario.add(cmbCurso);

        cmbCategoria = new JComboBox<>(CATEGORIAS);
        formulario.add(new JLabel("Categoria:"));
        formulario.add(cmbCategoria);

        txtObjetivo = new JTextField(String.valueOf(Calificacion.NOTA_APROBATORIA));
        formulario.add(new JLabel("Nota o promedio objetivo:"));
        formulario.add(txtObjetivo);

        btnRegistrar = new JButton("Registrar meta");
        btnRegistrar.addActionListener(listener);
        formulario.add(new JLabel(""));
        formulario.add(btnRegistrar);
        add(formulario, BorderLayout.NORTH);

        // Lista de metas
        areaMetas = new JTextArea();
        areaMetas.setEditable(false);
        areaMetas.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaMetas);
        scroll.setBorder(BorderFactory.createTitledBorder("Mis metas"));
        add(scroll, BorderLayout.CENTER);
    }
    private void actualizarCamposSegunTipo() {
        boolean porCurso = rbAprobacion.isSelected();
        cmbCurso.setEnabled(porCurso);
        cmbCategoria.setEnabled(!porCurso);
    }

    private void registrar() {
        double objetivo;
        try {
            objetivo = Double.parseDouble(txtObjetivo.getText().trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa un objetivo numerico.");
            return;
        }
        if (!Calificacion.esNotaValida(objetivo)) {
            mostrarError("El objetivo debe estar entre 0 y 20.");
            return;
        }
        if (estudiante.getCursos().isEmpty()) {
            mostrarError("Primero registra al menos un curso.");
            return;
        }

        Meta meta;
        if (rbAprobacion.isSelected()) {
            Curso curso = estudiante.getCursos().get(cmbCurso.getSelectedIndex());
            meta = new MetaAprobacion(curso, objetivo);
        } else {
            String categoria = (String) cmbCategoria.getSelectedItem();
            meta = new MetaPromedio(categoria, objetivo, estudiante.getCursos());
        }
        estudiante.agregarMeta(meta);
        mostrarMetas();
        JOptionPane.showMessageDialog(this, "Meta registrada.\n" + meta.obtenerEstado(),
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMetas() {
        if (estudiante.getMetas().isEmpty()) {
            areaMetas.setText("Aun no hay metas registradas.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        for (Meta m : estudiante.getMetas()) {
            texto.append("[").append(m.getTipo()).append("] ").append(m.getNombre()).append("\n");
            texto.append("    ").append(m.obtenerEstado()).append("\n\n");
        }
        areaMetas.setText(texto.toString());
        areaMetas.setCaretPosition(0);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnRegistrar) {
                registrar();
            } else {
                actualizarCamposSegunTipo();
            }
        }
    }
}
