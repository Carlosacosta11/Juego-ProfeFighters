package profefighter.composite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/** PATRÓN COMPOSITE (hoja): un trozo de escombro sobre el suelo. */
public class Escombro implements ElementoGrafico {

    private final Rectangle area;

    public Escombro(Rectangle area) {
        this.area = area;
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(30, 26, 28));
        g.fillRect(area.x, area.y, area.width, area.height);
    }
}
