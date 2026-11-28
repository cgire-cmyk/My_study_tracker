package analisis;

public interface Alertable {
    String RIESGO_ALTO = "ALTO";
    String RIESGO_MEDIO = "MEDIO";
    String RIESGO_BAJO = "BAJO";

    boolean estaEnRiesgo();

    String obtenerNivelRiesgo();
}
