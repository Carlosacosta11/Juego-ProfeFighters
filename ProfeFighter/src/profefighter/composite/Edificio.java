package profefighter.composite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

/** PATRÓN COMPOSITE (hoja): silueta de un edificio en ruinas. */
public class Edificio implements ElementoGrafico {

    private final Polygon silueta;

    public Edificio(Polygon silueta) {
        this.silueta = silueta;
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(20, 15, 20, 200));
        g.fillPolygon(silueta);
    }
}
