package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import planificacion.BloqueEstudio;
import planificacion.Estudiante;
import planificacion.HorarioEstudio;

public class VentanaHorario extends JFrame {

    private static final int ANCHO = 760;
    private static final int ALTO = 480;

    private Estudiante estudiante;
    private JTextField[] txtHoras;   
    private JButton btnGenerar;
    private JTextArea areaHorario;

    public VentanaHorario(Estudiante estudiante) {
        super("Horario de estudio");
        this.estudiante = estudiante;
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createContents();
        setVisible(true);
    }

    private void createContents() {
        setLayout(new BorderLayout(10, 10));
        HorarioEstudio horario = estudiante.getHorario();

        // Panel izquierdo: horas libres por dia
        JPanel panelDias = new JPanel(new GridLayout(HorarioEstudio.DIAS.length + 2, 2, 6, 6));
        panelDias.setBorder(BorderFactory.createTitledBorder("Horas libres por dia (0-12)"));
        panelDias.add(new JLabel("Dia"));
        panelDias.add(new JLabel("Horas"));
        txtHoras = new JTextField[HorarioEstudio.DIAS.length];
        for (int i = 0; i < HorarioEstudio.DIAS.length; i++) {
            String dia = HorarioEstudio.DIAS[i];
            txtHoras[i] = new JTextField(String.valueOf(horario.getHorasDisponibles(dia)), 4);
            panelDias.add(new JLabel(dia));
            panelDias.add(txtHoras[i]);
        }
        btnGenerar = new JButton("Generar horario");
        btnGenerar.addActionListener(new Listener());
        panelDias.add(new JLabel(""));
        panelDias.add(btnGenerar);
        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.add(panelDias, BorderLayout.NORTH);
        add(izquierda, BorderLayout.WEST);

        areaHorario = new JTextArea("Registra tus horas libres y presiona \"Generar horario\".");
        areaHorario.setEditable(false);
        areaHorario.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaHorario);
        scroll.setBorder(BorderFactory.createTitledBorder("Horario semanal"));
        add(scroll, BorderLayout.CENTER);
    }

    private void generar() {
        HorarioEstudio horario = estudiante.getHorario();
        for (int i = 0; i < HorarioEstudio.DIAS.length; i++) {
            String dia = HorarioEstudio.DIAS[i];
            int horas;
            try {
                horas = Integer.parseInt(txtHoras[i].getText().trim());
            } catch (NumberFormatException ex) {
                horas = -1;
            }
            if (!horario.definirDisponibilidad(dia, horas)) {
                JOptionPane.showMessageDialog(this, "Horas no validas para el " + dia
                        + ".\nIngresa un numero entero entre 0 y 12.", "Error", JOptionPane.ERROR_MESSAGE);
                txtHoras[i].requestFocus();
                return;
            }
        }

        horario.generarHorario(estudiante.getCursos());
        if (horario.calcularTotalHoras() == 0) {
            areaHorario.setText("No se asignaron horas: registra cursos y al menos un dia con horas libres.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        for (String dia : HorarioEstudio.DIAS) {
            ArrayList<BloqueEstudio> bloques = horario.obtenerBloquesPorDia(dia);
            if (!bloques.isEmpty()) {
                texto.append(dia).append(":\n");
                for (BloqueEstudio b : bloques) {
                    texto.append("    ").append(b).append("\n");
                }
            }
        }
        texto.append("\nTotal semanal: ").append(horario.calcularTotalHoras()).append(" horas");
        areaHorario.setText(texto.toString());
        areaHorario.setCaretPosition(0);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            generar();
        }
    }
}
