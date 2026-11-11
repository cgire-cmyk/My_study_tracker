package planificacion;

import nucleo.Calificacion;

public abstract class Meta {

    protected String nombre;
    protected double notaObjetivo;

    public Meta(String nombre, double notaObjetivo) {
        this.nombre = nombre;
        this.notaObjetivo = notaObjetivo;
    }

    public abstract double calcularNotaMinimaRequerida();

    public abstract String getTipo();

    public boolean esAlcanzable() {
        return calcularNotaMinimaRequerida() <= Calificacion.NOTA_MAXIMA;
    }

    public String obtenerEstado() {
        double requerida = calcularNotaMinimaRequerida();
        if (requerida <= 0) {
            return "META ASEGURADA: ya alcanzaste " + String.format("%.2f", notaObjetivo) + ".";
        }
        if (!esAlcanzable()) {
            return "META NO POSIBLE: se necesitaria mas de "
                    + (int) Calificacion.NOTA_MAXIMA + " en lo que falta rendir.";
        }
        return "ALCANZABLE: necesitas " + String.format("%.2f", requerida)
                + " en lo que falta rendir.";
    }

    public String getNombre() {
        return nombre;
    }

    public double getNotaObjetivo() {
        return notaObjetivo;
    }
}
