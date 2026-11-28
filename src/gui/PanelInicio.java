package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.DatoInvalidoException;
import nucleo.Evaluacion;
import planificacion.Estudiante;

public class PanelInicio extends PanelBase {
    private JTextField campoNombres;
    private JTextField campoCodigo;
    private JTextField campoCarrera;
    private JButton botonGuardarDatos;

    private JLabel valorCursos;
    private JLabel valorCreditos;
    private JLabel valorPromedio;
    private JLabel valorRiesgo;

    private JPanel panelTarjetas;
    private JScrollPane scrollTarjetas;

    public PanelInicio(VentanaPrincipal ventana) {
        super(ventana, "Inicio", "Resumen de tu semestre: cómo va cada curso y qué necesitas para aprobar.");

        JPanel superior = new JPanel(new BorderLayout(0, 12));
        superior.setBackground(Paleta.FONDO);
        superior.add(crearTarjetaDatos(), BorderLayout.NORTH);
        superior.add(crearIndicadores(), BorderLayout.SOUTH);

        panelTarjetas = new JPanel(new GridLayout(0, 2, 12, 12));
        panelTarjetas.setBackground(Paleta.FONDO);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setBackground(Paleta.FONDO);
        centro.add(superior, BorderLayout.NORTH);
        scrollTarjetas = crearScroll(alinearArriba(panelTarjetas, Paleta.FONDO));
        centro.add(scrollTarjetas, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaDatos() {
        JPanel tarjeta = crearTarjeta(null);
        JPanel formulario = new JPanel(new GridLayout(2, 4, 10, 4));
        formulario.setBackground(Paleta.TARJETA);

        campoNombres = crearCampo(18);
        campoCodigo = crearCampo(10);
        campoCarrera = crearCampo(18);
        botonGuardarDatos = crearBoton("Actualizar mis datos", Paleta.PRIMARIO);

        formulario.add(crearEtiqueta("Nombres y apellidos"));
        formulario.add(crearEtiqueta("Código"));
        formulario.add(crearEtiqueta("Carrera"));
        formulario.add(new JLabel(""));
        formulario.add(campoNombres);
        formulario.add(campoCodigo);
        formulario.add(campoCarrera);
        formulario.add(botonGuardarDatos);
        tarjeta.add(formulario, BorderLayout.CENTER);

        botonGuardarDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatosEstudiante();
            }
        });
        return tarjeta;
    }

    private JPanel crearIndicadores() {
        JPanel fila = new JPanel(new GridLayout(1, 4, 12, 0));
        fila.setBackground(Paleta.FONDO);
        valorCursos = new JLabel();
        valorCreditos = new JLabel();
        valorPromedio = new JLabel();
        valorRiesgo = new JLabel();
        fila.add(crearIndicador("Cursos matriculados", valorCursos));
        fila.add(crearIndicador("Créditos", valorCreditos));
        fila.add(crearIndicador("Promedio ponderado actual", valorPromedio));
        fila.add(crearIndicador("Cursos en riesgo", valorRiesgo));
        return fila;
    }

    private JPanel crearIndicador(String texto, JLabel valor) {
        JPanel indicador = new JPanel(new GridLayout(2, 1));
        indicador.setBackground(Paleta.TARJETA);
        agregarMargen(indicador, 10);
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Paleta.FUENTE_PEQUENA);
        etiqueta.setForeground(Paleta.TEXTO_SUAVE);
        valor.setFont(Paleta.FUENTE_DATO);
        valor.setForeground(Paleta.PRIMARIO);
        indicador.add(etiqueta);
        indicador.add(valor);
        return indicador;
    }

    @Override
    public void actualizar() {
        Estudiante estudiante = getEstudiante();
        campoNombres.setText(estudiante.getNombres());
        campoCodigo.setText(estudiante.getCodigo());
        campoCarrera.setText(estudiante.getCarrera());

        int enRiesgo = estudiante.contarCursosEnRiesgo();
        valorCursos.setText(String.valueOf(estudiante.getCursos().size()));
        valorCreditos.setText(String.valueOf(estudiante.calcularTotalCreditos()));
        valorPromedio.setText(Calificacion.formatear(estudiante.calcularPromedioPonderadoActual()));
        valorRiesgo.setText(String.valueOf(enRiesgo));
        valorRiesgo.setForeground(enRiesgo > 0 ? Paleta.ROJO : Paleta.VERDE);

        panelTarjetas.removeAll();
        if (estudiante.getCursos().isEmpty()) {
            panelTarjetas.add(crearMensajeSinCursos());
        } else {
            for (Curso curso : estudiante.getCursos()) {
                panelTarjetas.add(crearTarjetaCurso(curso));
            }
        }
        ajustarAlAncho(panelTarjetas);
        redistribuir(scrollTarjetas);
    }

    private JPanel crearMensajeSinCursos() {
        JPanel tarjeta = crearTarjeta("Aún no tienes cursos");
        tarjeta.add(crearEtiqueta("Registra tu primer curso con sus evaluaciones para ver tu avance."),
                BorderLayout.CENTER);
        JButton boton = crearBoton("Registrar mi primer curso", Paleta.ACENTO);
        boton.setForeground(Paleta.PRIMARIO_OSCURO);
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.mostrarSeccion(VentanaPrincipal.SECCION_CURSOS);
            }
        });
        tarjeta.add(boton, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearTarjetaCurso(Curso curso) {
        String nivel = curso.obtenerNivelRiesgo();
        Color fondo = Paleta.colorRiesgoClaro(nivel);

        JPanel tarjeta = new JPanel(new BorderLayout(8, 8));
        tarjeta.setBackground(fondo);
        agregarMargen(tarjeta, 12);

        JLabel nombre = new JLabel(curso.getCodigo() + " · " + curso.getNombre());
        nombre.setFont(Paleta.FUENTE_SUBTITULO);
        nombre.setForeground(Paleta.TEXTO);
        nombre.setToolTipText(curso.getNombre());

        JButton botonDetalle = crearBoton("Ver detalle", Paleta.PRIMARIO);
        botonDetalle.addActionListener(new VerCursoListener(curso));
        JPanel derecha = new JPanel(new GridLayout(2, 1, 0, 6));
        derecha.setBackground(fondo);
        derecha.add(crearSello("Riesgo " + nivel, Paleta.colorRiesgo(nivel)));
        derecha.add(botonDetalle);

        JPanel datos = new JPanel(new GridLayout(0, 2, 6, 2));
        datos.setBackground(fondo);
        agregarDato(datos, "Créditos", String.valueOf(curso.getCreditos()));
        agregarDato(datos, "Avance rendido", Evaluacion.formatearPeso(curso.calcularPesoRendido()) + " %");
        agregarDato(datos, "Promedio actual",
                curso.tieneNotas() ? Calificacion.formatear(curso.calcularPromedioActual()) : "sin notas");
        agregarDato(datos, "Para aprobar", describirNotaNecesaria(curso));

        tarjeta.add(nombre, BorderLayout.NORTH);
        tarjeta.add(datos, BorderLayout.CENTER);
        tarjeta.add(alinearArriba(derecha, fondo), BorderLayout.EAST);
        return tarjeta;
    }

    private void agregarDato(JPanel panel, String etiqueta, String valor) {
        JLabel e = new JLabel(etiqueta);
        e.setFont(Paleta.FUENTE_PEQUENA);
        e.setForeground(Paleta.TEXTO_SUAVE);
        JLabel v = new JLabel(valor);
        v.setFont(Paleta.FUENTE_NEGRITA);
        v.setForeground(Paleta.TEXTO);
        panel.add(e);
        panel.add(v);
    }

    public static String describirNotaNecesaria(Curso curso) {
        double necesaria = curso.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        if (necesaria > Calificacion.NOTA_MAXIMA) {
            return "ya no es posible";
        }
        if (necesaria == 0) {
            return "¡asegurado!";
        }
        return Calificacion.formatear(necesaria) + " en lo pendiente";
    }

    private void guardarDatosEstudiante() {
        try {
            String nombres = leerTexto(campoNombres, "Nombres y apellidos");
            String codigo = leerTexto(campoCodigo, "Código");
            getEstudiante().actualizarDatos(codigo, nombres, campoCarrera.getText());
            ventana.datosModificados("Datos del estudiante actualizados.");
            actualizar();
            mostrarInfo("Tus datos se actualizaron correctamente.");
        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());
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
