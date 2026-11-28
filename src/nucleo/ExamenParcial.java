package nucleo;

public class ExamenParcial extends Evaluacion {
    private int unidad;

    public ExamenParcial(String codigo, String nombre, double peso) throws DatoInvalidoException {
        this(codigo, nombre, peso, 1);
    }

    public ExamenParcial(String codigo, String nombre, double peso, int unidad)
            throws DatoInvalidoException {
        super(codigo, nombre, peso);
        if (unidad < 1 || unidad > 10) {
            throw new DatoInvalidoException("La unidad debe estar entre 1 y 10.");
        }
        this.unidad = unidad;
    }

    @Override
    public String getTipo() {
        return "Examen";
    }

    @Override
    public String getClaveTipo() {
        return "EX";
    }

    @Override
    public String getDatoExtra() {
        return String.valueOf(unidad);
    }

    @Override
    public String generarRecomendacion(double notaNecesaria) {
        if (notaNecesaria > Calificacion.NOTA_MAXIMA) {
            return nombre + ": la meta ya no es posible; enfócate en subir lo máximo posible.";
        }
        if (notaNecesaria >= 14) {
            return nombre + ": empieza a estudiar con 2 semanas de anticipación y resuelve exámenes"
                    + " pasados de la unidad " + unidad + " (necesitas "
                    + Calificacion.formatear(notaNecesaria) + ").";
        }
        return nombre + ": haz un resumen de la unidad " + unidad + " y un simulacro la semana previa.";
    }

    public int getUnidad() {
        return unidad;
    }
}
