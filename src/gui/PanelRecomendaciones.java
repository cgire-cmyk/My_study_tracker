package gui;

import analisis.GestorRecomendaciones;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import nucleo.Curso;
import planificacion.Meta;

public class PanelRecomendaciones extends PanelBase {
    private static final String OPCION_TODOS = "Todos los cursos";

    private JComboBox<String> comboFiltro;
    private JCheckBox checkMetas;
    private JTextArea areaRecomendaciones;
    private GestorRecomendaciones gestor;
    private boolean llenandoCombo;

    public PanelRecomendaciones(VentanaPrincipal ventana) {
        super(ventana, "Recomendaciones de estudio",
                "Consejos según el tipo de evaluación pendiente, el riesgo de cada curso y tus metas.");
        gestor = new GestorRecomendaciones();
        ManejadorFiltros manejador = new ManejadorFiltros();

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtros.setBackground(Paleta.TARJETA);
        comboFiltro = new JComboBox<>();
        comboFiltro.setFont(Paleta.FUENTE_NORMAL);
        comboFiltro.addActionListener(manejador);
        checkMetas = new JCheckBox("Incluir el estado de mis metas", true);
        checkMetas.setBackground(Paleta.TARJETA);
        checkMetas.setFont(Paleta.FUENTE_NORMAL);
        checkMetas.addActionListener(manejador);
        JLabel etiqueta = crearEtiqueta("Mostrar:");
        etiqueta.setFont(Paleta.FUENTE_NEGRITA);
        filtros.add(etiqueta);
        filtros.add(comboFiltro);
        filtros.add(checkMetas);

        areaRecomendaciones = crearAreaTexto(20, 60);

        JPanel tarjeta = crearTarjeta(null);
        tarjeta.add(filtros, BorderLayout.NORTH);
        tarjeta.add(crearScroll(areaRecomendaciones), BorderLayout.CENTER);
        add(tarjeta, BorderLayout.CENTER);
    }

    @Override
    public void actualizar() {
        String anterior = (String) comboFiltro.getSelectedItem();
        llenandoCombo = true;
        comboFiltro.removeAllItems();
        comboFiltro.addItem(OPCION_TODOS);
        for (Curso curso : getEstudiante().getCursos()) {
            comboFiltro.addItem(curso.toString());
        }
        if (anterior != null) {
            comboFiltro.setSelectedItem(anterior);
        }
        llenandoCombo = false;
        escribirRecomendaciones();
    }

    private void escribirRecomendaciones() {
        ArrayList<Curso> cursos = getEstudiante().getCursos();
        if (cursos.isEmpty()) {
            areaRecomendaciones.setText("Aún no hay recomendaciones: registra tus cursos y evaluaciones.");
            return;
        }
        int indice = comboFiltro.getSelectedIndex();
        checkMetas.setEnabled(indice <= 0);
        String texto = "";
        for (int i = 0; i < cursos.size(); i++) {
            if (indice <= 0 || indice == i + 1) {
                texto += escribirCurso(cursos.get(i));
            }
        }
        if (checkMetas.isSelected() && indice <= 0) {
            texto += "■ MIS METAS\n";
            if (getEstudiante().getMetas().isEmpty()) {
                texto += "   • Aún no tienes metas. Crea una en la sección «Metas».\n";
            }
            for (Meta meta : getEstudiante().getMetas()) {
                texto += "   • " + meta.getNombre() + ": " + meta.obtenerEstado() + "\n";
            }
        }
        areaRecomendaciones.setText(texto);
        areaRecomendaciones.setCaretPosition(0);
    }

    private String escribirCurso(Curso curso) {
        String texto = "■ " + curso.getCodigo() + " - " + curso.getNombre().toUpperCase()
                + "   (riesgo " + curso.obtenerNivelRiesgo() + ")\n";
        for (String recomendacion : gestor.generarRecomendaciones(curso)) {
            texto += "   • " + recomendacion + "\n";
        }
        return texto + "\n";
    }

    private class ManejadorFiltros implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!llenandoCombo) {
                escribirRecomendaciones();
            }
        }
    }
}
