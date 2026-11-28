package planificacion;

import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.DatoInvalidoException;
import nucleo.Evaluacion;

public class MetaAprobacion extends Meta {
    public static final String CLAVE_ARCHIVO = "META_CURSO";

    private Curso curso;

    public MetaAprobacion(Curso curso) throws DatoInvalidoException {
        this(curso, Calificacion.NOTA_APROBATORIA);
    }

    public MetaAprobacion(Curso curso, double notaObjetivo) throws DatoInvalidoException {
        super(construirNombre(curso, notaObjetivo), notaObjetivo);
        this.curso = curso;
    }

    private static String construirNombre(Curso curso, double notaObjetivo) throws DatoInvalidoException {
        if (curso == null) {
            throw new DatoInvalidoException("Elige el curso de la meta.");
        }
        if (notaObjetivo == Calificacion.NOTA_APROBATORIA) {
            return "Aprobar " + curso.getNombre();
        }
        return "Llegar a " + String.format("%.1f", notaObjetivo) + " en " + curso.getNombre();
    }

    @Override
    public double calcularNotaMinimaRequerida() {
        return curso.calcularNotaNecesaria(notaObjetivo);
    }

    @Override
    public String getTipo() {
        return "Curso";
    }

    @Override
    public String explicarCalculo() {
        double acumulado = curso.calcularPuntajeAcumulado();
        double pendiente = curso.calcularPesoPendiente();
        String texto = "Curso: " + curso + " (" + curso.getCreditos() + " créditos)\n"
                + "Puntaje acumulado: " + Calificacion.formatear(acumulado) + " de 20\n"
                + "Peso que falta rendir: " + Evaluacion.formatearPeso(pendiente) + " %\n";
        if (pendiente == 0) {
            return texto + "Ya no quedan evaluaciones: la nota final es "
                    + Calificacion.formatear(acumulado) + ".";
        }
        return texto + "Nota necesaria = (" + Calificacion.formatear(notaObjetivo) + " - "
                + Calificacion.formatear(acumulado) + ") / " + String.format("%.2f", pendiente / 100.0)
                + " = " + Calificacion.formatear((notaObjetivo - acumulado) / (pendiente / 100.0));
    }

    @Override
    public String[] obtenerDatosParaGuardar() {
        return new String[] {CLAVE_ARCHIVO, curso.getCodigo(), String.valueOf(notaObjetivo)};
    }

    @Override
    public boolean usaCurso(Curso otro) {
        return curso == otro;
    }

    public Curso getCurso() {
        return curso;
    }
}
