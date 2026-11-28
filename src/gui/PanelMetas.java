package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.DatoInvalidoException;
import planificacion.Meta;
import planificacion.MetaAprobacion;
import planificacion.MetaPromedio;

public class PanelMetas extends PanelBase {
    private static final String COMANDO_VER = "VER";
    private static final String COMANDO_QUITAR = "QUITAR";

    private JRadioButton radioAprobar;
    private JRadioButton radioTercio;
    private JRadioButton radioQuinto;
    private JRadioButton radioBeca;
    private JComboBox<Curso> comboCursos;
    private JTextField campoObjetivo;
    private JButton botonCalcular;
    private JButton botonGuardar;
    private JTextArea areaResultado;

    private JLabel etiquetaResumenMetas;
    private JPanel panelListaMetas;
    private JScrollPane scrollMetas;

    public PanelMetas(VentanaPrincipal ventana) {
        super(ventana, "Metas académicas",
                "Elige una meta y descubre la nota mínima que necesitas en lo que te falta rendir.");

        JPanel formulario = crearFormulario();
        formulario.setPreferredSize(new Dimension(370, 0));

        JPanel centro = new JPanel(new BorderLayout(12, 0));
        centro.setBackground(Paleta.FONDO);
        centro.add(formulario, BorderLayout.WEST);
        centro.add(crearListaMetas(), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearFormulario() {
        ManejadorEventos manejador = new ManejadorEventos();
        JPanel tarjeta = crearTarjeta("Nueva meta");

        radioAprobar = crearRadio("Aprobar un curso o llegar a una nota en él", manejador);
        radioTercio = crearRadio(MetaPromedio.TERCIO_SUPERIOR + " (promedio de referencia "
                + MetaPromedio.REFERENCIA_TERCIO + ")", manejador);
        radioQuinto = crearRadio(MetaPromedio.QUINTO_SUPERIOR + " (referencia "
                + MetaPromedio.REFERENCIA_QUINTO + ")", manejador);
        radioBeca = crearRadio(MetaPromedio.BECA + " (referencia " + MetaPromedio.REFERENCIA_BECA + ")", manejador);
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioAprobar);
        grupo.add(radioTercio);
        grupo.add(radioQuinto);
        grupo.add(radioBeca);
        radioAprobar.setSelected(true);

        JPanel radios = new JPanel(new GridLayout(0, 1, 0, 2));
        radios.setBackground(Paleta.TARJETA);
        radios.add(crearEtiqueta("¿Qué meta quieres alcanzar?"));
        radios.add(radioAprobar);
        radios.add(radioTercio);
        radios.add(radioQuinto);
        radios.add(radioBeca);

        comboCursos = new JComboBox<>();
        campoObjetivo = crearCampo(5);
        campoObjetivo.setText(String.valueOf(Calificacion.NOTA_APROBATORIA));
        JPanel campos = new JPanel(new GridLayout(2, 2, 6, 6));
        campos.setBackground(Paleta.TARJETA);
        campos.add(crearEtiqueta("Curso"));
        campos.add(comboCursos);
        campos.add(crearEtiqueta("Nota objetivo (0 - 20)"));
        campos.add(campoObjetivo);

        botonCalcular = crearBoton("Calcular", Paleta.PRIMARIO);
        botonCalcular.addActionListener(manejador);
        botonGuardar = crearBoton("Guardar meta", Paleta.VERDE);
        botonGuardar.addActionListener(manejador);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botones.setBackground(Paleta.TARJETA);
        botones.add(botonCalcular);
        botones.add(botonGuardar);

        JLabel nota = new JLabel("Las referencias son supuestos: ajústalas a tu facultad.");
        nota.setFont(Paleta.FUENTE_PEQUENA);
        nota.setForeground(Paleta.TEXTO_SUAVE);

        JPanel parteSuperior = new JPanel(new BorderLayout(0, 8));
        parteSuperior.setBackground(Paleta.TARJETA);
        parteSuperior.add(radios, BorderLayout.NORTH);
        parteSuperior.add(campos, BorderLayout.CENTER);
        JPanel pie = new JPanel(new GridLayout(2, 1, 0, 6));
        pie.setBackground(Paleta.TARJETA);
        pie.add(nota);
        pie.add(botones);
        parteSuperior.add(pie, BorderLayout.SOUTH);

        areaResultado = crearAreaTexto(8, 30);
        areaResultado.setBackground(Paleta.FILA_ALTERNA);

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setBackground(Paleta.TARJETA);
        contenido.add(parteSuperior, BorderLayout.NORTH);
        contenido.add(crearScroll(areaResultado), BorderLayout.CENTER);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private JRadioButton crearRadio(String texto, ActionListener manejador) {
        JRadioButton radio = new JRadioButton(texto);
        radio.setBackground(Paleta.TARJETA);
        radio.setFont(Paleta.FUENTE_NORMAL);
        radio.addActionListener(manejador);
        return radio;
    }

    private JPanel crearListaMetas() {
        JPanel tarjeta = crearTarjeta("Mis metas");
        etiquetaResumenMetas = crearEtiqueta(" ");
        etiquetaResumenMetas.setForeground(Paleta.TEXTO_SUAVE);
        panelListaMetas = new JPanel(new GridLayout(0, 1, 0, 8));
        panelListaMetas.setBackground(Paleta.TARJETA);

        JPanel contenido = new JPanel(new BorderLayout(0, 8));
        contenido.setBackground(Paleta.TARJETA);
        contenido.add(etiquetaResumenMetas, BorderLayout.NORTH);
        scrollMetas = crearScroll(alinearArriba(panelListaMetas, Paleta.TARJETA));
        contenido.add(scrollMetas, BorderLayout.CENTER);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    @Override
    public void actualizar() {
        Curso anterior = (Curso) comboCursos.getSelectedItem();
        comboCursos.removeAllItems();
        for (Curso curso : getEstudiante().getCursos()) {
            comboCursos.addItem(curso);
        }
        if (anterior != null && getEstudiante().getCursos().contains(anterior)) {
            comboCursos.setSelectedItem(anterior);
        }
        comboCursos.setEnabled(radioAprobar.isSelected() && comboCursos.getItemCount() > 0);
        if (areaResultado.getText().isEmpty()) {
            areaResultado.setText("Elige el tipo de meta, revisa la nota objetivo y pulsa «Calcular».\n\n"
                    + "El sistema te dirá la nota mínima que necesitas, en promedio, en todas las "
                    + "evaluaciones que aún no rindes.");
        }
        dibujarListaMetas();
    }

    private void dibujarListaMetas() {
        ArrayList<Meta> metas = getEstudiante().getMetas();
        panelListaMetas.removeAll();
        int posibles = 0;
        for (Meta meta : metas) {
            panelListaMetas.add(crearFilaMeta(meta));
            if (!meta.obtenerNivel().equals(Meta.NIVEL_NO_POSIBLE)) {
                posibles++;
            }
        }
        if (metas.isEmpty()) {
            etiquetaResumenMetas.setText("Aún no tienes metas guardadas. Crea una con el formulario.");
        } else {
            etiquetaResumenMetas.setText("Tienes " + metas.size() + " meta(s): " + posibles
                    + " todavía posible(s). Pulsa «Ver» para revisar el cálculo.");
        }
        ajustarAlAncho(panelListaMetas);
        redistribuir(scrollMetas);
    }

    private JPanel crearFilaMeta(Meta meta) {
        String nivel = meta.obtenerNivel();
        Color fondo = Paleta.colorMetaClaro(nivel);

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(fondo);
        agregarMargen(fila, 8);

        JLabel sello = crearSello(nivel, Paleta.colorMeta(nivel));
        sello.setPreferredSize(new Dimension(100, 30));

        JPanel textos = new JPanel(new GridLayout(3, 1));
        textos.setBackground(fondo);
        JLabel nombre = new JLabel(meta.getNombre());
        nombre.setToolTipText(meta.getNombre());
        nombre.setFont(Paleta.FUENTE_NEGRITA);
        nombre.setForeground(Paleta.TEXTO);
        JLabel tipo = new JLabel("Meta de " + meta.getTipo().toLowerCase());
        tipo.setFont(Paleta.FUENTE_PEQUENA);
        tipo.setForeground(Paleta.TEXTO_SUAVE);
        JLabel detalle = new JLabel(describirRequerida(meta));
        detalle.setFont(Paleta.FUENTE_PEQUENA);
        detalle.setForeground(Paleta.TEXTO);
        textos.add(nombre);
        textos.add(tipo);
        textos.add(detalle);

        JPanel botones = new JPanel(new GridLayout(2, 1, 0, 4));
        botones.setBackground(fondo);
        AccionMetaListener listener = new AccionMetaListener(meta);
        JButton botonVer = crearBoton("Ver", Paleta.PRIMARIO);
        botonVer.setActionCommand(COMANDO_VER);
        botonVer.addActionListener(listener);
        JButton botonQuitar = crearBoton("Quitar", Paleta.GRIS);
        botonQuitar.setActionCommand(COMANDO_QUITAR);
        botonQuitar.addActionListener(listener);
        botones.add(botonVer);
        botones.add(botonQuitar);

        fila.add(sello, BorderLayout.WEST);
        fila.add(textos, BorderLayout.CENTER);
        fila.add(botones, BorderLayout.EAST);
        return fila;
    }

    private String describirRequerida(Meta meta) {
        String nivel = meta.obtenerNivel();
        if (nivel.equals(Meta.NIVEL_SIN_DATOS)) {
            return "Registra cursos para calcularla";
        }
        if (nivel.equals(Meta.NIVEL_ASEGURADA)) {
            return "¡Ya la alcanzaste!";
        }
        if (nivel.equals(Meta.NIVEL_NO_POSIBLE)) {
            return "Necesitarías más de " + (int) Calificacion.NOTA_MAXIMA;
        }
        return "Necesitas " + Calificacion.formatear(meta.calcularNotaMinimaRequerida()) + " en lo pendiente";
    }

    private Meta crearMetaDesdeFormulario() throws DatoInvalidoException {
        double objetivo = leerDecimal(campoObjetivo, "Nota objetivo", 1, Calificacion.NOTA_MAXIMA);
        if (radioAprobar.isSelected()) {
            Curso curso = (Curso) comboCursos.getSelectedItem();
            if (curso == null) {
                throw new DatoInvalidoException("Primero registra un curso en «Cursos y notas».");
            }
            return new MetaAprobacion(curso, objetivo);
        }
        if (getEstudiante().getCursos().isEmpty()) {
            throw new DatoInvalidoException("Para una meta de promedio necesitas al menos un curso registrado.");
        }
        String categoria = radioTercio.isSelected() ? MetaPromedio.TERCIO_SUPERIOR
                : radioQuinto.isSelected() ? MetaPromedio.QUINTO_SUPERIOR : MetaPromedio.BECA;
        return new MetaPromedio(categoria, objetivo, getEstudiante().getCursos());
    }

    private void mostrarResultado(Meta meta) {
        String texto = "META: " + meta.getNombre() + "\n"
                + "Tipo: " + meta.getTipo() + "\n\n"
                + meta.explicarCalculo() + "\n\n"
                + "RESULTADO -> " + meta.obtenerEstado() + "\n\n"
                + "Consejo: " + obtenerConsejo(meta.obtenerNivel());
        areaResultado.setText(texto);
        areaResultado.setCaretPosition(0);
    }

    private String obtenerConsejo(String nivel) {
        if (nivel.equals(Meta.NIVEL_ASEGURADA)) {
            return "¡ya la tienes! Mantén el ritmo para no bajar tu promedio.";
        }
        if (nivel.equals(Meta.NIVEL_ALCANZABLE)) {
            return "es posible. Organiza tu horario y prepara cada evaluación pendiente.";
        }
        if (nivel.equals(Meta.NIVEL_EXIGENTE)) {
            return "es posible pero exigente: necesitas notas altas en todo lo que falta. "
                    + "Prioriza los cursos en riesgo y pide asesoría.";
        }
        if (nivel.equals(Meta.NIVEL_NO_POSIBLE)) {
            return "con las notas actuales no se puede llegar. Elige una meta más cercana, "
                    + "por ejemplo aprobar el curso o una nota menor.";
        }
        return "registra tus cursos y notas para poder calcular.";
    }

    private void calcular() {
        try {
            mostrarResultado(crearMetaDesdeFormulario());
        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void guardarMeta() {
        try {
            Meta meta = crearMetaDesdeFormulario();
            getEstudiante().agregarMeta(meta);
            ventana.datosModificados("Meta «" + meta.getNombre() + "» guardada.");
            mostrarResultado(meta);
            dibujarListaMetas();
            mostrarInfo("Meta guardada: " + meta.getNombre() + "\n" + meta.obtenerEstado());
        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void cambiarTipoMeta(Object radio) {
        double objetivo;
        if (radio == radioTercio) {
            objetivo = MetaPromedio.REFERENCIA_TERCIO;
        } else if (radio == radioQuinto) {
            objetivo = MetaPromedio.REFERENCIA_QUINTO;
        } else if (radio == radioBeca) {
            objetivo = MetaPromedio.REFERENCIA_BECA;
        } else {
            objetivo = Calificacion.NOTA_APROBATORIA;
        }
        campoObjetivo.setText(String.valueOf(objetivo));
        comboCursos.setEnabled(radio == radioAprobar && comboCursos.getItemCount() > 0);
    }

    private class ManejadorEventos implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object origen = e.getSource();
            if (origen == botonCalcular) {
                calcular();
            } else if (origen == botonGuardar) {
                guardarMeta();
            } else {
                cambiarTipoMeta(origen);
            }
        }
    }

    private class AccionMetaListener implements ActionListener {
        private Meta meta;

        public AccionMetaListener(Meta meta) {
            this.meta = meta;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(COMANDO_VER)) {
                mostrarResultado(meta);
            } else if (confirmar("¿Quitar la meta «" + meta.getNombre() + "»?")) {
                getEstudiante().eliminarMeta(meta);
                ventana.datosModificados("Meta «" + meta.getNombre() + "» eliminada.");
                dibujarListaMetas();
            }
        }
    }
}
