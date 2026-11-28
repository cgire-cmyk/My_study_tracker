package planificacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import nucleo.Curso;
import nucleo.DatoInvalidoException;
import nucleo.Evaluacion;
import nucleo.ExamenParcial;
import nucleo.PracticaCalificada;
import nucleo.Trabajo;

public class ArchivoDatos {
    public static final String NOMBRE_POR_DEFECTO = "mystudytracker_datos.txt";
    private static final String SEPARADOR = ";";
    private static final String SIN_NOTA = "-";

    private File archivo;

    public ArchivoDatos(String rutaArchivo) {
        this.archivo = new File(rutaArchivo);
    }

    public boolean existe() {
        return archivo.exists();
    }

    public String getRuta() {
        return archivo.getAbsolutePath();
    }

    public String getNombre() {
        return archivo.getName();
    }

    public void guardar(Estudiante estudiante) throws IOException {
        try (PrintWriter salida = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            salida.println("# MyStudyTracker - datos guardados por la aplicacion. No editar a mano.");
            salida.println(unir("ESTUDIANTE", estudiante.getCodigo(), estudiante.getNombres(),
                    estudiante.getCarrera()));

            for (Curso curso : estudiante.getCursos()) {
                salida.println(unir("CURSO", curso.getCodigo(), curso.getNombre(),
                        String.valueOf(curso.getCreditos())));
                for (Evaluacion e : curso.getEvaluaciones()) {
                    String nota = e.estaRendida() ? String.valueOf(e.getNota()) : SIN_NOTA;
                    String observacion = e.estaRendida() ? e.getCalificacion().getObservacion() : "";
                    salida.println(unir("EVAL", e.getClaveTipo(), e.getCodigo(), e.getNombre(),
                            String.valueOf(e.getPeso()), e.getDatoExtra(), nota, observacion));
                }
            }

            HorarioEstudio horario = estudiante.getHorario();
            for (String dia : HorarioEstudio.DIAS) {
                salida.println(unir("DISP", dia, String.valueOf(horario.getHorasDisponibles(dia))));
            }

            for (Meta meta : estudiante.getMetas()) {
                salida.println(unir(meta.obtenerDatosParaGuardar()));
            }
        }
    }

    private String unir(String... campos) {
        String linea = "";
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                linea += SEPARADOR;
            }
            linea += campos[i].replace(SEPARADOR, ",");
        }
        return linea;
    }

    public Estudiante cargar() throws IOException, DatoInvalidoException {
        Estudiante estudiante = null;
        Curso cursoActual = null;
        int numeroLinea = 0;

        try (BufferedReader entrada = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = entrada.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                String[] p = linea.split(SEPARADOR, -1);
                try {
                    if (p[0].equals("ESTUDIANTE")) {
                        estudiante = new Estudiante(p[1], p[2], p[3]);
                    } else if (estudiante == null) {
                        throw new DatoInvalidoException("falta la línea ESTUDIANTE al inicio");
                    } else if (p[0].equals("CURSO")) {
                        cursoActual = new Curso(p[1], p[2], Integer.parseInt(p[3]));
                        estudiante.agregarCurso(cursoActual);
                    } else if (p[0].equals("EVAL")) {
                        if (cursoActual == null) {
                            throw new DatoInvalidoException("evaluación sin curso");
                        }
                        cursoActual.agregarEvaluacion(crearEvaluacion(p));
                    } else if (p[0].equals("DISP")) {
                        estudiante.getHorario().definirDisponibilidad(p[1], Integer.parseInt(p[2]));
                    } else if (p[0].equals(MetaAprobacion.CLAVE_ARCHIVO)) {
                        Curso curso = estudiante.buscarCurso(p[1]);
                        estudiante.agregarMeta(new MetaAprobacion(curso, Double.parseDouble(p[2])));
                    } else if (p[0].equals(MetaPromedio.CLAVE_ARCHIVO)) {
                        estudiante.agregarMeta(new MetaPromedio(p[1], Double.parseDouble(p[2]),
                                estudiante.getCursos()));
                    } else {
                        throw new DatoInvalidoException("tipo de línea desconocido \"" + p[0] + "\"");
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    throw new DatoInvalidoException("Línea " + numeroLinea + " del archivo dañada.");
                } catch (DatoInvalidoException ex) {
                    throw new DatoInvalidoException("Línea " + numeroLinea + ": " + ex.getMessage());
                }
            }
        }
        if (estudiante == null) {
            throw new DatoInvalidoException("El archivo está vacío.");
        }
        return estudiante;
    }

    private Evaluacion crearEvaluacion(String[] p) throws DatoInvalidoException {
        String tipo = p[1];
        String codigo = p[2];
        String nombre = p[3];
        double peso = Double.parseDouble(p[4]);
        Evaluacion evaluacion;
        if (tipo.equals("PC")) {
            evaluacion = new PracticaCalificada(codigo, nombre, peso, Integer.parseInt(p[5]));
        } else if (tipo.equals("TR")) {
            evaluacion = new Trabajo(codigo, nombre, peso, Boolean.parseBoolean(p[5]));
        } else if (tipo.equals("EX")) {
            evaluacion = new ExamenParcial(codigo, nombre, peso, Integer.parseInt(p[5]));
        } else {
            throw new DatoInvalidoException("tipo de evaluación desconocido \"" + tipo + "\"");
        }
        if (!p[6].equals(SIN_NOTA)) {
            evaluacion.registrarNota(Double.parseDouble(p[6]), p[7]);
        }
        return evaluacion;
    }
}
