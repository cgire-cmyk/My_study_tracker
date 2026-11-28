package nucleo;

import analisis.Recomendable;

public abstract class Evaluacion implements Recomendable {
    public static final int LARGO_MAXIMO_CODIGO = 10;
    public static final int LARGO_MAXIMO_NOMBRE = 40;

    protected String codigo;
    protected String nombre;
    protected double peso;
    protected Calificacion calificacion;

    public Evaluacion(String codigo, String nombre, double peso) throws DatoInvalidoException {
        this.codigo = validarTexto(codigo, "código de la evaluación", LARGO_MAXIMO_CODIGO).toUpperCase();
        this.nombre = validarTexto(nombre, "nombre de la evaluación", LARGO_MAXIMO_NOMBRE);
        if (peso <= 0 || peso > Curso.PESO_TOTAL) {
            throw new DatoInvalidoException("El peso debe ser mayor que 0 y como máximo "
                    + (int) Curso.PESO_TOTAL + " %.");
        }
        this.peso = peso;
        this.calificacion = null;
    }

    protected static String validarTexto(String texto, String campo, int largoMaximo)
            throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException("El " + campo + " no puede estar vacío.");
        }
        if (texto.trim().length() > largoMaximo) {
            throw new DatoInvalidoException("El " + campo + " admite como máximo "
                    + largoMaximo + " caracteres.");
        }
        return texto.trim();
    }

    public void registrarNota(double nota) throws DatoInvalidoException {
        registrarNota(nota, "");
    }

    public void registrarNota(double nota, String observacion) throws DatoInvalidoException {
        this.calificacion = new Calificacion(nota, observacion);
    }

    public void borrarNota() {
        this.calificacion = null;
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

    public static String formatearPeso(double peso) {
        if (peso == Math.floor(peso)) {
            return String.valueOf((int) peso);
        }
        return String.format("%.1f", peso);
    }

    public abstract String getTipo();

    public abstract String getClaveTipo();

    public abstract String getDatoExtra();

    @Override
    public abstract String generarRecomendacion(double notaNecesaria);

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + formatearPeso(peso) + " %)";
    }

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
