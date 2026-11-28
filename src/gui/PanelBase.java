package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import nucleo.DatoInvalidoException;
import planificacion.Estudiante;

public abstract class PanelBase extends JPanel {
    protected VentanaPrincipal ventana;

    public PanelBase(VentanaPrincipal ventana, String titulo, String descripcion) {
        this.ventana = ventana;
        setLayout(new BorderLayout(12, 12));
        setBackground(Paleta.FONDO);
        agregarMargen(this, 16);

        JPanel cabecera = new JPanel(new GridLayout(2, 1, 0, 2));
        cabecera.setBackground(Paleta.FONDO);
        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(Paleta.FUENTE_TITULO);
        etiquetaTitulo.setForeground(Paleta.PRIMARIO);
        JLabel etiquetaDescripcion = new JLabel(descripcion);
        etiquetaDescripcion.setFont(Paleta.FUENTE_NORMAL);
        etiquetaDescripcion.setForeground(Paleta.TEXTO_SUAVE);
        cabecera.add(etiquetaTitulo);
        cabecera.add(etiquetaDescripcion);
        add(cabecera, BorderLayout.NORTH);
    }

    public abstract void actualizar();

    protected Estudiante getEstudiante() {
        return ventana.getEstudiante();
    }

    protected static void agregarMargen(JComponent componente, int pixeles) {
        componente.setBorder(BorderFactory.createEmptyBorder(pixeles, pixeles, pixeles, pixeles));
    }

    protected JPanel crearTarjeta(String titulo) {
        JPanel tarjeta = new JPanel(new BorderLayout(8, 8));
        tarjeta.setBackground(Paleta.TARJETA);
        agregarMargen(tarjeta, 12);
        if (titulo != null) {
            JLabel etiqueta = new JLabel(titulo);
            etiqueta.setFont(Paleta.FUENTE_SUBTITULO);
            etiqueta.setForeground(Paleta.PRIMARIO);
            tarjeta.add(etiqueta, BorderLayout.NORTH);
        }
        return tarjeta;
    }

    protected JButton crearBoton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setBackground(fondo);
        boton.setForeground(Paleta.TEXTO_CLARO);
        boton.setFont(Paleta.FUENTE_NEGRITA);
        boton.setFocusPainted(false);
        return boton;
    }

    protected JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Paleta.FUENTE_NORMAL);
        etiqueta.setForeground(Paleta.TEXTO);
        return etiqueta;
    }

    protected JLabel crearSello(String texto, Color fondo) {
        JLabel sello = new JLabel(texto, JLabel.CENTER);
        sello.setOpaque(true);
        sello.setBackground(fondo);
        sello.setForeground(Paleta.TEXTO_CLARO);
        sello.setFont(Paleta.FUENTE_NEGRITA);
        agregarMargen(sello, 6);
        return sello;
    }

    protected JTextField crearCampo(int columnas) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(Paleta.FUENTE_NORMAL);
        return campo;
    }

    protected JTextArea crearAreaTexto(int filas, int columnas) {
        JTextArea area = new JTextArea(filas, columnas);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(Paleta.FUENTE_NORMAL);
        area.setForeground(Paleta.TEXTO);
        area.setBackground(Paleta.TARJETA);
        agregarMargen(area, 8);
        return area;
    }

    protected JScrollPane crearScroll(JComponent contenido) {
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(contenido.getBackground());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    protected JPanel alinearArriba(JComponent componente, Color fondo) {
        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.setBackground(fondo);
        envoltura.add(componente, BorderLayout.NORTH);
        return envoltura;
    }

    protected void ajustarAlAncho(JComponent contenido) {
        contenido.setPreferredSize(null);
        int altoReal = contenido.getPreferredSize().height;
        contenido.setPreferredSize(new Dimension(100, altoReal));
    }

    protected void redistribuir(JScrollPane scroll) {
        scroll.validate();
        scroll.repaint();
    }

    protected String leerTexto(JTextField campo, String nombreCampo) throws DatoInvalidoException {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            campo.requestFocus();
            throw new DatoInvalidoException("Completa el campo \"" + nombreCampo + "\".");
        }
        return texto;
    }

    protected double leerDecimal(JTextField campo, String nombreCampo, double minimo, double maximo)
            throws DatoInvalidoException {
        String texto = leerTexto(campo, nombreCampo).replace(',', '.');
        double valor;
        try {
            valor = Double.parseDouble(texto);
        } catch (NumberFormatException ex) {
            campo.requestFocus();
            throw new DatoInvalidoException("En \"" + nombreCampo + "\" escribe un número (por ejemplo 12.5), no \""
                    + campo.getText().trim() + "\".");
        }
        if (valor < minimo || valor > maximo) {
            campo.requestFocus();
            throw new DatoInvalidoException("\"" + nombreCampo + "\" debe estar entre "
                    + formatearLimite(minimo) + " y " + formatearLimite(maximo) + ".");
        }
        return valor;
    }

    protected int leerEntero(JTextField campo, String nombreCampo, int minimo, int maximo)
            throws DatoInvalidoException {
        String texto = leerTexto(campo, nombreCampo);
        int valor;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            campo.requestFocus();
            throw new DatoInvalidoException("En \"" + nombreCampo + "\" escribe un número entero, no \""
                    + texto + "\".");
        }
        if (valor < minimo || valor > maximo) {
            campo.requestFocus();
            throw new DatoInvalidoException("\"" + nombreCampo + "\" debe estar entre "
                    + minimo + " y " + maximo + ".");
        }
        return valor;
    }

    private String formatearLimite(double valor) {
        return (valor == Math.floor(valor)) ? String.valueOf((int) valor) : String.valueOf(valor);
    }

    protected void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "MyStudyTracker", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    protected void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Revisa los datos", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(ventana, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }
}
