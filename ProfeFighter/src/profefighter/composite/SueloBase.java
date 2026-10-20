package profefighter.composite;

import java.awt.Color;
import java.awt.Graphics2D;

/** PATRÓN COMPOSITE (hoja): franja de suelo sobre la que pelean los personajes. */
public class SueloBase implements ElementoGrafico {

    private final int ancho;
    private final int sueloY;
    private final int altoTotal;

    public SueloBase(int ancho, int sueloY, int altoTotal) {
        this.ancho = ancho;
        this.sueloY = sueloY;
        this.altoTotal = altoTotal;
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(46, 40, 42));
        g.fillRect(0, sueloY, ancho, altoTotal - sueloY);
    }
}
