package gui;

import analisis.Alertable;
import java.awt.Color;
import java.awt.Font;
import planificacion.Meta;

public class Paleta {
    public static final Color PRIMARIO = new Color(23, 63, 107);
    public static final Color PRIMARIO_OSCURO = new Color(14, 40, 70);
    public static final Color PRIMARIO_CLARO = new Color(222, 232, 245);
    public static final Color ACENTO = new Color(240, 165, 0);
    public static final Color FONDO = new Color(241, 243, 246);
    public static final Color TARJETA = Color.WHITE;
    public static final Color LINEA = new Color(214, 219, 226);
    public static final Color FILA_ALTERNA = new Color(248, 249, 251);
    public static final Color TEXTO = new Color(33, 37, 41);
    public static final Color TEXTO_SUAVE = new Color(105, 114, 125);
    public static final Color TEXTO_CLARO = Color.WHITE;

    public static final Color ROJO = new Color(192, 45, 45);
    public static final Color ROJO_CLARO = new Color(252, 228, 228);
    public static final Color NARANJA = new Color(214, 120, 0);
    public static final Color NARANJA_CLARO = new Color(255, 241, 217);
    public static final Color VERDE = new Color(40, 125, 60);
    public static final Color VERDE_CLARO = new Color(222, 242, 226);
    public static final Color GRIS = new Color(120, 127, 136);
    public static final Color GRIS_CLARO = new Color(235, 237, 240);

    public static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 15);
    public static final Font FUENTE_NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FUENTE_NEGRITA = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FUENTE_PEQUENA = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FUENTE_DATO = new Font("SansSerif", Font.BOLD, 20);

    public static Color colorRiesgo(String nivel) {
        if (nivel.equals(Alertable.RIESGO_ALTO)) {
            return ROJO;
        }
        if (nivel.equals(Alertable.RIESGO_MEDIO)) {
            return NARANJA;
        }
        return VERDE;
    }

    public static Color colorRiesgoClaro(String nivel) {
        if (nivel.equals(Alertable.RIESGO_ALTO)) {
            return ROJO_CLARO;
        }
        if (nivel.equals(Alertable.RIESGO_MEDIO)) {
            return NARANJA_CLARO;
        }
        return VERDE_CLARO;
    }

    public static Color colorMeta(String nivelMeta) {
        if (nivelMeta.equals(Meta.NIVEL_ASEGURADA)) {
            return VERDE;
        }
        if (nivelMeta.equals(Meta.NIVEL_ALCANZABLE)) {
            return PRIMARIO;
        }
        if (nivelMeta.equals(Meta.NIVEL_EXIGENTE)) {
            return NARANJA;
        }
        if (nivelMeta.equals(Meta.NIVEL_NO_POSIBLE)) {
            return ROJO;
        }
        return GRIS;
    }

    public static Color colorMetaClaro(String nivelMeta) {
        if (nivelMeta.equals(Meta.NIVEL_ASEGURADA)) {
            return VERDE_CLARO;
        }
        if (nivelMeta.equals(Meta.NIVEL_ALCANZABLE)) {
            return PRIMARIO_CLARO;
        }
        if (nivelMeta.equals(Meta.NIVEL_EXIGENTE)) {
            return NARANJA_CLARO;
        }
        if (nivelMeta.equals(Meta.NIVEL_NO_POSIBLE)) {
            return ROJO_CLARO;
        }
        return GRIS_CLARO;
    }
}
