package gui;

import java.io.IOException;
import javax.swing.JOptionPane;
import nucleo.DatoInvalidoException;
import planificacion.ArchivoDatos;
import planificacion.DatosDePrueba;
import planificacion.Estudiante;

public class Aplicacion {
    public static void main(String[] args) {
        String opcion = (args.length > 0) ? args[0] : "";
        ArchivoDatos archivo = new ArchivoDatos(ArchivoDatos.NOMBRE_POR_DEFECTO);
        Estudiante estudiante;
        String mensaje;

        try {
            if (opcion.equals("--vacio")) {
                estudiante = DatosDePrueba.crearEstudianteVacio();
                mensaje = "Empezaste sin datos. Registra tus cursos en «Cursos y notas».";
            } else if (archivo.existe() && !opcion.equals("--demo")) {
                estudiante = cargarArchivo(archivo);
                mensaje = "Datos cargados de " + archivo.getNombre() + ".";
            } else {
                estudiante = DatosDePrueba.crearEstudiante();
                mensaje = "Se cargaron datos de prueba (ficticios). Los cambios se guardan automáticamente.";
            }
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(null, "No se pudieron crear los datos iniciales:\n"
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        VentanaPrincipal ventana = new VentanaPrincipal(estudiante, archivo, mensaje);
        ventana.setVisible(true);
        ventana.mostrarRecordatorioInicial();
    }

    private static Estudiante cargarArchivo(ArchivoDatos archivo) throws DatoInvalidoException {
        try {
            return archivo.cargar();
        } catch (IOException | DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo leer " + archivo.getNombre() + ":\n"
                    + ex.getMessage() + "\n\nSe abrirán los datos de prueba. Si haces cambios,"
                    + " el archivo se reemplazará.", "Archivo de datos", JOptionPane.WARNING_MESSAGE);
            return DatosDePrueba.crearEstudiante();
        }
    }
}
