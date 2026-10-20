package profefighter.composite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;

/** PATRÓN COMPOSITE (hoja): una grieta del suelo, dibujada como línea quebrada. */
public class Grieta implements ElementoGrafico {

    private final Path2D trazo;

    public Grieta(Path2D trazo) {
        this.trazo = trazo;
    }

    @Override
    public void dibujar(Graphics2D g) {
        Stroke anterior = g.getStroke();
        g.setColor(new Color(20, 16, 18));
        g.setStroke(new BasicStroke(2f));
        g.draw(trazo);
        g.setStroke(anterior);
    }
}
