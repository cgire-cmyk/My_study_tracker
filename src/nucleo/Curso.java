package nucleo;

import analisis.Alertable;
import java.util.ArrayList;

/**
 * Curso matriculado con sus evaluaciones (composicion).
 * Implementa Alertable: sabe calcular su propio nivel de riesgo.
 * Modulo 1 - Nucleo academico (Integrante 1).
 */
public class Curso implements Alertable {

    public static final int CREDITOS_POR_DEFECTO = 3;

    // Umbral de nota necesaria a partir del cual el riesgo es ALTO
    private static final double UMBRAL_RIESGO_ALTO = 16;

    private String codigo;
    private String nombre;
    private int creditos;
    private ArrayList<Evaluacion> evaluaciones;

    public Curso(String codigo, String nombre) {
        this(codigo, nombre, CREDITOS_POR_DEFECTO);
    }

    public Curso(String codigo, String nombre, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = (creditos > 0) ? creditos : CREDITOS_POR_DEFECTO;
        this.evaluaciones = new ArrayList<>();
    }

    /** Agrega una evaluacion si su codigo no se repite y la suma de pesos no pasa de 100. */
    public boolean agregarEvaluacion(Evaluacion evaluacion) {
        if (evaluacion == null || evaluacion.getPeso() <= 0) {
            return false;
        }
        if (buscarEvaluacion(evaluacion.getCodigo()) != null) {
            return false;
        }
        if (calcularSumaPesos() + evaluacion.getPeso() > 100 + 0.001) {
            return false;
        }
        evaluaciones.add(evaluacion);
        return true;
    }

    public Evaluacion buscarEvaluacion(String codigo) {
        for (Evaluacion e : evaluaciones) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                return e;
            }
        }
        return null;
    }

    public boolean registrarNota(String codigoEvaluacion, double nota) {
        Evaluacion e = buscarEvaluacion(codigoEvaluacion);
        if (e == null) {
            return false;
        }
        return e.registrarNota(nota);
    }

    public double calcularSumaPesos() {
        double suma = 0;
        for (Evaluacion e : evaluaciones) {
            suma += e.getPeso();
        }
        return suma;
    }

    public boolean pesosCompletos() {
        return Math.abs(calcularSumaPesos() - 100) < 0.001;
    }

    /** Suma de los aportes de las evaluaciones ya rendidas (puntos sobre 20). */
    public double calcularPuntajeAcumulado() {
        double acumulado = 0;
        for (Evaluacion e : evaluaciones) {
            if (e.estaRendida()) {
                acumulado += e.calcularAporte();
            }
        }
        return acumulado;
    }

    /** Suma de los pesos (%) de las evaluaciones que faltan rendir. */
    public double calcularPesoPendiente() {
        double pendiente = 0;
        for (Evaluacion e : evaluaciones) {
            if (!e.estaRendida()) {
                pendiente += e.getPeso();
            }
        }
        return pendiente;
    }

    private double calcularPesoRendido() {
        return calcularSumaPesos() - calcularPesoPendiente();
    }

    /** Promedio sobre 20 considerando solo lo ya rendido. 0 si aun no hay notas. */
    public double calcularPromedioActual() {
        double pesoRendido = calcularPesoRendido();
        if (pesoRendido == 0) {
            return 0;
        }
        return calcularPuntajeAcumulado() / (pesoRendido / 100.0);
    }

    /**
     * Nota uniforme que se necesita en todo lo pendiente para llegar a notaObjetivo.
     * notaNecesaria = (notaObjetivo - puntajeAcumulado) / (pesoPendiente / 100)
     * Devuelve 0 si la meta ya esta asegurada y un valor mayor a 20 si ya no es posible.
     */
    public double calcularNotaNecesaria(double notaObjetivo) {
        double acumulado = calcularPuntajeAcumulado();
        double pendiente = calcularPesoPendiente();
        if (pendiente == 0) {
            return (acumulado >= notaObjetivo) ? 0 : Calificacion.NOTA_MAXIMA + 1;
        }
        double necesaria = (notaObjetivo - acumulado) / (pendiente / 100.0);
        return Math.max(0, necesaria);
    }

    public ArrayList<Evaluacion> obtenerEvaluacionesPendientes() {
        ArrayList<Evaluacion> pendientes = new ArrayList<>();
        for (Evaluacion e : evaluaciones) {
            if (!e.estaRendida()) {
                pendientes.add(e);
            }
        }
        return pendientes;
    }

    @Override
    public boolean estaEnRiesgo() {
        return !obtenerNivelRiesgo().equals("BAJO");
    }

    /**
     * ALTO: necesita mas de 16 en lo pendiente o ya no puede aprobar.
     * MEDIO: su promedio actual esta por debajo de la nota aprobatoria.
     * BAJO: en los demas casos.
     */
    @Override
    public String obtenerNivelRiesgo() {
        double necesaria = calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        if (necesaria > UMBRAL_RIESGO_ALTO) {
            return "ALTO";
        }
        if (calcularPesoRendido() > 0 && calcularPromedioActual() < Calificacion.NOTA_APROBATORIA) {
            return "MEDIO";
        }
        return "BAJO";
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public ArrayList<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }
}
