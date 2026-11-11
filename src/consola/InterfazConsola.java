package consola;

import analisis.Alerta;
import analisis.GestorAlertas;
import analisis.GestorRecomendaciones;
import java.util.ArrayList;
import java.util.Scanner;
import nucleo.Calificacion;
import nucleo.Curso;
import nucleo.Evaluacion;
import nucleo.ExamenParcial;
import nucleo.PracticaCalificada;
import nucleo.Trabajo;
import planificacion.BloqueEstudio;
import planificacion.Estudiante;
import planificacion.HorarioEstudio;
import planificacion.Meta;
import planificacion.MetaAprobacion;
import planificacion.MetaPromedio;

public class InterfazConsola {

    private Estudiante estudiante;
    private GestorAlertas gestorAlertas;
    private GestorRecomendaciones gestorRecomendaciones;
    private Scanner scanner;

    public InterfazConsola(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.gestorAlertas = new GestorAlertas();
        this.gestorRecomendaciones = new GestorRecomendaciones();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("==============================================");
        System.out.println(" PLATAFORMA DE GESTION Y PREDICCION ACADEMICA");
        System.out.println("==============================================");
        System.out.println("Estudiante: " + estudiante.getNombres() + " (" + estudiante.getCodigo() + ")");
        System.out.println("Carrera:    " + estudiante.getCarrera());

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1: mostrarResumen(); break;
                case 2: registrarCurso(); break;
                case 3: registrarEvaluacion(); break;
                case 4: registrarNota(); break;
                case 5: registrarMeta(); break;
                case 6: mostrarMetas(); break;
                case 7: mostrarAlertas(); break;
                case 8: mostrarRecomendaciones(); break;
                case 9: mostrarHorario(); break;
                case 0: System.out.println("Hasta luego."); break;
                default: System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("------------------ MENU ------------------");
        System.out.println(" 1. Ver resumen de cursos");
        System.out.println(" 2. Registrar curso");
        System.out.println(" 3. Registrar evaluacion");
        System.out.println(" 4. Registrar nota");
        System.out.println(" 5. Registrar meta");
        System.out.println(" 6. Ver metas");
        System.out.println(" 7. Ver alertas de riesgo");
        System.out.println(" 8. Ver recomendaciones");
        System.out.println(" 9. Generar horario de estudio");
        System.out.println(" 0. Salir");
        System.out.println("------------------------------------------");
    }

    private void mostrarResumen() {
        ArrayList<Curso> cursos = estudiante.getCursos();
        if (cursos.isEmpty()) {
            System.out.println("Aun no hay cursos registrados.");
            return;
        }
        System.out.println();
        System.out.println("=== RESUMEN DE CURSOS ===");
        for (Curso c : cursos) {
            System.out.println();
            System.out.println(c.getCodigo() + " - " + c.getNombre() + " (" + c.getCreditos() + " creditos)");
            for (Evaluacion e : c.getEvaluaciones()) {
                String nota = e.estaRendida() ? String.format("%.2f", e.getNota()) : "pendiente";
                System.out.printf("   %-6s %-22s %-20s %5.1f%%  nota: %s%n",
                        e.getCodigo(), e.getNombre(), e.getTipo(), e.getPeso(), nota);
            }
            if (!c.pesosCompletos()) {
                System.out.printf("   AVISO: los pesos suman %.1f%%, deberian sumar 100%%.%n", c.calcularSumaPesos());
            }
            System.out.printf("   Puntaje acumulado: %.2f | Promedio actual: %.2f | Riesgo: %s%n",
                    c.calcularPuntajeAcumulado(), c.calcularPromedioActual(), c.obtenerNivelRiesgo());
            double necesaria = c.calcularNotaNecesaria(Calificacion.NOTA_APROBATORIA);
            if (necesaria > Calificacion.NOTA_MAXIMA) {
                System.out.println("   Para aprobar: ya no es posible.");
            } else if (necesaria == 0) {
                System.out.println("   Para aprobar: ya esta asegurado.");
            } else {
                System.out.printf("   Para aprobar necesitas %.2f en lo pendiente.%n", necesaria);
            }
        }
        System.out.println();
        System.out.printf("Total de creditos: %d | Promedio ponderado actual: %.2f%n",
                estudiante.calcularTotalCreditos(), estudiante.calcularPromedioPonderadoActual());
    }

