package nucleo;

/**
 * Practica calificada (PC). Modulo 1 - Nucleo academico (Integrante 1).
 */
public class PracticaCalificada extends Evaluacion {

    private int numeroPractica;

    public PracticaCalificada(String codigo, String nombre, double peso) {
        this(codigo, nombre, peso, 1);
    }

    public PracticaCalificada(String codigo, String nombre, double peso, int numeroPractica) {
        super(codigo, nombre, peso);
        this.numeroPractica = numeroPractica;
    }

    @Override
    public String getTipo() {
        return "Practica calificada";
    }

    @Override
    public String generarRecomendacion(double notaNecesaria) {
        if (notaNecesaria > Calificacion.NOTA_MAXIMA) {
            return nombre + ": aunque ya no alcanza para aprobar, practica para mejorar tu nota final.";
        }
        if (notaNecesaria >= 14) {
            return nombre + ": resuelve practicas anteriores y repasa ejercicios tipo PC; necesitas "
                    + String.format("%.2f", notaNecesaria) + ".";
        }
        return nombre + ": repasa la teoria de la semana y resuelve al menos 5 ejercicios.";
    }

    public int getNumeroPractica() {
        return numeroPractica;
    }
}
