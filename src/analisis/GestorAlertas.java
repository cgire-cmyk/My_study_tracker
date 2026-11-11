package analisis;

import java.util.ArrayList;
import nucleo.Calificacion;
import nucleo.Curso;

public class GestorAlertas {

    private ArrayList<Alerta> alertas;

    public GestorAlertas() {
        this.alertas = new ArrayList<>();
    }

    public ArrayList<Alerta> generarAlertas(ArrayList<Curso> cursos) {
        alertas.clear();
        for (Curso curso : cursos) {
            Alertable alertable = curso;   // se usa el curso a traves de la interfaz
            if (alertable.estaEnRiesgo()) {
                String nivel = alertable.obtenerNivelRiesgo();
                alertas.add(new Alerta(curso, nivel, construirMensaje(curso, nivel)));
            }
        }
        return alertas;
    }

    private String construirMensaje(Curso curso, String nivel) {
        double necesaria = curso.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        if (necesaria > Calificacion.NOTA_MAXIMA) {
            return "ya no es posible aprobar con las evaluaciones registradas.";
        }
        String texto = "necesitas " + String.format("%.2f", necesaria)
                + " en lo que falta rendir para aprobar";
        if (nivel.equals("MEDIO")) {
            texto += " (promedio actual " + String.format("%.2f", curso.calcularPromedioActual()) + ")";
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