    private void registrarCurso() {
        String codigo = leerTexto("Codigo del curso: ");
        String nombre = leerTexto("Nombre del curso: ");
        int creditos = leerEntero("Creditos (0 = usar " + Curso.CREDITOS_POR_DEFECTO + "): ");
        Curso curso = (creditos <= 0) ? new Curso(codigo, nombre) : new Curso(codigo, nombre, creditos);
        if (estudiante.agregarCurso(curso)) {
            System.out.println("Curso registrado.");
        } else {
            System.out.println("No se pudo registrar: ya existe un curso con ese codigo.");
        }
    }

    private void registrarEvaluacion() {
        Curso curso = pedirCurso();
        if (curso == null) {
            return;
        }
        System.out.println("Tipo: 1) Practica calificada  2) Trabajo  3) Examen parcial");
        int tipo = leerEntero("Elige el tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo no valido.");
            return;
        }
        String codigo = leerTexto("Codigo de la evaluacion (ej. PC1): ");
        String nombre = leerTexto("Nombre de la evaluacion: ");
        int peso = leerEntero("Peso en % (disponible: " + (100 - curso.calcularSumaPesos()) + "): ");

        Evaluacion evaluacion;
        if (tipo == 1) {
            int numero = leerEntero("Numero de practica: ");
            evaluacion = new PracticaCalificada(codigo, nombre, peso, numero);
        } else if (tipo == 2) {
            boolean grupal = leerTexto("Es grupal? (s/n): ").equalsIgnoreCase("s");
            evaluacion = new Trabajo(codigo, nombre, peso, grupal);
        } else {
            int unidad = leerEntero("Unidad que evalua: ");
            evaluacion = new ExamenParcial(codigo, nombre, peso, unidad);
        }

