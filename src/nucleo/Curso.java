package nucleo;

import analisis.Alertable;
import java.util.ArrayList;

public class Curso implements Alertable {
    public static final int CREDITOS_POR_DEFECTO = 3;
    public static final int CREDITOS_MINIMOS = 1;
    public static final int CREDITOS_MAXIMOS = 8;
    public static final double PESO_TOTAL = 100;
    public static final int LARGO_MAXIMO_CODIGO = 10;
    public static final int LARGO_MAXIMO_NOMBRE = 50;

    private static final double UMBRAL_RIESGO_ALTO = 16;
    private static final double TOLERANCIA = 0.001;

    private String codigo;
    private String nombre;
    private int creditos;
    private ArrayList<Evaluacion> evaluaciones;

    public Curso(String codigo, String nombre) throws DatoInvalidoException {
        this(codigo, nombre, CREDITOS_POR_DEFECTO);
    }

    public Curso(String codigo, String nombre, int creditos) throws DatoInvalidoException {
        this.codigo = Evaluacion.validarTexto(codigo, "código del curso", LARGO_MAXIMO_CODIGO).toUpperCase();
        setNombre(nombre);
        setCreditos(creditos);
        this.evaluaciones = new ArrayList<>();
    }

    public void agregarEvaluacion(Evaluacion evaluacion) throws DatoInvalidoException {
        if (evaluacion == null) {
            throw new DatoInvalidoException("No se recibió ninguna evaluación.");
        }
        if (buscarEvaluacion(evaluacion.getCodigo()) != null) {
            throw new DatoInvalidoException("Ya existe una evaluación con el código "
                    + evaluacion.getCodigo() + " en " + nombre + ".");
        }
        if (evaluacion.getPeso() > calcularPesoDisponible() + TOLERANCIA) {
            throw new DatoInvalidoException("El peso de " + evaluacion.getCodigo() + " ("
                    + Evaluacion.formatearPeso(evaluacion.getPeso()) + " %) supera lo que queda libre en "
                    + nombre + " (" + Evaluacion.formatearPeso(calcularPesoDisponible()) + " %).");
        }
        evaluaciones.add(evaluacion);
    }

    public boolean eliminarEvaluacion(String codigoEvaluacion) {
        Evaluacion e = buscarEvaluacion(codigoEvaluacion);
        if (e == null) {
            return false;
        }
        evaluaciones.remove(e);
        return true;
    }

    public Evaluacion buscarEvaluacion(String codigoEvaluacion) {
        for (Evaluacion e : evaluaciones) {
            if (e.getCodigo().equalsIgnoreCase(codigoEvaluacion.trim())) {
                return e;
            }
        }
        return null;
    }

    public void registrarNota(String codigoEvaluacion, double nota) throws DatoInvalidoException {
        registrarNota(codigoEvaluacion, nota, "");
    }

    public void registrarNota(String codigoEvaluacion, double nota, String observacion)
            throws DatoInvalidoException {
        Evaluacion e = buscarEvaluacion(codigoEvaluacion);
        if (e == null) {
            throw new DatoInvalidoException("No existe la evaluación " + codigoEvaluacion
                    + " en " + nombre + ".");
        }
        e.registrarNota(nota, observacion);
    }

    public double calcularSumaPesos() {
        double suma = 0;
        for (Evaluacion e : evaluaciones) {
            suma += e.getPeso();
        }
        return suma;
    }

    public double calcularPesoDisponible() {
        return Math.max(0, PESO_TOTAL - calcularSumaPesos());
    }

    public boolean pesosCompletos() {
        return Math.abs(calcularSumaPesos() - PESO_TOTAL) < TOLERANCIA;
    }

    public double calcularPesoRendido() {
        double rendido = 0;
        for (Evaluacion e : evaluaciones) {
            if (e.estaRendida()) {
                rendido += e.getPeso();
            }
        }
        return rendido;
    }

    public double calcularPesoPendiente() {
        return PESO_TOTAL - calcularPesoRendido();
    }

    public boolean tieneNotas() {
        return calcularPesoRendido() > 0;
    }

    public boolean estaTerminado() {
        return pesosCompletos() && obtenerEvaluacionesPendientes().isEmpty();
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
        if (pendiente < TOLERANCIA) {
            return (acumulado + TOLERANCIA >= notaObjetivo) ? 0 : Calificacion.NOTA_MAXIMA + 1;
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
        return !obtenerNivelRiesgo().equals(RIESGO_BAJO);
    }

    @Override
    public String obtenerNivelRiesgo() {
        double necesaria = calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
        if (necesaria > UMBRAL_RIESGO_ALTO) {
            return RIESGO_ALTO;
        }
        if (tieneNotas() && calcularPromedioActual() < Calificacion.NOTA_APROBATORIA) {
            return RIESGO_MEDIO;
        }
        return RIESGO_BAJO;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws DatoInvalidoException {
        this.nombre = Evaluacion.validarTexto(nombre, "nombre del curso", LARGO_MAXIMO_NOMBRE);
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) throws DatoInvalidoException {
        if (creditos < CREDITOS_MINIMOS || creditos > CREDITOS_MAXIMOS) {
            throw new DatoInvalidoException("Los créditos deben estar entre "
                    + CREDITOS_MINIMOS + " y " + CREDITOS_MAXIMOS + ".");
        }
        this.creditos = creditos;
    }

    public ArrayList<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
