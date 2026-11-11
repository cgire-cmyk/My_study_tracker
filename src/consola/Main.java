package consola;

import nucleo.Curso;
import nucleo.ExamenParcial;
import nucleo.PracticaCalificada;
import nucleo.Trabajo;
import planificacion.Estudiante;
import planificacion.HorarioEstudio;
import planificacion.MetaAprobacion;
import planificacion.MetaPromedio;

public class Main {

    public static void main(String[] args) {
        Estudiante estudiante;
        if (args.length > 0 && args[0].equals("--vacio")) {
            estudiante = new Estudiante("U0000", "Estudiante nuevo");
        } else {
            estudiante = cargarDatosDePrueba();
        }
        InterfazConsola consola = new InterfazConsola(estudiante);
        consola.iniciar();
    }

    /** Datos ficticios para demostrar todos los casos: riesgo ALTO, MEDIO, BAJO y metas imposibles. */
    private static Estudiante cargarDatosDePrueba() {
        Estudiante est = new Estudiante("U2024-0001", "Ana Torres Quispe", "Ingenieria de Sistemas");

        Curso poo = new Curso("POO", "Programacion Orientada a Objetos", 4);
        poo.agregarEvaluacion(new PracticaCalificada("PC1", "Practica 1", 20, 1));
        poo.agregarEvaluacion(new PracticaCalificada("PC2", "Practica 2", 20, 2));
        poo.agregarEvaluacion(new ExamenParcial("EP", "Examen parcial", 60, 1));
        poo.registrarNota("PC1", 12);
        poo.registrarNota("PC2", 10);

        Curso calculo = new Curso("MAT1", "Calculo I", 5);
        calculo.agregarEvaluacion(new PracticaCalificada("PC1", "Practica 1", 15, 1));
        calculo.agregarEvaluacion(new PracticaCalificada("PC2", "Practica 2", 15, 2));
        calculo.agregarEvaluacion(new Trabajo("TF", "Trabajo final", 30));
        calculo.agregarEvaluacion(new ExamenParcial("EF", "Examen final", 40, 2));
        calculo.registrarNota("PC1", 8);
        calculo.registrarNota("PC2", 7);
        calculo.registrarNota("TF", 9);

        Curso fisica = new Curso("FIS1", "Fisica I", 3);
        fisica.agregarEvaluacion(new PracticaCalificada("PC1", "Practica 1", 25, 1));
        fisica.agregarEvaluacion(new Trabajo("TG", "Trabajo de laboratorio", 25, true));
        fisica.agregarEvaluacion(new ExamenParcial("EP", "Examen parcial", 50, 1));
        fisica.registrarNota("PC1", 4);
        fisica.registrarNota("TG", 5);

        Curso comunicacion = new Curso("COM1", "Comunicacion");
        comunicacion.agregarEvaluacion(new PracticaCalificada("PC1", "Practica 1", 30, 1));
        comunicacion.agregarEvaluacion(new Trabajo("TG", "Ensayo grupal", 30, true));
        comunicacion.agregarEvaluacion(new ExamenParcial("EP", "Examen parcial", 40, 1));
        comunicacion.registrarNota("PC1", 16);
        comunicacion.getEvaluaciones().get(1).registrarNota(17, "Buena redaccion");

        est.agregarCurso(poo);
        est.agregarCurso(calculo);
        est.agregarCurso(fisica);
        est.agregarCurso(comunicacion);

        est.agregarMeta(new MetaAprobacion(poo, 17));
        est.agregarMeta(new MetaAprobacion(fisica));
        est.agregarMeta(new MetaPromedio("Tercio superior", 13.0, est.getCursos()));
        est.agregarMeta(new MetaPromedio("Quinto superior", 15.0, est.getCursos()));

        for (int i = 0; i < 5; i++) {
            est.getHorario().definirDisponibilidad(HorarioEstudio.DIAS[i], 3);
        }
        est.getHorario().definirDisponibilidad("SABADO", 4);
        est.getHorario().definirDisponibilidad("DOMINGO", 0);

        return est;
    }
}

