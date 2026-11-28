package nucleo;

public class PracticaCalificada extends Evaluacion {
    private int numeroPractica;

    public PracticaCalificada(String codigo, String nombre, double peso) throws DatoInvalidoException {
        this(codigo, nombre, peso, 1);
    }

    public PracticaCalificada(String codigo, String nombre, double peso, int numeroPractica)
            throws DatoInvalidoException {
        super(codigo, nombre, peso);
        if (numeroPractica < 1 || numeroPractica > 20) {
            throw new DatoInvalidoException("El número de práctica debe estar entre 1 y 20.");
        }
        this.numeroPractica = numeroPractica;
    }

    @Override
    public String getTipo() {
        return "Práctica calificada";
    }

    @Override
    public String getClaveTipo() {
        return "PC";
    }

    @Override
    public String getDatoExtra() {
        return String.valueOf(numeroPractica);
    }

    @Override
    public String generarRecomendacion(double notaNecesaria) {
        if (notaNecesaria > Calificacion.NOTA_MAXIMA) {
            return nombre + ": aunque ya no alcanza para aprobar, practica para mejorar tu nota final.";
        }
        if (notaNecesaria >= 14) {
            return nombre + ": resuelve prácticas anteriores y repasa ejercicios tipo PC; necesitas "
                    + Calificacion.formatear(notaNecesaria) + ".";
        }
        return nombre + ": repasa la teoría de la semana y resuelve al menos 5 ejercicios.";
    }

    public int getNumeroPractica() {
        return numeroPractica;
    }
}
