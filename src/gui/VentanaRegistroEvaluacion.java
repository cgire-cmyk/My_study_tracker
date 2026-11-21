package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import nucleo.Curso;
import nucleo.Evaluacion;
import nucleo.ExamenParcial;
import nucleo.PracticaCalificada;
import nucleo.Trabajo;
import planificacion.Estudiante;

public class VentanaRegistroEvaluacion extends JFrame {

    private static final int ANCHO = 500;
    private static final int ALTO = 380;

    private Estudiante estudiante;
    private JComboBox<String> cmbCurso;
    private JRadioButton rbPractica;
    private JRadioButton rbTrabajo;
    private JRadioButton rbExamen;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPeso;
    private JLabel lblPesoDisponible;
    private JLabel lblNumero;
    private JTextField txtNumero;
    private JCheckBox chkGrupal;
    private JButton btnRegistrar;

    public VentanaRegistroEvaluacion(Estudiante estudiante) {
        super("Registrar evaluacion");
        this.estudiante = estudiante;
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        actualizarPesoDisponible();
        actualizarCamposSegunTipo();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout());
        Listener listener = new Listener();

        JPanel formulario = new JPanel(new GridLayout(8, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        cmbCurso = new JComboBox<>();
        for (Curso c : estudiante.getCursos()) {
            cmbCurso.addItem(c.getCodigo() + " - " + c.getNombre());
        }
        cmbCurso.addActionListener(listener);
        formulario.add(new JLabel("Curso:"));
        formulario.add(cmbCurso);

        rbPractica = new JRadioButton("Practica", true);
        rbTrabajo = new JRadioButton("Trabajo");
        rbExamen = new JRadioButton("Examen");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbPractica);
        grupoTipo.add(rbTrabajo);
        grupoTipo.add(rbExamen);
        rbPractica.addActionListener(listener);
        rbTrabajo.addActionListener(listener);
        rbExamen.addActionListener(listener);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTipo.add(rbPractica);
        panelTipo.add(rbTrabajo);
        panelTipo.add(rbExamen);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(panelTipo);

        txtCodigo = new JTextField(8);
        txtNombre = new JTextField(15);
        txtPeso = new JTextField(5);
        formulario.add(new JLabel("Codigo (ej. PC1):"));
        formulario.add(txtCodigo);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Peso (%):"));
        formulario.add(txtPeso);

        lblPesoDisponible = new JLabel();
        formulario.add(new JLabel("Peso disponible:"));
        formulario.add(lblPesoDisponible);

        lblNumero = new JLabel();
        txtNumero = new JTextField(5);
        formulario.add(lblNumero);
        formulario.add(txtNumero);

        chkGrupal = new JCheckBox("Trabajo grupal");
        formulario.add(new JLabel(""));
        formulario.add(chkGrupal);
        add(formulario, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        btnRegistrar = new JButton("Registrar evaluacion");
        btnRegistrar.addActionListener(listener);
        botones.add(btnRegistrar);
        add(botones, BorderLayout.SOUTH);
    }

    private Curso cursoSeleccionado() {
        return estudiante.getCursos().get(cmbCurso.getSelectedIndex());
    }

    private void actualizarPesoDisponible() {
        double disponible = 100 - cursoSeleccionado().calcularSumaPesos();
        lblPesoDisponible.setText(String.format("%.1f %%", disponible));
    }

    private void actualizarCamposSegunTipo() {
        if (rbPractica.isSelected()) {
            lblNumero.setText("N.o de practica:");
            txtNumero.setEnabled(true);
            chkGrupal.setEnabled(false);
            chkGrupal.setSelected(false);
        } else if (rbExamen.isSelected()) {
            lblNumero.setText("Unidad que evalua:");
            txtNumero.setEnabled(true);
            chkGrupal.setEnabled(false);
            chkGrupal.setSelected(false);
        } else {
            lblNumero.setText("(no aplica)");
            txtNumero.setText("");
            txtNumero.setEnabled(false);
            chkGrupal.setEnabled(true);
        }
    }

    private void registrar() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        if (codigo.isEmpty() || nombre.isEmpty()) {
            mostrarError("Completa el codigo y el nombre de la evaluacion.");
            return;
        }
        double peso;
        int numero = 1;
        try {
            peso = Double.parseDouble(txtPeso.getText().trim().replace(',', '.'));
            if (txtNumero.isEnabled() && !txtNumero.getText().trim().isEmpty()) {
                numero = Integer.parseInt(txtNumero.getText().trim());
            }
        } catch (NumberFormatException ex) {
            mostrarError("El peso y el numero deben ser valores numericos.");
            return;
        }

        Evaluacion evaluacion;
        if (rbPractica.isSelected()) {
            evaluacion = new PracticaCalificada(codigo, nombre, peso, numero);
        } else if (rbTrabajo.isSelected()) {
            evaluacion = new Trabajo(codigo, nombre, peso, chkGrupal.isSelected());
        } else {
            evaluacion = new ExamenParcial(codigo, nombre, peso, numero);
        }

        Curso curso = cursoSeleccionado();
        if (curso.agregarEvaluacion(evaluacion)) {
            JOptionPane.showMessageDialog(this, "Evaluacion " + codigo + " (" + evaluacion.getTipo()
                    + ") registrada en " + curso.getCodigo() + ".\nPesos del curso: "
                    + curso.calcularSumaPesos() + "%", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            txtCodigo.setText("");
            txtNombre.setText("");
            txtPeso.setText("");
            txtNumero.setText("");
            actualizarPesoDisponible();
        } else {
            mostrarError("No se pudo registrar: codigo repetido, peso invalido\n"
                    + "o la suma de pesos pasa de 100%.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object origen = e.getSource();
            if (origen == cmbCurso) {
                actualizarPesoDisponible();
            } else if (origen == btnRegistrar) {
                registrar();
            } else {
                actualizarCamposSegunTipo();   
            }
        }
    }
}
