package nucleo;

public class Trabajo extends Evaluacion {
    private boolean esGrupal;

    public Trabajo(String codigo, String nombre, double peso) throws DatoInvalidoException {
        this(codigo, nombre, peso, false);
    }

    public Trabajo(String codigo, String nombre, double peso, boolean esGrupal)
            throws DatoInvalidoException {
        super(codigo, nombre, peso);
        this.esGrupal = esGrupal;
    }

    @Override
    public String getTipo() {
        return esGrupal ? "Trabajo grupal" : "Trabajo individual";
    }

    @Override
    public String getClaveTipo() {
        return "TR";
    }

    @Override
    public String getDatoExtra() {
        return String.valueOf(esGrupal);
    }

    @Override
    public String generarRecomendacion(double notaNecesaria) {
        String base = esGrupal
                ? nombre + ": repartan tareas con fechas internas y revisen juntos antes de entregar"
                : nombre + ": divide el trabajo en avances diarios";
        if (notaNecesaria > Calificacion.NOTA_MAXIMA) {
            return base + "; entrega un trabajo completo para subir lo máximo posible.";
        }
        if (notaNecesaria >= 14) {
            return base + "; revisa la rúbrica punto por punto (necesitas "
                    + Calificacion.formatear(notaNecesaria) + ").";
        }
        return base + ".";
    }

    public boolean isEsGrupal() {
        return esGrupal;
    }
}