        if (curso.agregarEvaluacion(evaluacion)) {
            System.out.println("Evaluacion registrada. Pesos del curso: " + curso.calcularSumaPesos() + "%");
        } else {
            System.out.println("No se pudo registrar: codigo repetido, peso invalido o la suma pasa de 100%.");
        }
    }

    private void registrarNota() {
        Curso curso = pedirCurso();
        if (curso == null) {
            return;
        }
        String codigo = leerTexto("Codigo de la evaluacion: ");
        Evaluacion evaluacion = curso.buscarEvaluacion(codigo);
        if (evaluacion == null) {
            System.out.println("No existe esa evaluacion en el curso.");
            return;
        }
        double nota = leerNota();
        String observacion = leerTexto("Observacion (Enter para omitir): ");
        boolean ok = observacion.isEmpty()
                ? evaluacion.registrarNota(nota)
                : evaluacion.registrarNota(nota, observacion);
        System.out.println(ok ? "Nota registrada." : "No se pudo registrar la nota.");
    }

    private void registrarMeta() {
        System.out.println("Tipo de meta: 1) Aprobar / nota en un curso  2) Promedio general");
        int tipo = leerEntero("Elige el tipo: ");
        if (tipo == 1) {
            Curso curso = pedirCurso();
            if (curso == null) {
                return;
            }
            System.out.println("Nota objetivo del curso:");
            estudiante.agregarMeta(new MetaAprobacion(curso, leerNota()));
            System.out.println("Meta registrada.");
        } else if (tipo == 2) {
            if (estudiante.getCursos().isEmpty()) {
                System.out.println("Primero registra al menos un curso.");
                return;
            }
            String categoria = leerTexto("Categoria (ej. Tercio superior, Quinto superior, Beca): ");
            System.out.println("Promedio de referencia de esa categoria:");
            double objetivo = leerNota();
            estudiante.agregarMeta(new MetaPromedio(categoria, objetivo, estudiante.getCursos()));
            System.out.println("Meta registrada.");
        } else {
            System.out.println("Tipo no valido.");
        }
    }

    private void mostrarMetas() {
        ArrayList<Meta> metas = estudiante.getMetas();
        if (metas.isEmpty()) {
            System.out.println("Aun no hay metas registradas.");
            return;
        }
        System.out.println();
        System.out.println("=== METAS ===");
        for (Meta m : metas) {   // polimorfismo: cada meta calcula a su manera
            System.out.println("- [" + m.getTipo() + "] " + m.getNombre());
            System.out.println("    " + m.obtenerEstado());
        }
    }

    private void mostrarRecomendaciones() {
        ArrayList<String> lista = gestorRecomendaciones.generarRecomendaciones(estudiante);
        System.out.println();
        System.out.println("=== RECOMENDACIONES ===");
        if (lista.isEmpty()) {
            System.out.println("No hay recomendaciones por ahora.");
        }
        for (String r : lista) {
            System.out.println("- " + r);
        }
    }

    private void mostrarHorario() {
        HorarioEstudio horario = estudiante.getHorario();
        String respuesta = leerTexto("Actualizar horas disponibles por dia? (s/n): ");
        if (respuesta.equalsIgnoreCase("s")) {
            for (String dia : HorarioEstudio.DIAS) {
                int horas = leerEntero("Horas libres el " + dia + " (0-12): ");
                if (!horario.definirDisponibilidad(dia, horas)) {
                    System.out.println("Valor no valido, se mantiene el anterior.");
                }
            }
        }
        horario.generarHorario(estudiante.getCursos());
        System.out.println();
        System.out.println("=== HORARIO DE ESTUDIO SEMANAL ===");
        if (horario.calcularTotalHoras() == 0) {
            System.out.println("No se asignaron horas: registra cursos y tu disponibilidad.");
            return;
        }
        for (String dia : HorarioEstudio.DIAS) {
            ArrayList<BloqueEstudio> bloques = horario.obtenerBloquesPorDia(dia);
            if (!bloques.isEmpty()) {
                System.out.println(dia + ":");
                for (BloqueEstudio b : bloques) {
                    System.out.println("    " + b);
                }
            }
        }
        System.out.println("Total semanal: " + horario.calcularTotalHoras() + " horas");
    }

    private void mostrarAlertas() {
        ArrayList<Alerta> alertas = gestorAlertas.generarAlertas(estudiante.getCursos());
        System.out.println();
        System.out.println("=== ALERTAS DE RIESGO ===");
        if (alertas.isEmpty()) {
            System.out.println("Ningun curso en riesgo.");
            return;
        }
        for (Alerta a : alertas) {
            System.out.println("- " + a);
        }
        System.out.println("Resumen: " + gestorAlertas.contarAlertasPorNivel("ALTO") + " ALTO, "
                + gestorAlertas.contarAlertasPorNivel("MEDIO") + " MEDIO");
    }


    private Curso pedirCurso() {
        if (estudiante.getCursos().isEmpty()) {
            System.out.println("Primero registra un curso.");
            return null;
        }
        System.out.print("Cursos:");
        for (Curso c : estudiante.getCursos()) {
            System.out.print(" " + c.getCodigo());
        }
        System.out.println();
        Curso curso = estudiante.buscarCurso(leerTexto("Codigo del curso: "));
        if (curso == null) {
            System.out.println("No existe un curso con ese codigo.");
        }
        return curso;
    }

    private double leerNota() {
        while (true) {
            String texto = leerTexto("Nota (" + (int) Calificacion.NOTA_MINIMA + " - "
                    + (int) Calificacion.NOTA_MAXIMA + "): ").replace(',', '.');
            try {
                double nota = Double.parseDouble(texto);
                if (Calificacion.esNotaValida(nota)) {
                    return nota;
                }
            } catch (NumberFormatException ex) {
                // se vuelve a pedir
            }
            System.out.println("Nota no valida, intenta de nuevo.");
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException ex) {
                System.out.println("Ingresa un numero entero.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {   
            System.out.println();
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }
}