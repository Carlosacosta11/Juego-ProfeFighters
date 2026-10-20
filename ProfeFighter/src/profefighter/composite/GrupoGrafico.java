package profefighter.composite;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN COMPOSITE (clase "Composite")
 * Contenedor con subelementos (hojas u otros grupos). No conoce las clases
 * concretas de sus hijos: trabaja con todos a través de ElementoGrafico.
 * Al recibir dibujar() delega el trabajo en sus hijos, en orden.
 */
public class GrupoGrafico implements ElementoGrafico {

    private final String nombre;
    private final List<ElementoGrafico> children = new ArrayList<>();

    public GrupoGrafico(String nombre) {
        this.nombre = nombre;
    }

    public void add(ElementoGrafico c) {
        children.add(c);
    }

    public void remove(ElementoGrafico c) {
        children.remove(c);
    }

    public List<ElementoGrafico> getChildren() {
        return children;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void dibujar(Graphics2D g) {
        for (ElementoGrafico hijo : children) {
            hijo.dibujar(g);
        }
    }
}
