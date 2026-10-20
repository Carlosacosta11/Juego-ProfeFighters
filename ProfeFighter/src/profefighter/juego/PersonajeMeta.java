package profefighter.juego;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Metadatos puramente visuales de cada profesor (color, materia que
 * representa, descripción corta) usados por la pantalla de selección y la
 * arena de combate.
 *
 * Se mantienen separados de Personaje a propósito: Personaje solo debe
 * encargarse de su estado de combate (SRP), no de cómo se dibuja o presenta
 * en la interfaz gráfica.
 */
public final class PersonajeMeta {

    public static final class Info {
        public final String materia;
        public final String descripcion;
        public final Color color;
        public final String inicial;

        Info(String materia, String descripcion, Color color, String inicial) {
            this.materia = materia;
            this.descripcion = descripcion;
            this.color = color;
            this.inicial = inicial;
        }
    }

    private static final Map<String, Info> DATOS = new LinkedHashMap<>();

    static {
        DATOS.put("1", new Info("Construcción de Software",
                "Ataca con refactors y builds implacables.",
                new Color(37, 99, 235), "M"));
        DATOS.put("2", new Info("Ecuaciones Diferenciales",
                "Golpea con integrales y combos diferenciales.",
                new Color(124, 58, 237), "E"));
        DATOS.put("3", new Info("Arquitectura de Computadores",
                "Pelea a puro overclock y buses de datos.",
                new Color(234, 88, 12), "D"));
        DATOS.put("4", new Info("Inglés",
                "Somete al rival a puro grammar y vocabulario.",
                new Color(22, 163, 74), "JP"));
    }

    private PersonajeMeta() {
    }

    public static Info of(String idFactory) {
        return DATOS.getOrDefault(idFactory, new Info("Profesor", "", Color.GRAY, "?"));
    }
}
