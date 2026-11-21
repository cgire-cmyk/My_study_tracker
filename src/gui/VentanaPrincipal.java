package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import planificacion.Estudiante;

public class VentanaPrincipal extends JFrame {

    private static final int ANCHO = 560;
    private static final int ALTO = 460;

    private Estudiante estudiante;

    private JButton btnResumen;
    private JButton btnCurso;
    private JButton btnEvaluacion;
    private JButton btnNota;
    private JButton btnMetas;
    private JButton btnAlertas;
    private JButton btnHorario;
    private JButton btnSalir;

    public VentanaPrincipal(Estudiante estudiante) {
        super("MyStudyTracker - Chachani Dev");
        this.estudiante = estudiante;
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout(10, 10));

        JPanel encabezado = new JPanel(new GridLayout(3, 1));
        encabezado.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));
        JLabel titulo = new JLabel("MyStudyTracker", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(31, 111, 235));
        encabezado.add(titulo);
        encabezado.add(new JLabel("Estudiante: " + estudiante.getNombres()
                + " (" + estudiante.getCodigo() + ")", JLabel.CENTER));
        encabezado.add(new JLabel("Carrera: " + estudiante.getCarrera(), JLabel.CENTER));
        add(encabezado, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(4, 2, 10, 10));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        Listener listener = new Listener();

        btnResumen = crearBoton("Resumen de cursos", listener, menu);
        btnCurso = crearBoton("Registrar curso", listener, menu);
        btnEvaluacion = crearBoton("Registrar evaluacion", listener, menu);
        btnNota = crearBoton("Registrar nota", listener, menu);
        btnMetas = crearBoton("Metas academicas", listener, menu);
        btnAlertas = crearBoton("Alertas y recomendaciones", listener, menu);
        btnHorario = crearBoton("Horario de estudio", listener, menu);
        btnSalir = crearBoton("Salir", listener, menu);
        btnSalir.setForeground(new Color(200, 40, 40));
        add(menu, BorderLayout.CENTER);

        JLabel pie = new JLabel("Gestion y prediccion academica - UNSA 2026", JLabel.CENTER);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pie.setForeground(Color.GRAY);
        add(pie, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, ActionListener listener, JPanel panel) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boton.addActionListener(listener);
        panel.add(boton);
        return boton;
    }

    private boolean hayCursos() {
        if (estudiante.getCursos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero registra al menos un curso.",
                    "Sin cursos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object origen = e.getSource();
            if (origen == btnResumen) {
                new VentanaResumen(estudiante);
            } else if (origen == btnCurso) {
                new VentanaRegistroCurso(estudiante);
            } else if (origen == btnEvaluacion) {
                if (hayCursos()) {
                    new VentanaRegistroEvaluacion(estudiante);
                }
            } else if (origen == btnNota) {
                if (hayCursos()) {
                    new VentanaRegistroNota(estudiante);
                }
            } else if (origen == btnMetas) {
                new VentanaMetas(estudiante);
            } else if (origen == btnAlertas) {
                new VentanaAlertas(estudiante);
            } else if (origen == btnHorario) {
                new VentanaHorario(estudiante);
            } else if (origen == btnSalir) {
                System.exit(0);
            }
        }
    }
}
