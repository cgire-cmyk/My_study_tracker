package analisis;
import nucleo.Curso;

public class Alerta {

    private Curso curso;   // asociacion
    private String nivel;
    private String mensaje;

    public Alerta(Curso curso, String nivel, String mensaje) {
        this.curso = curso;
        this.nivel = nivel;
        this.mensaje = mensaje;
    }

    public Curso getCurso() {
        return curso;
    }

    public String getNivel() {
        return nivel;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return "[" + nivel + "] " + curso.getCodigo() + " - " + curso.getNombre() + ": " + mensaje;
    }
}
