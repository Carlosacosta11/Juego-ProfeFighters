package profefighter.composite;

import java.awt.Graphics2D;

/**
 * PATRÓN COMPOSITE (interfaz "Component")
 * Describe la operación común a los elementos simples (hojas) y a los
 * complejos (grupos) del escenario: dibujarse. dibujar() es el "execute()"
 * del patrón. Quien dibuja la escena trabaja solo con esta interfaz.
 */
public interface ElementoGrafico {

    void dibujar(Graphics2D g);
}
