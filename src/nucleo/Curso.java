package nucleo;

import analisis.Alertable;
import java.util.ArrayList;

public class Curso implements Alertable {

    public static final int CREDITOS_POR_DEFECTO = 3;

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

    public double calcularPuntajeAcumulado() {
        double acumulado = 0;
        for (Evaluacion e : evaluaciones) {
            if (e.estaRendida()) {
                acumulado += e.calcularAporte();
            }
        }
        return acumulado;
    }

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

    public double calcularPromedioActual() {
        double pesoRendido = calcularPesoRendido();
        if (pesoRendido == 0) {
            return 0;
        }
        return calcularPuntajeAcumulado() / (pesoRendido / 100.0);
    }

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
