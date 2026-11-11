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
            recomendarPorCurso(curso);
        }
        recomendarPorMetas(estudiante.getMetas());
        return recomendaciones;
    }

    private void recomendarPorCurso(Curso curso) {
        double necesaria = curso.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        ArrayList<Evaluacion> pendientes = curso.obtenerEvaluacionesPendientes();
        String prefijo = "[" + curso.getCodigo() + "] ";

        if (pendientes.isEmpty()) {
            recomendaciones.add(prefijo + "Curso sin evaluaciones pendientes.");
            return;
        }
        for (Evaluacion evaluacion : pendientes) {
            Recomendable recomendable = evaluacion;   // polimorfismo a traves de la interfaz
            recomendaciones.add(prefijo + recomendable.generarRecomendacion(necesaria));
        }
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