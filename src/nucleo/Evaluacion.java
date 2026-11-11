package nucleo;

import analisis.Recomendable;

/**
 * Clase abstracta base de todas las evaluaciones de un curso.
 * Cada tipo concreto define su tipo y su propia recomendacion (polimorfismo).
 * Modulo 1 - Nucleo academico (Integrante 1).
 */
public abstract class Evaluacion implements Recomendable {

    protected String codigo;
    protected String nombre;
    protected double peso;               // porcentaje del curso (ej. 20 = 20%)
    protected Calificacion calificacion; // composicion: null si aun no se rinde

    public Evaluacion(String codigo, String nombre, double peso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.peso = peso;
        this.calificacion = null;
    }

    /** Registra la nota sin observacion. Devuelve false si la nota no es valida. */
    public boolean registrarNota(double nota) {
        return registrarNota(nota, "");
    }

    /** Sobrecarga: registra la nota con una observacion. */
    public boolean registrarNota(double nota, String observacion) {
        if (!Calificacion.esNotaValida(nota)) {
            return false;
        }
        this.calificacion = new Calificacion(nota, observacion);
        return true;
    }

    public boolean estaRendida() {
        return calificacion != null;
    }

    /** Nota obtenida; 0 si la evaluacion aun no se rinde. */
    public double getNota() {
        return estaRendida() ? calificacion.getNota() : 0;
    }

    /** Puntos que esta evaluacion aporta al promedio final del curso. */
    public double calcularAporte() {
        return getNota() * peso / 100.0;
    }

    public abstract String getTipo();

    @Override
    public abstract String generarRecomendacion(double notaNecesaria);

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }
}
