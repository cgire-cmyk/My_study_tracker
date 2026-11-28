package gui;

import analisis.Alertable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import nucleo.Curso;
import nucleo.DatoInvalidoException;
import planificacion.BloqueEstudio;
import planificacion.HorarioEstudio;

public class PanelHorario extends PanelBase {
    private JTextField[] camposHoras;
    private JLabel etiquetaDisponible;
    private JButton botonGenerar;
    private JPanel panelSemana;
    private JScrollPane scrollSemana;
    private JTextArea areaResumen;

    public PanelHorario(VentanaPrincipal ventana) {
        super(ventana, "Horario de estudio",
                "Indica tus horas libres por día; el horario da prioridad a los cursos en riesgo ALTO y MEDIO.");

        JPanel centro = new JPanel(new BorderLayout(12, 12));
        centro.setBackground(Paleta.FONDO);
        centro.add(crearDisponibilidad(), BorderLayout.NORTH);
        centro.add(crearSemana(), BorderLayout.CENTER);
        JPanel resumen = crearResumen();
        resumen.setPreferredSize(new Dimension(290, 0));
        centro.add(resumen, BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearDisponibilidad() {
        JPanel tarjeta = crearTarjeta("Tus horas libres (0 a " + HorarioEstudio.MAX_HORAS_DIA + " por día)");
        JPanel rejilla = new JPanel(new GridLayout(2, HorarioEstudio.DIAS.length, 8, 4));
        rejilla.setBackground(Paleta.TARJETA);

        camposHoras = new JTextField[HorarioEstudio.DIAS.length];
        for (int i = 0; i < HorarioEstudio.DIAS.length; i++) {
            JLabel dia = crearEtiqueta(HorarioEstudio.DIAS_VISIBLES[i]);
            dia.setHorizontalAlignment(JLabel.CENTER);
            rejilla.add(dia);
        }
        for (int i = 0; i < HorarioEstudio.DIAS.length; i++) {
            camposHoras[i] = crearCampo(3);
            camposHoras[i].setHorizontalAlignment(JTextField.CENTER);
            rejilla.add(camposHoras[i]);
        }

        etiquetaDisponible = crearEtiqueta(" ");
        etiquetaDisponible.setForeground(Paleta.TEXTO_SUAVE);
        botonGenerar = crearBoton("Generar horario", Paleta.PRIMARIO);
        botonGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarHorario();
            }
        });
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(Paleta.TARJETA);
        pie.add(etiquetaDisponible, BorderLayout.CENTER);
        pie.add(botonGenerar, BorderLayout.EAST);

