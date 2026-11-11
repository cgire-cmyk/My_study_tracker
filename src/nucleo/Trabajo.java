package nucleo;

/**
 * Trabajo individual o grupal. Modulo 1 - Nucleo academico (Integrante 1).
 */
public class Trabajo extends Evaluacion {

    private boolean esGrupal;

    public Trabajo(String codigo, String nombre, double peso) {
        this(codigo, nombre, peso, false);
    }

    public Trabajo(String codigo, String nombre, double peso, boolean esGrupal) {
        super(codigo, nombre, peso);
        this.esGrupal = esGrupal;
    }

    @Override
    public String getTipo() {
        return esGrupal ? "Trabajo grupal" : "Trabajo individual";
    }

    @Override
    public String generarRecomendacion(double notaNecesaria) {
        String base = esGrupal
                ? nombre + ": repartan tareas con fechas internas y revisen juntos antes de entregar"
                : nombre + ": divide el trabajo en avances diarios";
        if (notaNecesaria >= 14) {
            return base + "; revisa la rubrica punto por punto (necesitas "
                    + String.format("%.2f", notaNecesaria) + ").";
        }
        return base + ".";
    }

    public boolean isEsGrupal() {
        return esGrupal;
    }
}
