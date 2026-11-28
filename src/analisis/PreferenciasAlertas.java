package analisis;

public class PreferenciasAlertas {
    private boolean incluirRiesgoMedio;
    private boolean mostrarCursosSinRiesgo;
    private boolean avisarAlRegistrarNota;

    public PreferenciasAlertas() {
        this(true, false, true);
    }

    public PreferenciasAlertas(boolean incluirRiesgoMedio, boolean mostrarCursosSinRiesgo,
                               boolean avisarAlRegistrarNota) {
        this.incluirRiesgoMedio = incluirRiesgoMedio;
        this.mostrarCursosSinRiesgo = mostrarCursosSinRiesgo;
        this.avisarAlRegistrarNota = avisarAlRegistrarNota;
    }

    public boolean debeMostrar(String nivel) {
        if (nivel.equals(Alertable.RIESGO_ALTO)) {
            return true;
        }
        if (nivel.equals(Alertable.RIESGO_MEDIO)) {
            return incluirRiesgoMedio;
        }
        return mostrarCursosSinRiesgo;
    }

    public boolean isIncluirRiesgoMedio() {
        return incluirRiesgoMedio;
    }

    public void setIncluirRiesgoMedio(boolean incluirRiesgoMedio) {
        this.incluirRiesgoMedio = incluirRiesgoMedio;
    }

    public boolean isMostrarCursosSinRiesgo() {
        return mostrarCursosSinRiesgo;
    }

    public void setMostrarCursosSinRiesgo(boolean mostrarCursosSinRiesgo) {
        this.mostrarCursosSinRiesgo = mostrarCursosSinRiesgo;
    }

    public boolean isAvisarAlRegistrarNota() {
        return avisarAlRegistrarNota;
    }

    public void setAvisarAlRegistrarNota(boolean avisarAlRegistrarNota) {
        this.avisarAlRegistrarNota = avisarAlRegistrarNota;
    }
}
