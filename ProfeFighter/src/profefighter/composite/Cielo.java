package profefighter.composite;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

/** PATRÓN COMPOSITE (hoja): cielo de atardecer apocalíptico con el sol rojizo. */
public class Cielo implements ElementoGrafico {

    private final int ancho;
    private final int sueloY;

    public Cielo(int ancho, int sueloY) {
        this.ancho = ancho;
        this.sueloY = sueloY;
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setPaint(new GradientPaint(0, 0, new Color(70, 30, 30), 0, sueloY, new Color(35, 25, 35)));
        g.fillRect(0, 0, ancho, sueloY);

        g.setPaint(new Color(200, 90, 50, 160));
        g.fillOval(ancho - 160, 40, 90, 90);
        g.setPaint(new Color(200, 90, 50, 70));
        g.fillOval(ancho - 190, 10, 150, 150);
    }
}
