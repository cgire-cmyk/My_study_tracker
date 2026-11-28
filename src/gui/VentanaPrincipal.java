package gui;

import analisis.Alerta;
import analisis.Alertable;
import analisis.GestorAlertas;
import analisis.PreferenciasAlertas;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nucleo.Calificacion;
import nucleo.Curso;
import planificacion.ArchivoDatos;
import planificacion.Estudiante;

public class VentanaPrincipal extends JFrame {
    public static final int ANCHO = 1180;
    public static final int ALTO = 720;

    public static final String SECCION_INICIO = "INICIO";
    public static final String SECCION_CURSOS = "CURSOS";
    public static final String SECCION_METAS = "METAS";
    public static final String SECCION_HORARIO = "HORARIO";
    public static final String SECCION_ALERTAS = "ALERTAS";
    public static final String SECCION_RECOMENDACIONES = "RECOMENDACIONES";
    private static final String COMANDO_ACERCA = "ACERCA";
    private static final String COMANDO_SALIR = "SALIR";

    private Estudiante estudiante;
    private ArchivoDatos archivo;
    private PreferenciasAlertas preferencias;
    private GestorAlertas gestorAlertas;

    private JLabel etiquetaEstudiante;
    private JLabel etiquetaResumen;
    private JLabel etiquetaEstado;
    private JPanel areaTrabajo;
    private ArrayList<JButton> botonesSeccion;

    private PanelInicio panelInicio;
    private PanelCursos panelCursos;
    private PanelMetas panelMetas;
    private PanelHorario panelHorario;
    private PanelAlertas panelAlertas;
    private PanelRecomendaciones panelRecomendaciones;

    public VentanaPrincipal(Estudiante estudiante, ArchivoDatos archivo, String mensajeInicial) {
        this.estudiante = estudiante;
        this.archivo = archivo;
        this.preferencias = new PreferenciasAlertas();
        this.gestorAlertas = new GestorAlertas();
        this.botonesSeccion = new ArrayList<>();

        setTitle("MyStudyTracker - Plataforma académica");
        setSize(ANCHO, ALTO);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelInicio = new PanelInicio(this);
        panelCursos = new PanelCursos(this);
        panelMetas = new PanelMetas(this);
        panelHorario = new PanelHorario(this);
        panelAlertas = new PanelAlertas(this);
        panelRecomendaciones = new PanelRecomendaciones(this);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearMenu(), BorderLayout.WEST);
        areaTrabajo = new JPanel(new BorderLayout());
        areaTrabajo.setBackground(Paleta.FONDO);
        add(areaTrabajo, BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);

