package planificacion;

import nucleo.Curso;
import nucleo.DatoInvalidoException;
import nucleo.ExamenParcial;
import nucleo.PracticaCalificada;
import nucleo.Trabajo;

public class DatosDePrueba {
    public static Estudiante crearEstudiante() throws DatoInvalidoException {
        Estudiante est = new Estudiante("U2024-0001", "Ana Torres Quispe", "Ingeniería de Sistemas");

        Curso poo = new Curso("POO", "Programación Orientada a Objetos", 4);
        poo.agregarEvaluacion(new PracticaCalificada("PC1", "Práctica 1", 20, 1));
        poo.agregarEvaluacion(new PracticaCalificada("PC2", "Práctica 2", 20, 2));
        poo.agregarEvaluacion(new ExamenParcial("EP", "Examen parcial", 60, 1));
        poo.registrarNota("PC1", 12);
        poo.registrarNota("PC2", 10);

        Curso calculo = new Curso("MAT1", "Cálculo I", 5);
        calculo.agregarEvaluacion(new PracticaCalificada("PC1", "Práctica 1", 15, 1));
        calculo.agregarEvaluacion(new PracticaCalificada("PC2", "Práctica 2", 15, 2));
        calculo.agregarEvaluacion(new Trabajo("TF", "Trabajo final", 30));
        calculo.agregarEvaluacion(new ExamenParcial("EF", "Examen final", 40, 2));
        calculo.registrarNota("PC1", 8);
        calculo.registrarNota("PC2", 7);
        calculo.registrarNota("TF", 9);

        Curso fisica = new Curso("FIS1", "Física I", 3);
        fisica.agregarEvaluacion(new PracticaCalificada("PC1", "Práctica 1", 25, 1));
        fisica.agregarEvaluacion(new Trabajo("TG", "Trabajo de laboratorio", 25, true));
        fisica.agregarEvaluacion(new ExamenParcial("EP", "Examen parcial", 50, 1));
        fisica.registrarNota("PC1", 4);
        fisica.registrarNota("TG", 5);

        Curso comunicacion = new Curso("COM1", "Comunicación");
        comunicacion.agregarEvaluacion(new PracticaCalificada("PC1", "Práctica 1", 30, 1));
        comunicacion.agregarEvaluacion(new Trabajo("TG", "Ensayo grupal", 30, true));
        comunicacion.agregarEvaluacion(new ExamenParcial("EP", "Examen parcial", 40, 1));
        comunicacion.registrarNota("PC1", 16);
        comunicacion.registrarNota("TG", 17, "Buena redacción");

        est.agregarCurso(poo);
        est.agregarCurso(calculo);
        est.agregarCurso(fisica);
        est.agregarCurso(comunicacion);

        est.agregarMeta(new MetaAprobacion(poo, 17));
        est.agregarMeta(new MetaAprobacion(fisica));
        est.agregarMeta(new MetaPromedio(MetaPromedio.TERCIO_SUPERIOR, MetaPromedio.REFERENCIA_TERCIO,
                est.getCursos()));
        est.agregarMeta(new MetaPromedio(MetaPromedio.QUINTO_SUPERIOR, MetaPromedio.REFERENCIA_QUINTO,
                est.getCursos()));

        for (int i = 0; i < 5; i++) {
            est.getHorario().definirDisponibilidad(HorarioEstudio.DIAS[i], 3);
        }
        est.getHorario().definirDisponibilidad("SABADO", 4);
        est.getHorario().definirDisponibilidad("DOMINGO", 0);

        return est;
    }

    public static Estudiante crearEstudianteVacio() throws DatoInvalidoException {
        return new Estudiante("U0000", "Estudiante nuevo");
    }
}
