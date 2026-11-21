package gui;

import java.awt.BorderLayout;
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
import nucleo.Curso;
import planificacion.Estudiante;

public class VentanaRegistroCurso extends JFrame {

    private static final int ANCHO = 400;
    private static final int ALTO = 220;
    private static final Integer[] OPCIONES_CREDITOS = {1, 2, 3, 4, 5, 6};

    private Estudiante estudiante;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox<Integer> cmbCreditos;
    private JButton btnRegistrar;
    private JButton btnLimpiar;

    public VentanaRegistroCurso(Estudiante estudiante) {
        super("Registrar curso");
        this.estudiante = estudiante;
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        txtCodigo = new JTextField(10);
        txtNombre = new JTextField(20);
        cmbCreditos = new JComboBox<>(OPCIONES_CREDITOS);
        cmbCreditos.setSelectedItem(Curso.CREDITOS_POR_DEFECTO);
        formulario.add(new JLabel("Codigo del curso:"));
        formulario.add(txtCodigo);
        formulario.add(new JLabel("Nombre del curso:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Creditos:"));
        formulario.add(cmbCreditos);
        add(formulario, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        Listener listener = new Listener();
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnRegistrar.addActionListener(listener);
        btnLimpiar.addActionListener(listener);
        txtNombre.addActionListener(listener);   
        botones.add(btnRegistrar);
        botones.add(btnLimpiar);
        add(botones, BorderLayout.SOUTH);
    }

    private void registrar() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa el codigo y el nombre del curso.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int creditos = (Integer) cmbCreditos.getSelectedItem();
        if (estudiante.agregarCurso(new Curso(codigo, nombre, creditos))) {
            JOptionPane.showMessageDialog(this, "Curso " + codigo + " registrado.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un curso con el codigo " + codigo + ".",
                    "Curso repetido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        cmbCreditos.setSelectedItem(Curso.CREDITOS_POR_DEFECTO);
        txtCodigo.requestFocus();
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnLimpiar) {
                limpiar();
            } else {
                registrar();
            }
        }
    }
}
