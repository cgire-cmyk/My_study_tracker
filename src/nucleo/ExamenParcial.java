package nucleo;

public class ExamenParcial extends Evaluacion {

    private int unidad;

    public ExamenParcial(String codigo, String nombre, double peso) {
        this(codigo, nombre, peso, 1);
    }

    public ExamenParcial(String codigo, String nombre, double peso, int unidad) {
        super(codigo, nombre, peso);
        this.unidad = unidad;
    }

    @Override
    public String getTipo() {
        return "Examen parcial";
    }

    @Override
    public String generarRecomendacion(double notaNecesaria) {
        if (notaNecesaria > Calificacion.NOTA_MAXIMA) {
            return nombre + ": la meta ya no es posible; enfocate en subir lo maximo posible.";
        }
        if (notaNecesaria >= 14) {
            return nombre + ": empieza a estudiar con 2 semanas de anticipacion y resuelve examenes"
                    + " pasados de la unidad " + unidad + " (necesitas "
                    + String.format("%.2f", notaNecesaria) + ").";
        }
        return nombre + ": haz un resumen de la unidad " + unidad + " y un simulacro la semana previa.";
    }

    public int getUnidad() {
        return unidad;
    }
}
