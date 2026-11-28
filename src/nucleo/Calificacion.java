package nucleo;

public class Calificacion {
    public static final double NOTA_MINIMA = 0;
    public static final double NOTA_MAXIMA = 20;
    public static final double NOTA_APROBATORIA = 10.5;

    private double nota;
    private String observacion;

    public Calificacion(double nota) throws DatoInvalidoException {
        this(nota, "");
    }

    public Calificacion(double nota, String observacion) throws DatoInvalidoException {
        if (!esNotaValida(nota)) {
            throw new DatoInvalidoException("La nota debe estar entre "
                    + (int) NOTA_MINIMA + " y " + (int) NOTA_MAXIMA + ".");
        }
        this.nota = nota;
        this.observacion = (observacion == null) ? "" : observacion.trim();
    }

    public static boolean esNotaValida(double nota) {
        return nota >= NOTA_MINIMA && nota <= NOTA_MAXIMA;
    }

    public static String formatear(double valor) {
        return String.format("%.2f", valor);
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
