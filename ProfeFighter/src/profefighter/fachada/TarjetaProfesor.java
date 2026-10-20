package profefighter.fachada;

import java.awt.Color;

/** Datos ya preparados de un profesor para dibujar su tarjeta en la pantalla de selección. */
public class TarjetaProfesor {

    private final String id;
    private final String nombre;
    private final String materia;
    private final Color color;
    private final String inicial;
    private final String resumen;

    public TarjetaProfesor(String id, String nombre, String materia, Color color, String inicial, String resumen) {
        this.id = id;
        this.nombre = nombre;
        this.materia = materia;
        this.color = color;
        this.inicial = inicial;
        this.resumen = resumen;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMateria() {
        return materia;
    }

    public Color getColor() {
        return color;
    }

    public String getInicial() {
        return inicial;
    }

    /** Texto plano con vida y ataques (varias líneas), tal como lo arma PersonajeFactory.describir(). */
    public String getResumen() {
        return resumen;
    }
}
