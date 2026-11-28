package gui;

import analisis.Alerta;
import analisis.Alertable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.DatoInvalidoException;
import nucleo.Evaluacion;
import nucleo.ExamenParcial;
import nucleo.PracticaCalificada;
import nucleo.Trabajo;

public class PanelCursos extends PanelBase {
    private static final String[] TIPOS_EVALUACION = {"Práctica calificada", "Trabajo", "Examen"};
    private static final int TIPO_PRACTICA = 0;
    private static final int TIPO_TRABAJO = 1;
    private static final String[] COLUMNAS = {"Código", "Evaluación", "Tipo", "Peso", "Nota", "Aporte"};

    private Curso cursoSeleccionado;
    private boolean llenandoCombos;

    private JComboBox<Curso> comboCursos;
    private JButton botonEliminarCurso;

    private JTextField campoCodigoCurso;
    private JTextField campoNombreCurso;
    private JComboBox<Integer> comboCreditos;
    private JButton botonRegistrarCurso;

    private JComboBox<String> comboTipo;
    private JTextField campoCodigoEvaluacion;
    private JTextField campoNombreEvaluacion;
    private JTextField campoPeso;
    private JLabel etiquetaPeso;
    private JLabel etiquetaDatoExtra;
    private JPanel celdaDatoExtra;
    private JTextField campoNumero;
    private JCheckBox checkGrupal;
    private JButton botonAgregarEvaluacion;

    private JComboBox<Evaluacion> comboEvaluaciones;
    private JTextField campoNota;
    private JTextField campoObservacion;
    private JButton botonGuardarNota;
    private JButton botonEliminarEvaluacion;

    private JLabel etiquetaDetalle;
    private JPanel contenedorTabla;
    private JPanel panelTabla;
    private JScrollPane scrollTabla;
    private JLabel tituloEvaluacion;
    private JLabel resumenPesos;
    private JLabel resumenPromedio;
    private JLabel resumenNecesaria;
    private JLabel resumenRiesgo;

    public PanelCursos(VentanaPrincipal ventana) {
        super(ventana, "Cursos y notas",
                "Registra tus cursos, sus evaluaciones con su peso (deben sumar 100 %) y las notas que obtienes.");

        ManejadorBotones manejador = new ManejadorBotones();

        JPanel formularios = new JPanel(new GridLayout(1, 3, 12, 0));
        formularios.setBackground(Paleta.FONDO);
        formularios.add(crearFormularioCurso(manejador));
        formularios.add(crearFormularioEvaluacion(manejador));
        formularios.add(crearFormularioNota(manejador));

        JPanel superior = new JPanel(new BorderLayout(0, 10));
        superior.setBackground(Paleta.FONDO);
        superior.add(crearSelectorCurso(manejador), BorderLayout.NORTH);
        superior.add(formularios, BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setBackground(Paleta.FONDO);
        centro.add(superior, BorderLayout.NORTH);
        centro.add(crearDetalle(), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearSelectorCurso(ActionListener manejador) {
        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        selector.setBackground(Paleta.FONDO);
        JLabel etiqueta = new JLabel("Curso seleccionado:");
        etiqueta.setFont(Paleta.FUENTE_NEGRITA);
        etiqueta.setForeground(Paleta.PRIMARIO);

        comboCursos = new JComboBox<>();
        comboCursos.setFont(Paleta.FUENTE_NORMAL);
        comboCursos.setPreferredSize(new Dimension(380, 28));
        comboCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!llenandoCombos) {
                    cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
                    mostrarCursoSeleccionado();
                }
            }
        });

        botonEliminarCurso = crearBoton("Eliminar curso", Paleta.ROJO);
        botonEliminarCurso.addActionListener(manejador);

