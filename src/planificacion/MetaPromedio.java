package planificacion;

import java.util.ArrayList;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.DatoInvalidoException;

public class MetaPromedio extends Meta {
    public static final String CLAVE_ARCHIVO = "META_PROMEDIO";

    public static final String TERCIO_SUPERIOR = "Tercio superior";
    public static final String QUINTO_SUPERIOR = "Quinto superior";
    public static final String BECA = "Beca";
    public static final double REFERENCIA_TERCIO = 13.0;
    public static final double REFERENCIA_QUINTO = 15.0;
    public static final double REFERENCIA_BECA = 16.0;

    private String categoria;
    private ArrayList<Curso> cursos;

    public MetaPromedio(String categoria, double notaObjetivo, ArrayList<Curso> cursos)
            throws DatoInvalidoException {
        super(categoria + " (promedio " + String.format("%.1f", notaObjetivo) + ")", notaObjetivo);
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new DatoInvalidoException("Escribe la categoría de la meta.");
        }
        this.categoria = categoria.trim();
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
    public String explicarCalculo() {
        int totalCreditos = 0;
        double puntosAcumulados = 0;
        double pesoPendiente = 0;
        for (Curso c : cursos) {
            totalCreditos += c.getCreditos();
            puntosAcumulados += c.getCreditos() * c.calcularPuntajeAcumulado();
            pesoPendiente += c.getCreditos() * c.calcularPesoPendiente() / 100.0;
        }
        String texto = "Cursos considerados: " + cursos.size() + " (" + totalCreditos + " créditos)\n"
                + "Puntos acumulados (créditos x puntaje): " + Calificacion.formatear(puntosAcumulados) + "\n"
                + "Peso pendiente ponderado por créditos: " + Calificacion.formatear(pesoPendiente) + "\n";
        if (pesoPendiente == 0) {
            return texto + "Ya no quedan evaluaciones pendientes en ningún curso.";
        }
        return texto + "Nota necesaria = (" + Calificacion.formatear(notaObjetivo) + " x " + totalCreditos
                + " - " + Calificacion.formatear(puntosAcumulados) + ") / " + Calificacion.formatear(pesoPendiente)
                + " = " + Calificacion.formatear((notaObjetivo * totalCreditos - puntosAcumulados) / pesoPendiente);
    }

    @Override
    protected boolean tieneDatosSuficientes() {
        return !cursos.isEmpty();
    }

    @Override
    public String getTipo() {
        return "Promedio general";
    }

    @Override
    public String[] obtenerDatosParaGuardar() {
        return new String[] {CLAVE_ARCHIVO, categoria, String.valueOf(notaObjetivo)};
    }

    public String getCategoria() {
        return categoria;
    }
}
