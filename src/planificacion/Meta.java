package planificacion;

import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.DatoInvalidoException;

public abstract class Meta {
    public static final String NIVEL_ASEGURADA = "ASEGURADA";
    public static final String NIVEL_ALCANZABLE = "ALCANZABLE";
    public static final String NIVEL_EXIGENTE = "EXIGENTE";
    public static final String NIVEL_NO_POSIBLE = "NO POSIBLE";
    public static final String NIVEL_SIN_DATOS = "SIN DATOS";

    private static final double UMBRAL_EXIGENTE = 16;

    protected String nombre;
    protected double notaObjetivo;

    public Meta(String nombre, double notaObjetivo) throws DatoInvalidoException {
        if (notaObjetivo <= Calificacion.NOTA_MINIMA || notaObjetivo > Calificacion.NOTA_MAXIMA) {
            throw new DatoInvalidoException("La nota objetivo debe ser mayor que "
                    + (int) Calificacion.NOTA_MINIMA + " y como máximo "
                    + (int) Calificacion.NOTA_MAXIMA + ".");
        }
        this.nombre = nombre;
        this.notaObjetivo = notaObjetivo;
    }

    public abstract double calcularNotaMinimaRequerida();

    public abstract String getTipo();

    public abstract String explicarCalculo();

    public abstract String[] obtenerDatosParaGuardar();

    public boolean usaCurso(Curso curso) {
        return false;
    }

    protected boolean tieneDatosSuficientes() {
        return true;
    }

    public boolean esAlcanzable() {
        return calcularNotaMinimaRequerida() <= Calificacion.NOTA_MAXIMA;
    }

    public String obtenerNivel() {
        if (!tieneDatosSuficientes()) {
            return NIVEL_SIN_DATOS;
        }
        double requerida = calcularNotaMinimaRequerida();
        if (requerida <= 0) {
            return NIVEL_ASEGURADA;
        }
        if (!esAlcanzable()) {
            return NIVEL_NO_POSIBLE;
        }
        if (requerida >= UMBRAL_EXIGENTE) {
            return NIVEL_EXIGENTE;
        }
        return NIVEL_ALCANZABLE;
    }

    public String obtenerEstado() {
        String nivel = obtenerNivel();
        double requerida = calcularNotaMinimaRequerida();
        if (nivel.equals(NIVEL_SIN_DATOS)) {
            return "SIN DATOS: registra al menos un curso para calcular esta meta.";
        }
        if (nivel.equals(NIVEL_ASEGURADA)) {
            return "META ASEGURADA: ya alcanzaste " + Calificacion.formatear(notaObjetivo) + ".";
        }
        if (nivel.equals(NIVEL_NO_POSIBLE)) {
            return "META NO POSIBLE: se necesitaría más de "
                    + (int) Calificacion.NOTA_MAXIMA + " en lo que falta rendir.";
        }
        String texto = "ALCANZABLE: necesitas " + Calificacion.formatear(requerida)
                + " en lo que falta rendir";
        if (nivel.equals(NIVEL_EXIGENTE)) {
            texto += " (meta exigente)";
        }
        return texto + ".";
    }

    public String getNombre() {
        return nombre;
    }

    public double getNotaObjetivo() {
        return notaObjetivo;
    }

    @Override
    public String toString() {
        return "[" + getTipo() + "] " + nombre;
    }
}
