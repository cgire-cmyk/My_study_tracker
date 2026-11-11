package nucleo;

/**
 * Nota obtenida en una evaluacion (escala vigesimal 0 - 20).
 * Modulo 1 - Nucleo academico (Integrante 1).
 */
public class Calificacion {

    // Constantes de la escala: static porque son iguales para todas las calificaciones
    public static final double NOTA_MINIMA = 0;
    public static final double NOTA_MAXIMA = 20;
    public static final double NOTA_APROBATORIA = 10.5;

    private double nota;
    private String observacion;

    public Calificacion(double nota) {
        this(nota, "");
    }

    public Calificacion(double nota, String observacion) {
        if (!esNotaValida(nota)) {
            throw new IllegalArgumentException("La nota debe estar entre "
                    + NOTA_MINIMA + " y " + NOTA_MAXIMA);
        }
        this.nota = nota;
        this.observacion = (observacion == null) ? "" : observacion;
    }

    /** Valida que la nota este dentro de la escala 0 - 20. */
    public static boolean esNotaValida(double nota) {
        return nota >= NOTA_MINIMA && nota <= NOTA_MAXIMA;
    }

    public boolean estaAprobada() {
        return nota >= NOTA_APROBATORIA;
    }

    public double getNota() {
        return nota;
    }

    public String getObservacion() {
        return observacion;
    }
}