        selector.add(etiqueta);
        selector.add(comboCursos);
        selector.add(botonEliminarCurso);
        return selector;
    }

    private JPanel crearFormularioCurso(ActionListener manejador) {
        JPanel tarjeta = crearTarjeta("1. Nuevo curso");
        JPanel campos = new JPanel(new GridLayout(3, 2, 6, 6));
        campos.setBackground(Paleta.TARJETA);

        campoCodigoCurso = crearCampo(8);
        campoNombreCurso = crearCampo(14);
        comboCreditos = new JComboBox<>();
        for (int c = Curso.CREDITOS_MINIMOS; c <= Curso.CREDITOS_MAXIMOS; c++) {
            comboCreditos.addItem(c);
        }
        comboCreditos.setSelectedItem(Curso.CREDITOS_POR_DEFECTO);

        campos.add(crearEtiqueta("Código"));
        campos.add(campoCodigoCurso);
        campos.add(crearEtiqueta("Nombre"));
        campos.add(campoNombreCurso);
        campos.add(crearEtiqueta("Créditos"));
        campos.add(comboCreditos);

        botonRegistrarCurso = crearBoton("Registrar curso", Paleta.PRIMARIO);
        botonRegistrarCurso.addActionListener(manejador);

        tarjeta.add(alinearArriba(campos, Paleta.TARJETA), BorderLayout.CENTER);
        tarjeta.add(crearFilaBotones(botonRegistrarCurso, null), BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearFormularioEvaluacion(ActionListener manejador) {
        JPanel tarjeta = crearTarjeta(null);
        tituloEvaluacion = new JLabel("2. Nueva evaluación");
        tituloEvaluacion.setFont(Paleta.FUENTE_SUBTITULO);
        tituloEvaluacion.setForeground(Paleta.PRIMARIO);
        tarjeta.add(tituloEvaluacion, BorderLayout.NORTH);
        JPanel campos = new JPanel(new GridLayout(5, 2, 6, 6));
        campos.setBackground(Paleta.TARJETA);

        comboTipo = new JComboBox<>(TIPOS_EVALUACION);
        campoCodigoEvaluacion = crearCampo(6);
        campoNombreEvaluacion = crearCampo(12);
        campoPeso = crearCampo(5);
        etiquetaPeso = crearEtiqueta("Peso (%)");
        etiquetaDatoExtra = crearEtiqueta("N.º de práctica");
        campoNumero = crearCampo(4);
        campoNumero.setText("1");
        checkGrupal = new JCheckBox("Es grupal");
        checkGrupal.setBackground(Paleta.TARJETA);
        checkGrupal.setFont(Paleta.FUENTE_NORMAL);
        celdaDatoExtra = new JPanel(new BorderLayout());
        celdaDatoExtra.setBackground(Paleta.TARJETA);
        celdaDatoExtra.add(campoNumero, BorderLayout.CENTER);

        comboTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarDatoExtra();
            }
        });

        campos.add(crearEtiqueta("Tipo"));
        campos.add(comboTipo);
        campos.add(crearEtiqueta("Código"));
        campos.add(campoCodigoEvaluacion);
        campos.add(crearEtiqueta("Nombre"));
        campos.add(campoNombreEvaluacion);
        campos.add(etiquetaPeso);
        campos.add(campoPeso);
        campos.add(etiquetaDatoExtra);
        campos.add(celdaDatoExtra);

        botonAgregarEvaluacion = crearBoton("Agregar evaluación", Paleta.PRIMARIO);
        botonAgregarEvaluacion.addActionListener(manejador);

        tarjeta.add(alinearArriba(campos, Paleta.TARJETA), BorderLayout.CENTER);
        tarjeta.add(crearFilaBotones(botonAgregarEvaluacion, null), BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearFormularioNota(ActionListener manejador) {
        JPanel tarjeta = crearTarjeta("3. Registrar nota");
        JPanel campos = new JPanel(new GridLayout(3, 2, 6, 6));
        campos.setBackground(Paleta.TARJETA);

        comboEvaluaciones = new JComboBox<>();
        campoNota = crearCampo(5);
        campoNota.addActionListener(manejador);
        campoObservacion = crearCampo(12);

        comboEvaluaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!llenandoCombos) {
                    mostrarNotaDeEvaluacion();
                }
            }
        });

        campos.add(crearEtiqueta("Evaluación"));
        campos.add(comboEvaluaciones);
        campos.add(crearEtiqueta("Nota (0 - 20)"));
        campos.add(campoNota);
        campos.add(crearEtiqueta("Observación"));
        campos.add(campoObservacion);

        botonGuardarNota = crearBoton("Guardar nota", Paleta.VERDE);
        botonGuardarNota.addActionListener(manejador);
        botonEliminarEvaluacion = crearBoton("Eliminar", Paleta.ROJO);
        botonEliminarEvaluacion.setToolTipText("Elimina la evaluación elegida y su nota");
        botonEliminarEvaluacion.addActionListener(manejador);

        tarjeta.add(alinearArriba(campos, Paleta.TARJETA), BorderLayout.CENTER);
        tarjeta.add(crearFilaBotones(botonGuardarNota, botonEliminarEvaluacion), BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearFilaBotones(JButton principal, JButton secundario) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        fila.setBackground(Paleta.TARJETA);
        if (secundario != null) {
            fila.add(secundario);
        }
        fila.add(principal);
        return fila;
    }

    private JPanel crearDetalle() {
        JPanel tarjeta = crearTarjeta(null);
        etiquetaDetalle = new JLabel("Detalle del curso");
        etiquetaDetalle.setFont(Paleta.FUENTE_SUBTITULO);
        etiquetaDetalle.setForeground(Paleta.PRIMARIO);

        panelTabla = new JPanel(new GridLayout(0, COLUMNAS.length, 1, 1));
        panelTabla.setBackground(Paleta.LINEA);
        contenedorTabla = new JPanel(new BorderLayout());
        contenedorTabla.setBackground(Paleta.TARJETA);

        JPanel resumen = new JPanel(new GridLayout(1, 4, 8, 0));
        resumen.setBackground(Paleta.TARJETA);
        resumenPesos = crearChip();
        resumenPromedio = crearChip();
        resumenNecesaria = crearChip();
        resumenRiesgo = crearChip();
        resumen.add(resumenPesos);
        resumen.add(resumenPromedio);
        resumen.add(resumenNecesaria);
        resumen.add(resumenRiesgo);

        tarjeta.add(etiquetaDetalle, BorderLayout.NORTH);
        scrollTabla = crearScroll(contenedorTabla);
        tarjeta.add(scrollTabla, BorderLayout.CENTER);
        tarjeta.add(resumen, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JLabel crearChip() {
        JLabel chip = new JLabel(" ", JLabel.CENTER);
        chip.setOpaque(true);
        chip.setFont(Paleta.FUENTE_NEGRITA);
        agregarMargen(chip, 7);
        return chip;
    }

    private void pintarChip(JLabel chip, String texto, Color fondo, Color letra) {
        chip.setText(texto);
        chip.setBackground(fondo);
        chip.setForeground(letra);
    }

    @Override
    public void actualizar() {
        llenarComboCursos();
        mostrarCursoSeleccionado();
    }

    public void seleccionarCurso(Curso curso) {
        this.cursoSeleccionado = curso;
    }

    private void llenarComboCursos() {
        llenandoCombos = true;
        comboCursos.removeAllItems();
        for (Curso curso : getEstudiante().getCursos()) {
            comboCursos.addItem(curso);
        }
        if (cursoSeleccionado == null || !getEstudiante().getCursos().contains(cursoSeleccionado)) {
            cursoSeleccionado = getEstudiante().getCursos().isEmpty() ? null : getEstudiante().getCursos().get(0);
        }
        comboCursos.setSelectedItem(cursoSeleccionado);
        llenandoCombos = false;
    }

    private void mostrarCursoSeleccionado() {
        llenarComboEvaluaciones();
        actualizarFormularios();
        actualizarDetalle();
    }

    private void llenarComboEvaluaciones() {
        Evaluacion anterior = (Evaluacion) comboEvaluaciones.getSelectedItem();
        llenandoCombos = true;
        comboEvaluaciones.removeAllItems();
        if (cursoSeleccionado != null) {
            for (Evaluacion e : cursoSeleccionado.getEvaluaciones()) {
                comboEvaluaciones.addItem(e);
            }
            if (anterior != null && cursoSeleccionado.getEvaluaciones().contains(anterior)) {
                comboEvaluaciones.setSelectedItem(anterior);
            }
        }
        llenandoCombos = false;
        mostrarNotaDeEvaluacion();
    }

    private void mostrarNotaDeEvaluacion() {
        Evaluacion e = (Evaluacion) comboEvaluaciones.getSelectedItem();
        if (e != null && e.estaRendida()) {
            campoNota.setText(String.valueOf(e.getNota()));
            campoObservacion.setText(e.getCalificacion().getObservacion());
        } else {
            campoNota.setText("");
            campoObservacion.setText("");
        }
    }

    private void actualizarFormularios() {
        boolean hayCurso = cursoSeleccionado != null;
        boolean hayEvaluaciones = hayCurso && !cursoSeleccionado.getEvaluaciones().isEmpty();

        botonEliminarCurso.setEnabled(hayCurso);
        comboCursos.setEnabled(hayCurso);
        comboTipo.setEnabled(hayCurso);
        campoCodigoEvaluacion.setEnabled(hayCurso);
        campoNombreEvaluacion.setEnabled(hayCurso);
        campoPeso.setEnabled(hayCurso);
        campoNumero.setEnabled(hayCurso);
        checkGrupal.setEnabled(hayCurso);
        botonAgregarEvaluacion.setEnabled(hayCurso);

        comboEvaluaciones.setEnabled(hayEvaluaciones);
        campoNota.setEnabled(hayEvaluaciones);
        campoObservacion.setEnabled(hayEvaluaciones);
        botonGuardarNota.setEnabled(hayEvaluaciones);
        botonEliminarEvaluacion.setEnabled(hayEvaluaciones);

        if (hayCurso) {
            tituloEvaluacion.setText("2. Evaluación · libre "
                    + Evaluacion.formatearPeso(cursoSeleccionado.calcularPesoDisponible()) + " %");
        } else {
            tituloEvaluacion.setText("2. Nueva evaluación");
        }
    }

    private void cambiarDatoExtra() {
        int tipo = comboTipo.getSelectedIndex();
        celdaDatoExtra.removeAll();
        if (tipo == TIPO_TRABAJO) {
            etiquetaDatoExtra.setText("Modalidad");
            celdaDatoExtra.add(checkGrupal, BorderLayout.CENTER);
        } else {
            etiquetaDatoExtra.setText(tipo == TIPO_PRACTICA ? "N.º de práctica" : "Unidad");
            celdaDatoExtra.add(campoNumero, BorderLayout.CENTER);
        }
        celdaDatoExtra.validate();
        celdaDatoExtra.repaint();
    }

    private void actualizarDetalle() {
        contenedorTabla.removeAll();
        Curso curso = cursoSeleccionado;
        if (curso == null) {
            etiquetaDetalle.setText("Detalle del curso");
            contenedorTabla.add(crearMensaje("Aún no tienes cursos. Registra el primero en el formulario 1."),
                    BorderLayout.NORTH);
            pintarChip(resumenPesos, "Pesos: -", Paleta.GRIS_CLARO, Paleta.TEXTO_SUAVE);
            pintarChip(resumenPromedio, "Promedio: -", Paleta.GRIS_CLARO, Paleta.TEXTO_SUAVE);
            pintarChip(resumenNecesaria, "Para aprobar: -", Paleta.GRIS_CLARO, Paleta.TEXTO_SUAVE);
            pintarChip(resumenRiesgo, "Riesgo: -", Paleta.GRIS_CLARO, Paleta.TEXTO_SUAVE);
        } else {
            etiquetaDetalle.setText("Evaluaciones de " + curso + "  ·  " + curso.getCreditos() + " créditos");
            if (curso.getEvaluaciones().isEmpty()) {
                contenedorTabla.add(crearMensaje("Este curso aún no tiene evaluaciones. Agrégalas en el formulario 2."),
                        BorderLayout.NORTH);
            } else {
                construirTabla(curso);
                contenedorTabla.add(panelTabla, BorderLayout.NORTH);
            }
            actualizarResumen(curso);
        }
        ajustarAlAncho(contenedorTabla);
        redistribuir(scrollTabla);
    }

    private JLabel crearMensaje(String texto) {
        JLabel mensaje = crearEtiqueta(texto);
        mensaje.setForeground(Paleta.TEXTO_SUAVE);
        agregarMargen(mensaje, 10);
        return mensaje;
    }

    private void construirTabla(Curso curso) {
        panelTabla.removeAll();
        for (String columna : COLUMNAS) {
            panelTabla.add(crearCelda(columna, Paleta.PRIMARIO, Paleta.TEXTO_CLARO, Paleta.FUENTE_NEGRITA));
        }
        int fila = 0;
        for (Evaluacion e : curso.getEvaluaciones()) {
            Color fondo = (fila % 2 == 0) ? Paleta.TARJETA : Paleta.FILA_ALTERNA;
            panelTabla.add(crearCelda(e.getCodigo(), fondo, Paleta.TEXTO, Paleta.FUENTE_NEGRITA));
            panelTabla.add(crearCelda(e.getNombre(), fondo, Paleta.TEXTO, Paleta.FUENTE_NORMAL));
            panelTabla.add(crearCelda(e.getTipo(), fondo, Paleta.TEXTO, Paleta.FUENTE_NORMAL));
            panelTabla.add(crearCelda(Evaluacion.formatearPeso(e.getPeso()) + " %", fondo, Paleta.TEXTO,
                    Paleta.FUENTE_NORMAL));
            if (e.estaRendida()) {
                boolean aprobada = e.getCalificacion().estaAprobada();
                JLabel celdaNota = crearCelda(Calificacion.formatear(e.getNota()),
                        aprobada ? Paleta.VERDE_CLARO : Paleta.ROJO_CLARO,
                        aprobada ? Paleta.VERDE : Paleta.ROJO, Paleta.FUENTE_NEGRITA);
                if (!e.getCalificacion().getObservacion().isEmpty()) {
                    celdaNota.setToolTipText("Observación: " + e.getCalificacion().getObservacion());
                }
                panelTabla.add(celdaNota);
                panelTabla.add(crearCelda(Calificacion.formatear(e.calcularAporte()) + " pts", fondo,
                        Paleta.TEXTO, Paleta.FUENTE_NORMAL));
            } else {
                panelTabla.add(crearCelda("pendiente", fondo, Paleta.TEXTO_SUAVE, Paleta.FUENTE_NORMAL));
                panelTabla.add(crearCelda("-", fondo, Paleta.TEXTO_SUAVE, Paleta.FUENTE_NORMAL));
            }
            fila++;
        }
        if (!curso.pesosCompletos()) {
            Font cursiva = new Font("SansSerif", Font.ITALIC, 13);
            panelTabla.add(crearCelda("-", Paleta.NARANJA_CLARO, Paleta.NARANJA, cursiva));
            panelTabla.add(crearCelda("Sin registrar", Paleta.NARANJA_CLARO, Paleta.NARANJA, cursiva));
            panelTabla.add(crearCelda("-", Paleta.NARANJA_CLARO, Paleta.NARANJA, cursiva));
            panelTabla.add(crearCelda(Evaluacion.formatearPeso(curso.calcularPesoDisponible()) + " %",
                    Paleta.NARANJA_CLARO, Paleta.NARANJA, cursiva));
            panelTabla.add(crearCelda("pendiente", Paleta.NARANJA_CLARO, Paleta.NARANJA, cursiva));
            panelTabla.add(crearCelda("-", Paleta.NARANJA_CLARO, Paleta.NARANJA, cursiva));
        }
        panelTabla.add(crearCelda("", Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO, Paleta.FUENTE_NEGRITA));
        panelTabla.add(crearCelda("Puntaje acumulado", Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO, Paleta.FUENTE_NEGRITA));
        panelTabla.add(crearCelda("", Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO, Paleta.FUENTE_NEGRITA));
        panelTabla.add(crearCelda(Evaluacion.formatearPeso(curso.calcularSumaPesos()) + " %",
                Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO, Paleta.FUENTE_NEGRITA));
        panelTabla.add(crearCelda("", Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO, Paleta.FUENTE_NEGRITA));
        panelTabla.add(crearCelda(Calificacion.formatear(curso.calcularPuntajeAcumulado()) + " pts",
                Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO, Paleta.FUENTE_NEGRITA));
    }

    private JLabel crearCelda(String texto, Color fondo, Color letra, Font fuente) {
        JLabel celda = new JLabel(texto);
        celda.setOpaque(true);
        celda.setBackground(fondo);
        celda.setForeground(letra);
        celda.setFont(fuente);
        agregarMargen(celda, 5);
        return celda;
    }

    private void actualizarResumen(Curso curso) {
        String pesos = "Pesos: " + Evaluacion.formatearPeso(curso.calcularSumaPesos()) + " % de 100 %";
        if (curso.pesosCompletos()) {
            pintarChip(resumenPesos, pesos, Paleta.VERDE_CLARO, Paleta.VERDE);
        } else {
            pintarChip(resumenPesos, pesos, Paleta.NARANJA_CLARO, Paleta.NARANJA);
        }
        String promedio = curso.tieneNotas() ? Calificacion.formatear(curso.calcularPromedioActual()) : "sin notas";
        pintarChip(resumenPromedio, "Promedio actual: " + promedio, Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO);
        double necesaria = curso.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        String textoNecesaria = (necesaria > Calificacion.NOTA_MAXIMA) ? "Ya no puede aprobar"
                : (necesaria == 0) ? "Aprobado asegurado" : "Para aprobar: " + Calificacion.formatear(necesaria);
        pintarChip(resumenNecesaria, textoNecesaria, Paleta.PRIMARIO_CLARO, Paleta.PRIMARIO);
        String nivel = curso.obtenerNivelRiesgo();
        pintarChip(resumenRiesgo, "Riesgo " + nivel, Paleta.colorRiesgo(nivel), Paleta.TEXTO_CLARO);
    }

    private void registrarCurso() {
        try {
            String codigo = leerTexto(campoCodigoCurso, "Código del curso");
            String nombre = leerTexto(campoNombreCurso, "Nombre del curso");
            int creditos = (Integer) comboCreditos.getSelectedItem();
            Curso curso = new Curso(codigo, nombre, creditos);
            getEstudiante().agregarCurso(curso);

            cursoSeleccionado = curso;
            campoCodigoCurso.setText("");
            campoNombreCurso.setText("");
            comboCreditos.setSelectedItem(Curso.CREDITOS_POR_DEFECTO);
            ventana.datosModificados("Curso " + curso.getCodigo() + " registrado.");
            actualizar();
            mostrarInfo("Curso «" + curso + "» registrado.\n"
                    + "Ahora agrega sus evaluaciones en el formulario 2 (los pesos deben sumar 100 %).");
            campoCodigoEvaluacion.requestFocus();
        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarCurso() {
        if (cursoSeleccionado == null) {
            return;
        }
        String pregunta = "¿Eliminar el curso «" + cursoSeleccionado + "» con sus "
                + cursoSeleccionado.getEvaluaciones().size() + " evaluaciones?\n"
                + "También se borrarán las metas de ese curso. Esta acción no se puede deshacer.";
        if (confirmar(pregunta)) {
            String codigo = cursoSeleccionado.getCodigo();
            getEstudiante().eliminarCurso(codigo);
            cursoSeleccionado = null;
            ventana.datosModificados("Curso " + codigo + " eliminado.");
            actualizar();
        }
    }

    private void agregarEvaluacion() {
        if (cursoSeleccionado == null) {
            mostrarAdvertencia("Primero registra o elige un curso.");
            return;
        }
        try {
            String codigo = leerTexto(campoCodigoEvaluacion, "Código de la evaluación");
            String nombre = leerTexto(campoNombreEvaluacion, "Nombre de la evaluación");
            double peso = leerDecimal(campoPeso, "Peso", 1, Curso.PESO_TOTAL);
            Evaluacion evaluacion = crearEvaluacion(codigo, nombre, peso);
            cursoSeleccionado.agregarEvaluacion(evaluacion);

            campoCodigoEvaluacion.setText("");
            campoNombreEvaluacion.setText("");
            campoPeso.setText("");
            checkGrupal.setSelected(false);
            ventana.datosModificados("Evaluación " + evaluacion.getCodigo() + " agregada a "
                    + cursoSeleccionado.getCodigo() + ".");
            mostrarCursoSeleccionado();
            comboEvaluaciones.setSelectedItem(evaluacion);

            String mensaje = "Evaluación «" + evaluacion + "» agregada a " + cursoSeleccionado.getNombre() + ".";
            if (cursoSeleccionado.pesosCompletos()) {
                mensaje += "\n¡Listo! Los pesos del curso suman 100 %.";
            } else {
                mensaje += "\nAún falta asignar "
                        + Evaluacion.formatearPeso(cursoSeleccionado.calcularPesoDisponible()) + " % del curso.";
            }
            mostrarInfo(mensaje);
        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private Evaluacion crearEvaluacion(String codigo, String nombre, double peso) throws DatoInvalidoException {
        int tipo = comboTipo.getSelectedIndex();
        if (tipo == TIPO_TRABAJO) {
            return new Trabajo(codigo, nombre, peso, checkGrupal.isSelected());
        }
        if (tipo == TIPO_PRACTICA) {
            int numero = leerEntero(campoNumero, "N.º de práctica", 1, 20);
            return new PracticaCalificada(codigo, nombre, peso, numero);
        }
        int unidad = leerEntero(campoNumero, "Unidad", 1, 10);
        return new ExamenParcial(codigo, nombre, peso, unidad);
    }

    private void guardarNota() {
        Evaluacion evaluacion = (Evaluacion) comboEvaluaciones.getSelectedItem();
        if (cursoSeleccionado == null || evaluacion == null) {
            mostrarAdvertencia("Este curso no tiene evaluaciones. Agrégalas primero en el formulario 2.");
            return;
        }
        try {
            boolean yaTeniaNota = evaluacion.estaRendida();
            double nota = leerDecimal(campoNota, "Nota", Calificacion.NOTA_MINIMA, Calificacion.NOTA_MAXIMA);
            evaluacion.registrarNota(nota, campoObservacion.getText());
            ventana.datosModificados("Nota de " + evaluacion.getCodigo() + " ("
                    + cursoSeleccionado.getCodigo() + ") " + (yaTeniaNota ? "actualizada." : "registrada."));
            mostrarCursoSeleccionado();

            String mensaje = "Nota " + Calificacion.formatear(nota) + (yaTeniaNota ? " actualizada" : " registrada")
                    + " en " + evaluacion.getNombre() + " (" + cursoSeleccionado.getCodigo() + ").\n"
                    + "Promedio actual: " + Calificacion.formatear(cursoSeleccionado.calcularPromedioActual())
                    + "  ·  Para aprobar: " + PanelInicio.describirNotaNecesaria(cursoSeleccionado);

            Alerta alerta = ventana.getGestorAlertas().evaluarCurso(cursoSeleccionado);
            if (ventana.getPreferencias().isAvisarAlRegistrarNota()
                    && alerta.getNivel().equals(Alertable.RIESGO_ALTO)) {
                mostrarAdvertencia(mensaje + "\n\nRecordatorio: el curso está en riesgo ALTO: "
                        + alerta.getMensaje() + "\nRevisa «Recomendaciones» y dale prioridad en tu horario.");
            } else {
                mostrarInfo(mensaje);
            }
        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarEvaluacion() {
        Evaluacion evaluacion = (Evaluacion) comboEvaluaciones.getSelectedItem();
        if (cursoSeleccionado == null || evaluacion == null) {
            return;
        }
        if (confirmar("¿Eliminar la evaluación «" + evaluacion + "» de " + cursoSeleccionado.getNombre()
                + "?\nSi tenía nota, también se perderá.")) {
            cursoSeleccionado.eliminarEvaluacion(evaluacion.getCodigo());
            ventana.datosModificados("Evaluación " + evaluacion.getCodigo() + " eliminada de "
                    + cursoSeleccionado.getCodigo() + ".");
            mostrarCursoSeleccionado();
        }
    }

    private class ManejadorBotones implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object origen = e.getSource();
            if (origen == botonRegistrarCurso) {
                registrarCurso();
            } else if (origen == botonEliminarCurso) {
                eliminarCurso();
            } else if (origen == botonAgregarEvaluacion) {
                agregarEvaluacion();
            } else if (origen == botonGuardarNota || origen == campoNota) {
                guardarNota();
            } else if (origen == botonEliminarEvaluacion) {
                eliminarEvaluacion();
            }
        }
    }
}