        actualizarEncabezado();
        setEstado(mensajeInicial);
        mostrarSeccion(SECCION_INICIO);
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout(20, 0));
        encabezado.setBackground(Paleta.PRIMARIO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        JPanel bloqueTitulo = new JPanel(new GridLayout(2, 1));
        bloqueTitulo.setBackground(Paleta.PRIMARIO);
        JLabel titulo = new JLabel("MyStudyTracker");
        titulo.setFont(Paleta.FUENTE_TITULO);
        titulo.setForeground(Paleta.TEXTO_CLARO);
        JLabel subtitulo = new JLabel("Plataforma académica · Chachani Dev · DSOO - EPIS UNSA");
        subtitulo.setFont(Paleta.FUENTE_PEQUENA);
        subtitulo.setForeground(Paleta.PRIMARIO_CLARO);
        bloqueTitulo.add(titulo);
        bloqueTitulo.add(subtitulo);

        JPanel bloqueEstudiante = new JPanel(new GridLayout(2, 1));
        bloqueEstudiante.setBackground(Paleta.PRIMARIO);
        etiquetaEstudiante = new JLabel("", JLabel.RIGHT);
        etiquetaEstudiante.setFont(Paleta.FUENTE_NEGRITA);
        etiquetaEstudiante.setForeground(Paleta.TEXTO_CLARO);
        etiquetaResumen = new JLabel("", JLabel.RIGHT);
        etiquetaResumen.setFont(Paleta.FUENTE_PEQUENA);
        etiquetaResumen.setForeground(Paleta.PRIMARIO_CLARO);
        bloqueEstudiante.add(etiquetaEstudiante);
        bloqueEstudiante.add(etiquetaResumen);

        encabezado.add(bloqueTitulo, BorderLayout.WEST);
        encabezado.add(bloqueEstudiante, BorderLayout.EAST);
        return encabezado;
    }

    private JPanel crearMenu() {
        ManejadorMenu manejador = new ManejadorMenu();

        JPanel botonesArriba = new JPanel(new GridLayout(0, 1, 0, 6));
        botonesArriba.setBackground(Paleta.PRIMARIO_OSCURO);
        JLabel etiquetaMenu = new JLabel("MENÚ");
        etiquetaMenu.setFont(Paleta.FUENTE_PEQUENA);
        etiquetaMenu.setForeground(Paleta.PRIMARIO_CLARO);
        botonesArriba.add(etiquetaMenu);
        botonesArriba.add(crearBotonMenu("Inicio", SECCION_INICIO, manejador));
        botonesArriba.add(crearBotonMenu("Cursos y notas", SECCION_CURSOS, manejador));
        botonesArriba.add(crearBotonMenu("Metas", SECCION_METAS, manejador));
        botonesArriba.add(crearBotonMenu("Horario", SECCION_HORARIO, manejador));
        botonesArriba.add(crearBotonMenu("Alertas", SECCION_ALERTAS, manejador));
        botonesArriba.add(crearBotonMenu("Recomendaciones", SECCION_RECOMENDACIONES, manejador));

        JPanel botonesAbajo = new JPanel(new GridLayout(2, 1, 0, 6));
        botonesAbajo.setBackground(Paleta.PRIMARIO_OSCURO);
        botonesAbajo.add(crearBotonMenu("Acerca de", COMANDO_ACERCA, manejador));
        botonesAbajo.add(crearBotonMenu("Salir", COMANDO_SALIR, manejador));

        JPanel menu = new JPanel(new BorderLayout());
        menu.setBackground(Paleta.PRIMARIO_OSCURO);
        menu.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));
        menu.add(botonesArriba, BorderLayout.NORTH);
        menu.add(botonesAbajo, BorderLayout.SOUTH);
        return menu;
    }

    private JButton crearBotonMenu(String texto, String comando, ActionListener manejador) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        boton.addActionListener(manejador);
        boton.setFont(Paleta.FUENTE_NEGRITA);
        boton.setBackground(Paleta.PRIMARIO);
        boton.setForeground(Paleta.TEXTO_CLARO);
        boton.setFocusPainted(false);
        boton.setHorizontalAlignment(JButton.LEFT);
        boton.setPreferredSize(new Dimension(170, 36));
        if (!comando.equals(COMANDO_ACERCA) && !comando.equals(COMANDO_SALIR)) {
            botonesSeccion.add(boton);
        }
        return boton;
    }

    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        barra.setBackground(Paleta.LINEA);
        etiquetaEstado = new JLabel(" ");
        etiquetaEstado.setFont(Paleta.FUENTE_PEQUENA);
        etiquetaEstado.setForeground(Paleta.TEXTO);
        barra.add(etiquetaEstado);
        return barra;
    }

    public void mostrarSeccion(String seccion) {
        PanelBase panel = buscarPanel(seccion);
        areaTrabajo.removeAll();
        panel.actualizar();
        areaTrabajo.add(panel, BorderLayout.CENTER);
        areaTrabajo.validate();
        areaTrabajo.repaint();
        resaltarBoton(seccion);
    }

    public void mostrarCurso(Curso curso) {
        panelCursos.seleccionarCurso(curso);
        mostrarSeccion(SECCION_CURSOS);
    }

    private PanelBase buscarPanel(String seccion) {
        if (seccion.equals(SECCION_CURSOS)) {
            return panelCursos;
        } else if (seccion.equals(SECCION_METAS)) {
            return panelMetas;
        } else if (seccion.equals(SECCION_HORARIO)) {
            return panelHorario;
        } else if (seccion.equals(SECCION_ALERTAS)) {
            return panelAlertas;
        } else if (seccion.equals(SECCION_RECOMENDACIONES)) {
            return panelRecomendaciones;
        }
        return panelInicio;
    }

    private void resaltarBoton(String seccion) {
        for (JButton boton : botonesSeccion) {
            if (boton.getActionCommand().equals(seccion)) {
                boton.setBackground(Paleta.ACENTO);
                boton.setForeground(Paleta.PRIMARIO_OSCURO);
            } else {
                boton.setBackground(Paleta.PRIMARIO);
                boton.setForeground(Paleta.TEXTO_CLARO);
            }
        }
    }

    public void datosModificados(String mensaje) {
        actualizarEncabezado();
        try {
            archivo.guardar(estudiante);
            setEstado(mensaje + "  ·  Guardado automático en " + archivo.getNombre());
        } catch (IOException ex) {
            setEstado(mensaje + "  ·  ¡No se pudo guardar!");
            JOptionPane.showMessageDialog(this, "No se pudo guardar en " + archivo.getRuta()
                    + "\nDetalle: " + ex.getMessage()
                    + "\nTus cambios siguen en pantalla, pero se perderán al cerrar.",
                    "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEncabezado() {
        etiquetaEstudiante.setText(estudiante.getNombres() + "  (" + estudiante.getCodigo() + ")  ·  "
                + estudiante.getCarrera());
        int enRiesgo = estudiante.contarCursosEnRiesgo();
        etiquetaResumen.setText("Promedio ponderado actual: "
                + Calificacion.formatear(estudiante.calcularPromedioPonderadoActual())
                + "   ·   " + estudiante.calcularTotalCreditos() + " créditos"
                + "   ·   " + enRiesgo + (enRiesgo == 1 ? " curso en riesgo" : " cursos en riesgo"));
    }

    public void setEstado(String mensaje) {
        etiquetaEstado.setText(mensaje);
    }

    public void mostrarRecordatorioInicial() {
        ArrayList<Alerta> alertas = gestorAlertas.generarAlertas(estudiante.getCursos());
        String cursosAlto = "";
        for (Alerta alerta : alertas) {
            if (alerta.getNivel().equals(Alertable.RIESGO_ALTO)) {
                cursosAlto += "\n  • " + alerta.getCurso().getNombre();
            }
        }
        if (!cursosAlto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Recordatorio: estos cursos están en riesgo ALTO:" + cursosAlto
                    + "\n\nRevisa la sección «Alertas» y prioriza su estudio en tu horario.",
                    "Cursos en riesgo", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public PreferenciasAlertas getPreferencias() {
        return preferencias;
    }

    public GestorAlertas getGestorAlertas() {
        return gestorAlertas;
    }

    private class ManejadorMenu implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String comando = e.getActionCommand();
            if (comando.equals(COMANDO_SALIR)) {
                int respuesta = JOptionPane.showConfirmDialog(VentanaPrincipal.this,
                        "¿Deseas salir de MyStudyTracker?\nTus datos ya están guardados.",
                        "Salir", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else if (comando.equals(COMANDO_ACERCA)) {
                JOptionPane.showMessageDialog(VentanaPrincipal.this,
                        "MyStudyTracker - 3.er avance (interfaz gráfica con Swing)\n"
                        + "Desarrollo de Software Orientado a Objetos - EPIS UNSA\n\n"
                        + "Equipo Chachani Dev:\n"
                        + "  • José Juan Choquemamani Calisaya - Núcleo académico\n"
                        + "  • Arnold Marco Antonio Choque Mendoza - Metas, horario y archivo\n"
                        + "  • Cristhian Hernán Gire Maita - Alertas, recomendaciones y ventana\n\n"
                        + "Los datos de prueba son ficticios.",
                        "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarSeccion(comando);
            }
        }
    }
}
