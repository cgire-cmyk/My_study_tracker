package planificacion;

import nucleo.Calificacion;
import nucleo.Curso;

public class MetaAprobacion extends Meta {

    private Curso curso;   

    public MetaAprobacion(Curso curso) {
        this(curso, Calificacion.NOTA_APROBATORIA);
    }

    public MetaAprobacion(Curso curso, double notaObjetivo) {
        super("Llegar a " + String.format("%.1f", notaObjetivo) + " en " + curso.getNombre(), notaObjetivo);
        this.curso = curso;
    }

    @Override
    public double calcularNotaMinimaRequerida() {
        return curso.calcularNotaNecesaria(notaObjetivo);
    }

    @Override
    public String getTipo() {
        return "Aprobacion de curso";
    }

    public Curso getCurso() {
        return curso;
    }
}
