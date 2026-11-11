package nucleo;

import analisis.Recomendable;

public abstract class Evaluacion implements Recomendable {

    protected String codigo;
    protected String nombre;
    protected double peso;               
    protected Calificacion calificacion; 

    public Evaluacion(String codigo, String nombre, double peso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.peso = peso;
        this.calificacion = null;
    }

    public boolean registrarNota(double nota) {
        return registrarNota(nota, "");
    }

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

    public double getNota() {
        return estaRendida() ? calificacion.getNota() : 0;
    }

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