        tarjeta.add(rejilla, BorderLayout.CENTER);
        tarjeta.add(pie, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearSemana() {
        JPanel tarjeta = crearTarjeta(null);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(Paleta.TARJETA);
        JLabel titulo = new JLabel("Horario sugerido");
        titulo.setFont(Paleta.FUENTE_SUBTITULO);
        titulo.setForeground(Paleta.PRIMARIO);
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        leyenda.setBackground(Paleta.TARJETA);
        leyenda.add(crearEtiqueta("Riesgo:"));
        leyenda.add(crearSello(Alertable.RIESGO_ALTO, Paleta.ROJO));
        leyenda.add(crearSello(Alertable.RIESGO_MEDIO, Paleta.NARANJA));
        leyenda.add(crearSello(Alertable.RIESGO_BAJO, Paleta.VERDE));
        cabecera.add(titulo, BorderLayout.WEST);
        cabecera.add(leyenda, BorderLayout.EAST);

        panelSemana = new JPanel(new GridLayout(1, HorarioEstudio.DIAS.length, 6, 0));
        panelSemana.setBackground(Paleta.TARJETA);

        tarjeta.add(cabecera, BorderLayout.NORTH);
        scrollSemana = crearScroll(alinearArriba(panelSemana, Paleta.TARJETA));
        tarjeta.add(scrollSemana, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearResumen() {
        JPanel tarjeta = crearTarjeta("Resumen de la semana");
        areaResumen = crearAreaTexto(10, 20);
        areaResumen.setBackground(Paleta.FILA_ALTERNA);
        tarjeta.add(crearScroll(areaResumen), BorderLayout.CENTER);
        return tarjeta;
    }

    @Override
    public void actualizar() {
        HorarioEstudio horario = getEstudiante().getHorario();
        for (int i = 0; i < HorarioEstudio.DIAS.length; i++) {
            camposHoras[i].setText(String.valueOf(horario.getHorasDisponibles(HorarioEstudio.DIAS[i])));
        }
        horario.generarHorario(getEstudiante().getCursos());
        dibujarSemana();
        escribirResumen();
    }

    private void dibujarSemana() {
        HorarioEstudio horario = getEstudiante().getHorario();
        panelSemana.removeAll();
        for (int i = 0; i < HorarioEstudio.DIAS.length; i++) {
            String dia = HorarioEstudio.DIAS[i];
            ArrayList<BloqueEstudio> bloques = horario.obtenerBloquesPorDia(dia);

            JPanel columna = new JPanel(new BorderLayout(0, 4));
            columna.setBackground(Paleta.FILA_ALTERNA);
            JLabel cabecera = crearSello(HorarioEstudio.DIAS_VISIBLES[i], Paleta.PRIMARIO);

            JPanel listaBloques = new JPanel(new GridLayout(0, 1, 0, 4));
            listaBloques.setBackground(Paleta.FILA_ALTERNA);
            int horasDia = 0;
            for (BloqueEstudio bloque : bloques) {
                listaBloques.add(crearBloque(bloque));
                horasDia += bloque.getHoras();
            }
            if (bloques.isEmpty()) {
                JLabel libre = crearEtiqueta(horario.getHorasDisponibles(dia) == 0 ? "Sin horas" : "Libre");
                libre.setForeground(Paleta.TEXTO_SUAVE);
                libre.setHorizontalAlignment(JLabel.CENTER);
                listaBloques.add(libre);
            }

            JLabel total = new JLabel(horasDia + " h de " + horario.getHorasDisponibles(dia), JLabel.CENTER);
            total.setFont(Paleta.FUENTE_PEQUENA);
            total.setForeground(Paleta.TEXTO_SUAVE);

            columna.add(cabecera, BorderLayout.NORTH);
            columna.add(alinearArriba(listaBloques, Paleta.FILA_ALTERNA), BorderLayout.CENTER);
            columna.add(total, BorderLayout.SOUTH);
            panelSemana.add(columna);
        }
        etiquetaDisponible.setText("Disponible: " + horario.calcularTotalDisponible() + " h por semana  ·  "
                + "Asignado: " + horario.calcularTotalHoras() + " h");
        ajustarAlAncho(panelSemana);
        redistribuir(scrollSemana);
    }

    private JPanel crearBloque(BloqueEstudio bloque) {
        String nivel = bloque.getCurso().obtenerNivelRiesgo();
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(Paleta.colorRiesgoClaro(nivel));
        agregarMargen(panel, 5);
        JLabel codigo = new JLabel(bloque.getCurso().getCodigo(), JLabel.CENTER);
        codigo.setFont(Paleta.FUENTE_NEGRITA);
        codigo.setForeground(Paleta.colorRiesgo(nivel));
        JLabel horas = new JLabel(bloque.getHoras() + " h", JLabel.CENTER);
        horas.setFont(Paleta.FUENTE_PEQUENA);
        horas.setForeground(Paleta.TEXTO);
        panel.add(codigo);
        panel.add(horas);
        panel.setToolTipText(bloque.getCurso().getNombre() + " - riesgo " + nivel);
        return panel;
    }

    private void escribirResumen() {
        HorarioEstudio horario = getEstudiante().getHorario();
        ArrayList<Curso> cursos = getEstudiante().getCursos();
        if (cursos.isEmpty()) {
            areaResumen.setText("Aún no tienes cursos. Regístralos en «Cursos y notas» para repartir tus horas.");
            return;
        }
        String texto = "Total: " + horario.calcularTotalHoras() + " h de estudio en la semana.\n\n"
                + "Horas por curso (asignadas / recomendadas):\n";
        for (Curso curso : cursos) {
            texto += "• " + curso.getCodigo() + ": " + horario.calcularHorasAsignadas(curso) + " / "
                    + horario.calcularHorasRecomendadas(curso) + " h"
                    + (curso.estaTerminado() ? " (terminado)" : " (riesgo " + curso.obtenerNivelRiesgo() + ")") + "\n";
        }
        texto += "\nRegla: 1 h por crédito, +3 si el riesgo es ALTO y +1 si es MEDIO.\n";
        int sobrantes = horario.calcularTotalDisponible() - horario.calcularTotalHoras();
        if (horario.getHorasSinAsignar() > 0) {
            texto += "Faltaron " + horario.getHorasSinAsignar()
                    + " h por falta de tiempo libre: si puedes, aumenta tu disponibilidad.";
        } else if (sobrantes > 0) {
            texto += "Te quedan " + sobrantes + " h libres: úsalas para repasar o adelantar trabajos.";
        } else {
            texto += "Todas las horas recomendadas entraron en tu semana.";
        }
        areaResumen.setText(texto);
        areaResumen.setCaretPosition(0);
    }

    private void generarHorario() {
        HorarioEstudio horario = getEstudiante().getHorario();
        int[] horas = new int[HorarioEstudio.DIAS.length];
        try {
            for (int i = 0; i < horas.length; i++) {
                horas[i] = leerEntero(camposHoras[i], "Horas del " + HorarioEstudio.DIAS_VISIBLES[i].toLowerCase(),
                        0, HorarioEstudio.MAX_HORAS_DIA);
            }
            for (int i = 0; i < horas.length; i++) {
                horario.definirDisponibilidad(HorarioEstudio.DIAS[i], horas[i]);
            }
        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());
            return;
        }
        horario.generarHorario(getEstudiante().getCursos());
        ventana.datosModificados("Horario generado: " + horario.calcularTotalHoras() + " h de estudio por semana.");
        dibujarSemana();
        escribirResumen();

        if (getEstudiante().getCursos().isEmpty()) {
            mostrarAdvertencia("Aún no tienes cursos: registra tus cursos para repartir las horas.");
        } else if (horario.calcularTotalDisponible() == 0) {
            mostrarAdvertencia("Indica al menos una hora libre en algún día para armar tu horario.");
        } else if (horario.getHorasSinAsignar() > 0) {
            mostrarAdvertencia("Horario generado con " + horario.calcularTotalHoras() + " h.\nFaltaron "
                    + horario.getHorasSinAsignar() + " h recomendadas por falta de tiempo libre.");
        } else {
            mostrarInfo("Horario generado: " + horario.calcularTotalHoras() + " h de estudio en la semana.");
        }
    }
}
