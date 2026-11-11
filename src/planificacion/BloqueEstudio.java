package planificacion;

import nucleo.Curso;


public class BloqueEstudio {

    private String dia;
    private int horas;
    private Curso curso;  

    public BloqueEstudio(String dia, int horas, Curso curso) {
        this.dia = dia;
        this.horas = horas;
        this.curso = curso;
    }

    public String getDia() {
        return dia;
    }

    public int getHoras() {
        return horas;
    }

    public Curso getCurso() {
        return curso;
    }

    @Override
    public String toString() {
        return horas + " h - " + curso.getNombre() + " (riesgo " + curso.obtenerNivelRiesgo() + ")";
    }
}
