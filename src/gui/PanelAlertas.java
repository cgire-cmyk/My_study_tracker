package gui;

import analisis.Alerta;
import analisis.Alertable;
import analisis.GestorAlertas;
import analisis.PreferenciasAlertas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import nucleo.Curso;

public class PanelAlertas extends PanelBase {
    private JCheckBox checkRiesgoMedio;
    private JCheckBox checkSinRiesgo;
    private JCheckBox checkAvisarNotas;
    private JLabel etiquetaResumen;
    private JPanel panelLista;
    private JScrollPane scrollLista;

    public PanelAlertas(VentanaPrincipal ventana) {
        super(ventana, "Alertas de riesgo",
                "Cursos que necesitan atención. El riesgo ALTO siempre se muestra; el resto depende de tus preferencias.");

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setBackground(Paleta.FONDO);
        centro.add(crearPreferencias(), BorderLayout.NORTH);
        centro.add(crearListaAlertas(), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearPreferencias() {
        JPanel tarjeta = crearTarjeta("Preferencias de alertas");
        ManejadorPreferencias manejador = new ManejadorPreferencias();
        PreferenciasAlertas preferencias = ventana.getPreferencias();

        checkRiesgoMedio = crearCasilla("Incluir cursos con riesgo MEDIO (promedio actual por debajo de 10.5)",
                preferencias.isIncluirRiesgoMedio(), manejador);
        checkSinRiesgo = crearCasilla("Mostrar también los cursos que van bien (riesgo BAJO)",
                preferencias.isMostrarCursosSinRiesgo(), manejador);
        checkAvisarNotas = crearCasilla("Activar recordatorio: avisarme al registrar una nota si el curso queda en riesgo ALTO",
                preferencias.isAvisarAlRegistrarNota(), manejador);

        JPanel casillas = new JPanel(new GridLayout(3, 1, 0, 2));
        casillas.setBackground(Paleta.TARJETA);
        casillas.add(checkRiesgoMedio);
        casillas.add(checkSinRiesgo);
        casillas.add(checkAvisarNotas);
        tarjeta.add(casillas, BorderLayout.CENTER);
        return tarjeta;
    }

    private JCheckBox crearCasilla(String texto, boolean marcada, ActionListener manejador) {
        JCheckBox casilla = new JCheckBox(texto, marcada);
        casilla.setBackground(Paleta.TARJETA);
        casilla.setFont(Paleta.FUENTE_NORMAL);
        casilla.addActionListener(manejador);
        return casilla;
    }

    private JPanel crearListaAlertas() {
        JPanel tarjeta = crearTarjeta("Alertas");
        etiquetaResumen = crearEtiqueta(" ");
        panelLista = new JPanel(new GridLayout(0, 1, 0, 8));
        panelLista.setBackground(Paleta.TARJETA);

        JPanel contenido = new JPanel(new BorderLayout(0, 8));
        contenido.setBackground(Paleta.TARJETA);
        contenido.add(etiquetaResumen, BorderLayout.NORTH);
        scrollLista = crearScroll(alinearArriba(panelLista, Paleta.TARJETA));
        contenido.add(scrollLista, BorderLayout.CENTER);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    @Override
    public void actualizar() {
        GestorAlertas gestor = ventana.getGestorAlertas();
        ArrayList<Alerta> alertas = gestor.generarAlertas(getEstudiante().getCursos(), ventana.getPreferencias());

        panelLista.removeAll();
        for (Alerta alerta : alertas) {
            panelLista.add(crearFilaAlerta(alerta));
        }
        int alto = gestor.contarAlertasPorNivel(Alertable.RIESGO_ALTO);
        int medio = gestor.contarAlertasPorNivel(Alertable.RIESGO_MEDIO);

        if (getEstudiante().getCursos().isEmpty()) {
            etiquetaResumen.setText("Aún no tienes cursos registrados.");
            etiquetaResumen.setForeground(Paleta.TEXTO_SUAVE);
        } else if (alto + medio == 0) {
            etiquetaResumen.setText("¡Ningún curso en riesgo! Sigue así.");
            etiquetaResumen.setForeground(Paleta.VERDE);
        } else {
            etiquetaResumen.setText(alto + " curso(s) en riesgo ALTO y " + medio + " en riesgo MEDIO"
                    + (ventana.getPreferencias().isIncluirRiesgoMedio() ? "." : " (los MEDIO están ocultos)."));
            etiquetaResumen.setForeground(alto > 0 ? Paleta.ROJO : Paleta.NARANJA);
        }
        ajustarAlAncho(panelLista);
        redistribuir(scrollLista);
    }

    private JPanel crearFilaAlerta(Alerta alerta) {
        Curso curso = alerta.getCurso();
        Color fondo = Paleta.colorRiesgoClaro(alerta.getNivel());

        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(fondo);
        agregarMargen(fila, 8);

        JLabel sello = crearSello(alerta.getNivel(), Paleta.colorRiesgo(alerta.getNivel()));
        sello.setPreferredSize(new Dimension(80, 30));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setBackground(fondo);
        JLabel nombre = new JLabel(curso.getCodigo() + " - " + curso.getNombre());
        nombre.setFont(Paleta.FUENTE_NEGRITA);
        nombre.setForeground(Paleta.TEXTO);
        JLabel mensaje = new JLabel(alerta.getMensaje().substring(0, 1).toUpperCase()
                + alerta.getMensaje().substring(1));
        mensaje.setFont(Paleta.FUENTE_NORMAL);
        mensaje.setForeground(Paleta.TEXTO);
        textos.add(nombre);
        textos.add(mensaje);

        JButton botonVer = crearBoton("Ver curso", Paleta.PRIMARIO);
        botonVer.addActionListener(new VerCursoListener(curso));

        fila.add(sello, BorderLayout.WEST);
        fila.add(textos, BorderLayout.CENTER);
        fila.add(alinearArriba(botonVer, fondo), BorderLayout.EAST);
        return fila;
    }

    private class ManejadorPreferencias implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PreferenciasAlertas preferencias = ventana.getPreferencias();
            Object origen = e.getSource();
            if (origen == checkRiesgoMedio) {
                preferencias.setIncluirRiesgoMedio(checkRiesgoMedio.isSelected());
            } else if (origen == checkSinRiesgo) {
                preferencias.setMostrarCursosSinRiesgo(checkSinRiesgo.isSelected());
            } else if (origen == checkAvisarNotas) {
                preferencias.setAvisarAlRegistrarNota(checkAvisarNotas.isSelected());
                ventana.setEstado(checkAvisarNotas.isSelected()
                        ? "Recordatorio activado: te avisaré si una nota deja un curso en riesgo ALTO."
                        : "Recordatorio desactivado.");
            }
            actualizar();
        }
    }

    private class VerCursoListener implements ActionListener {
        private Curso curso;

        public VerCursoListener(Curso curso) {
            this.curso = curso;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ventana.mostrarCurso(curso);
        }
    }
}
