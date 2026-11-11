package planificacion;

import java.util.ArrayList;
import nucleo.Calificacion;
import nucleo.Curso;

public class MetaPromedio extends Meta {

    private String categoria;
    private ArrayList<Curso> cursos; 
    
    public MetaPromedio(String categoria, double notaObjetivo, ArrayList<Curso> cursos) {
        super(categoria + " (promedio " + String.format("%.1f", notaObjetivo) + ")", notaObjetivo);
        this.categoria = categoria;
        this.cursos = cursos;
    }

    @Override
    public double calcularNotaMinimaRequerida() {
        double totalCreditos = 0;
        double puntosAcumulados = 0;
        double pesoPendiente = 0;
        for (Curso c : cursos) {
            totalCreditos += c.getCreditos();
            puntosAcumulados += c.getCreditos() * c.calcularPuntajeAcumulado();
            pesoPendiente += c.getCreditos() * c.calcularPesoPendiente() / 100.0;
        }
        if (totalCreditos == 0) {
            return 0;
        }
        double faltan = notaObjetivo * totalCreditos - puntosAcumulados;
        if (pesoPendiente == 0) {
            return (faltan <= 0) ? 0 : Calificacion.NOTA_MAXIMA + 1;
        }
        return Math.max(0, faltan / pesoPendiente);
    }

    @Override
    public String getTipo() {
        return "Promedio general";
    }

    public String getCategoria() {
        return categoria;
    }
}
