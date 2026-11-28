package planificacion;

import java.util.ArrayList;
import nucleo.Curso;
import nucleo.DatoInvalidoException;

public class Estudiante {
    public static final String CARRERA_POR_DEFECTO = "Sin especificar";

    private String codigo;
    private String nombres;
    private String carrera;
    private ArrayList<Curso> cursos;
    private ArrayList<Meta> metas;
    private HorarioEstudio horario;

    public Estudiante(String codigo, String nombres) throws DatoInvalidoException {
        this(codigo, nombres, CARRERA_POR_DEFECTO);
    }

    public Estudiante(String codigo, String nombres, String carrera) throws DatoInvalidoException {
        setCodigo(codigo);
        setNombres(nombres);
        setCarrera(carrera);
        this.cursos = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.horario = new HorarioEstudio();
    }

    public void agregarCurso(Curso curso) throws DatoInvalidoException {
        if (curso == null) {
            throw new DatoInvalidoException("No se recibió ningún curso.");
        }
        if (buscarCurso(curso.getCodigo()) != null) {
            throw new DatoInvalidoException("Ya tienes un curso con el código " + curso.getCodigo() + ".");
        }
        cursos.add(curso);
    }

    public boolean eliminarCurso(String codigoCurso) {
        Curso curso = buscarCurso(codigoCurso);
        if (curso == null) {
            return false;
        }
        for (int i = metas.size() - 1; i >= 0; i--) {
            if (metas.get(i).usaCurso(curso)) {
                metas.remove(i);
            }
        }
        cursos.remove(curso);
        return true;
    }

    public Curso buscarCurso(String codigoCurso) {
        for (Curso c : cursos) {
            if (c.getCodigo().equalsIgnoreCase(codigoCurso.trim())) {
                return c;
            }
        }
        return null;
    }

    public void agregarMeta(Meta meta) throws DatoInvalidoException {
        if (meta == null) {
            throw new DatoInvalidoException("No se recibió ninguna meta.");
        }
        for (Meta m : metas) {
            if (m.getNombre().equalsIgnoreCase(meta.getNombre())) {
                throw new DatoInvalidoException("Ya tienes registrada la meta \"" + meta.getNombre() + "\".");
            }
        }
        metas.add(meta);
    }

    public boolean eliminarMeta(Meta meta) {
        return metas.remove(meta);
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
            if (c.tieneNotas()) {
                suma += c.calcularPromedioActual() * c.getCreditos();
                creditos += c.getCreditos();
            }
        }
        return (creditos == 0) ? 0 : suma / creditos;
    }

    public int contarCursosEnRiesgo() {
        int contador = 0;
        for (Curso c : cursos) {
            if (c.estaEnRiesgo()) {
                contador++;
            }
        }
        return contador;
    }

    private static String validarTexto(String texto, String campo, int largoMaximo)
            throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException("El campo " + campo + " no puede estar vacío.");
        }
        if (texto.trim().length() > largoMaximo) {
            throw new DatoInvalidoException("El campo " + campo + " admite como máximo "
                    + largoMaximo + " caracteres.");
        }
        return texto.trim();
    }

    public void actualizarDatos(String nuevoCodigo, String nuevosNombres, String nuevaCarrera)
            throws DatoInvalidoException {
        String c = validarTexto(nuevoCodigo, "código", 15).toUpperCase();
        String n = validarTexto(nuevosNombres, "nombres", 60);
        setCarrera(nuevaCarrera);
        this.codigo = c;
        this.nombres = n;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) throws DatoInvalidoException {
        this.codigo = validarTexto(codigo, "código", 15).toUpperCase();
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) throws DatoInvalidoException {
        this.nombres = validarTexto(nombres, "nombres", 60);
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) throws DatoInvalidoException {
        if (carrera == null || carrera.trim().isEmpty()) {
            this.carrera = CARRERA_POR_DEFECTO;
        } else {
            this.carrera = validarTexto(carrera, "carrera", 60);
        }
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
