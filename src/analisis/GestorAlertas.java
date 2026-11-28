package analisis;

import java.util.ArrayList;
import nucleo.Calificacion;
import nucleo.Curso;

public class GestorAlertas {
    private static final String[] ORDEN_NIVELES = {
        Alertable.RIESGO_ALTO, Alertable.RIESGO_MEDIO, Alertable.RIESGO_BAJO
    };

    private ArrayList<Alerta> alertas;

    public GestorAlertas() {
        this.alertas = new ArrayList<>();
    }

    public ArrayList<Alerta> generarAlertas(ArrayList<Curso> cursos) {
        return generarAlertas(cursos, new PreferenciasAlertas());
    }

    public ArrayList<Alerta> generarAlertas(ArrayList<Curso> cursos, PreferenciasAlertas preferencias) {
        alertas.clear();
        for (String nivel : ORDEN_NIVELES) {
            if (!preferencias.debeMostrar(nivel)) {
                continue;
            }
            for (Curso curso : cursos) {
                Alertable alertable = curso;
                if (alertable.obtenerNivelRiesgo().equals(nivel)) {
                    alertas.add(evaluarCurso(curso));
                }
            }
        }
        return alertas;
    }

    public Alerta evaluarCurso(Curso curso) {
        String nivel = curso.obtenerNivelRiesgo();
        return new Alerta(curso, nivel, construirMensaje(curso, nivel));
    }

    private String construirMensaje(Curso curso, String nivel) {
        double necesaria = curso.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        if (necesaria > Calificacion.NOTA_MAXIMA) {
            return "ya no es posible aprobar con las evaluaciones registradas.";
        }
        if (nivel.equals(Alertable.RIESGO_BAJO)) {
            if (!curso.tieneNotas()) {
                return "aún no tiene notas registradas.";
            }
            if (necesaria == 0) {
                return "¡aprobado asegurado! Promedio actual "
                        + Calificacion.formatear(curso.calcularPromedioActual()) + ".";
            }
            return "vas bien: promedio actual " + Calificacion.formatear(curso.calcularPromedioActual())
                    + "; necesitas " + Calificacion.formatear(necesaria) + " en lo pendiente.";
        }
        String texto = "necesitas " + Calificacion.formatear(necesaria)
                + " en lo que falta rendir para aprobar";
        if (nivel.equals(Alertable.RIESGO_MEDIO)) {
            texto += " (promedio actual " + Calificacion.formatear(curso.calcularPromedioActual()) + ")";
        }
        return texto + ".";
    }

    public int contarAlertasPorNivel(String nivel) {
        int contador = 0;
        for (Alerta a : alertas) {
            if (a.getNivel().equalsIgnoreCase(nivel)) {
                contador++;
            }
        }
        return contador;
    }

    public ArrayList<Alerta> getAlertas() {
        return alertas;
    }
}
