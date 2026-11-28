package analisis;

import java.util.ArrayList;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.Evaluacion;
import planificacion.Estudiante;
import planificacion.Meta;

public class GestorRecomendaciones {
    private ArrayList<String> recomendaciones;

    public GestorRecomendaciones() {
        this.recomendaciones = new ArrayList<>();
    }

    public ArrayList<String> generarRecomendaciones(Estudiante estudiante) {
        recomendaciones.clear();
        for (Curso curso : estudiante.getCursos()) {
            recomendarPorCurso(curso, "[" + curso.getCodigo() + "] ");
        }
        recomendarPorMetas(estudiante.getMetas());
        return recomendaciones;
    }

    public ArrayList<String> generarRecomendaciones(Curso curso) {
        recomendaciones.clear();
        recomendarPorCurso(curso, "");
        return recomendaciones;
    }

    private void recomendarPorCurso(Curso curso, String prefijo) {
        double necesaria = curso.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        ArrayList<Evaluacion> pendientes = curso.obtenerEvaluacionesPendientes();

        recomendaciones.add(prefijo + obtenerConsejoGeneral(curso));
        if (pendientes.isEmpty()) {
            if (!curso.pesosCompletos()) {
                recomendaciones.add(prefijo + "Registra las evaluaciones que faltan (queda "
                        + Evaluacion.formatearPeso(curso.calcularPesoDisponible()) + " % sin asignar).");
            }
            return;
        }
        for (Evaluacion evaluacion : pendientes) {
            Recomendable recomendable = evaluacion;
            recomendaciones.add(prefijo + recomendable.generarRecomendacion(necesaria));
        }
    }

    private String obtenerConsejoGeneral(Curso curso) {
        if (curso.estaTerminado()) {
            return "Curso terminado con " + Calificacion.formatear(curso.calcularPuntajeAcumulado())
                    + ". " + (curso.calcularPuntajeAcumulado() >= Calificacion.NOTA_APROBATORIA
                    ? "¡Felicitaciones!" : "Revisa las fechas de examen sustitutorio o de aplazados.");
        }
        String nivel = curso.obtenerNivelRiesgo();
        if (nivel.equals(Alertable.RIESGO_ALTO)) {
            return "Riesgo ALTO: pide asesoría al docente esta semana y dale prioridad en tu horario.";
        }
        if (nivel.equals(Alertable.RIESGO_MEDIO)) {
            return "Riesgo MEDIO: tu promedio está por debajo de "
                    + Calificacion.NOTA_APROBATORIA + "; repasa los temas en los que fallaste.";
        }
        return "Vas bien: mantén un repaso semanal para no bajar el ritmo.";
    }

    private void recomendarPorMetas(ArrayList<Meta> metas) {
        for (Meta meta : metas) {
            recomendaciones.add("[Meta] " + meta.getNombre() + ": " + meta.obtenerEstado());
        }
    }

    public ArrayList<String> getRecomendaciones() {
        return recomendaciones;
    }
}
