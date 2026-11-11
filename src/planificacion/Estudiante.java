package planificacion;

import java.util.ArrayList;
import nucleo.Curso;

public class Estudiante {

    private String codigo;
    private String nombres;
    private String carrera;
    private ArrayList<Curso> cursos;
    private ArrayList<Meta> metas;
    private HorarioEstudio horario;

    public Estudiante(String codigo, String nombres) {
        this(codigo, nombres, "Sin especificar");
    }

    public Estudiante(String codigo, String nombres, String carrera) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.carrera = carrera;
        this.cursos = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.horario = new HorarioEstudio();  
    }

    public boolean agregarCurso(Curso curso) {
        if (curso == null || buscarCurso(curso.getCodigo()) != null) {
            return false;
        }
        cursos.add(curso);
        return true;
    }

    public Curso buscarCurso(String codigo) {
        for (Curso c : cursos) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }

    public void agregarMeta(Meta meta) {
        if (meta != null) {
            metas.add(meta);
        }
    }

    public int calcularTotalCreditos() {
        int total = 0;
        for (Curso c : cursos) {
            total += c.getCreditos();
        }
        return total;
    }

    public double calcularPromedioPonderadoActual() {
        double suma = 0;
        int creditos = 0;
        for (Curso c : cursos) {
            boolean tieneNotas = c.calcularPesoPendiente() < c.calcularSumaPesos();
            if (tieneNotas) {
                suma += c.calcularPromedioActual() * c.getCreditos();
                creditos += c.getCreditos();
            }
        }
        return (creditos == 0) ? 0 : suma / creditos;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public String getCarrera() {
        return carrera;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public ArrayList<Meta> getMetas() {
        return metas;
    }

    public HorarioEstudio getHorario() {
        return horario;
    }
}
