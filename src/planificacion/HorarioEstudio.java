package planificacion;

import java.util.ArrayList;
import java.util.HashMap;
import nucleo.Curso;

    public static final String[] DIAS = {
        "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"
    };

    private static final int MAX_HORAS_DIA = 12;

    private HashMap<String, Integer> horasDisponibles;
    private ArrayList<BloqueEstudio> bloques;

    public HorarioEstudio() {
        this.horasDisponibles = new HashMap<>();
        this.bloques = new ArrayList<>();
        for (String dia : DIAS) {
            horasDisponibles.put(dia, 0);
        }
    }

    public boolean definirDisponibilidad(String dia, int horas) {
        String clave = dia.trim().toUpperCase();
        if (!horasDisponibles.containsKey(clave) || horas < 0 || horas > MAX_HORAS_DIA) {
            return false;
        }
        horasDisponibles.put(clave, horas);
        return true;
    }

    public void generarHorario(ArrayList<Curso> cursos) {
        bloques.clear();
        int[] libres = new int[DIAS.length];
        for (int i = 0; i < DIAS.length; i++) {
            libres[i] = horasDisponibles.get(DIAS[i]);
        }
        int[][] asignadas = new int[cursos.size()][DIAS.length];
        String[] prioridades = {"ALTO", "MEDIO", "BAJO"};
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

    private int calcularHorasRecomendadas(Curso curso) {
        int horas = curso.getCreditos();
        String nivel = curso.obtenerNivelRiesgo();
        if (nivel.equals("ALTO")) {
            horas += 3;
        } else if (nivel.equals("MEDIO")) {
            horas += 1;
        }
        return horas;
    }

    public ArrayList<BloqueEstudio> getBloques() {
        return bloques;
    }
}
