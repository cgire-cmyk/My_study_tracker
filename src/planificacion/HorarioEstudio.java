package planificacion;

import analisis.Alertable;
import java.util.ArrayList;
import java.util.HashMap;
import nucleo.Curso;
import nucleo.DatoInvalidoException;

public class HorarioEstudio {
    public static final String[] DIAS = {
        "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"
    };
    public static final String[] DIAS_VISIBLES = {
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    };

    public static final int MAX_HORAS_DIA = 12;

    private HashMap<String, Integer> horasDisponibles;
    private ArrayList<BloqueEstudio> bloques;
    private int horasSinAsignar;

    public HorarioEstudio() {
        this.horasDisponibles = new HashMap<>();
        this.bloques = new ArrayList<>();
        this.horasSinAsignar = 0;
        for (String dia : DIAS) {
            horasDisponibles.put(dia, 0);
        }
    }

    public void definirDisponibilidad(String dia, int horas) throws DatoInvalidoException {
        String clave = dia.trim().toUpperCase();
        if (!horasDisponibles.containsKey(clave)) {
            throw new DatoInvalidoException("El día " + dia + " no existe.");
        }
        if (horas < 0 || horas > MAX_HORAS_DIA) {
            throw new DatoInvalidoException("Las horas del " + dia.toLowerCase()
                    + " deben estar entre 0 y " + MAX_HORAS_DIA + ".");
        }
        horasDisponibles.put(clave, horas);
    }

    public int getHorasDisponibles(String dia) {
        Integer horas = horasDisponibles.get(dia.trim().toUpperCase());
        return (horas == null) ? 0 : horas;
    }

    public int calcularTotalDisponible() {
        int total = 0;
        for (String dia : DIAS) {
            total += horasDisponibles.get(dia);
        }
        return total;
    }

    public void generarHorario(ArrayList<Curso> cursos) {
        bloques.clear();
        horasSinAsignar = 0;
        int[] libres = new int[DIAS.length];
        for (int i = 0; i < DIAS.length; i++) {
            libres[i] = horasDisponibles.get(DIAS[i]);
        }
        int[][] asignadas = new int[cursos.size()][DIAS.length];
        String[] prioridades = {Alertable.RIESGO_ALTO, Alertable.RIESGO_MEDIO, Alertable.RIESGO_BAJO};
        int diaActual = 0;

        for (String nivel : prioridades) {
            for (int c = 0; c < cursos.size(); c++) {
                Curso curso = cursos.get(c);
                if (!curso.obtenerNivelRiesgo().equals(nivel)) {
                    continue;
                }
                int porAsignar = calcularHorasRecomendadas(curso);
                int diasRevisados = 0;
                while (porAsignar > 0 && diasRevisados < DIAS.length) {
                    if (libres[diaActual] > 0) {
                        libres[diaActual]--;
                        asignadas[c][diaActual]++;
                        porAsignar--;
                        diasRevisados = 0;
                    } else {
                        diasRevisados++;
                    }
                    diaActual = (diaActual + 1) % DIAS.length;
                }
                horasSinAsignar += porAsignar;
            }
        }

        for (int d = 0; d < DIAS.length; d++) {
            for (int c = 0; c < cursos.size(); c++) {
                if (asignadas[c][d] > 0) {
                    bloques.add(new BloqueEstudio(DIAS[d], asignadas[c][d], cursos.get(c)));
                }
            }
        }
    }

    public ArrayList<BloqueEstudio> obtenerBloquesPorDia(String dia) {
        ArrayList<BloqueEstudio> resultado = new ArrayList<>();
        for (BloqueEstudio b : bloques) {
            if (b.getDia().equalsIgnoreCase(dia)) {
                resultado.add(b);
            }
        }
        return resultado;
    }

    public int calcularTotalHoras() {
        int total = 0;
        for (BloqueEstudio b : bloques) {
            total += b.getHoras();
        }
        return total;
    }

    public int calcularHorasAsignadas(Curso curso) {
        int total = 0;
        for (BloqueEstudio b : bloques) {
            if (b.getCurso() == curso) {
                total += b.getHoras();
            }
        }
        return total;
    }

    public int calcularHorasRecomendadas(Curso curso) {
        if (curso.estaTerminado()) {
            return 0;
        }
        int horas = curso.getCreditos();
        String nivel = curso.obtenerNivelRiesgo();
        if (nivel.equals(Alertable.RIESGO_ALTO)) {
            horas += 3;
        } else if (nivel.equals(Alertable.RIESGO_MEDIO)) {
            horas += 1;
        }
        return horas;
    }

    public int getHorasSinAsignar() {
        return horasSinAsignar;
    }

    public ArrayList<BloqueEstudio> getBloques() {
        return bloques;
    }
}
