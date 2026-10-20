package profefighter.ui;

import profefighter.composite.Cielo;
import profefighter.composite.Edificio;
import profefighter.composite.ElementoGrafico;
import profefighter.composite.Escombro;
import profefighter.composite.Grieta;
import profefighter.composite.GrupoGrafico;
import profefighter.composite.SueloBase;
import profefighter.juego.GameConfig;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.Random;

/**
 * Ensambla el árbol del patrón Composite con el decorado de la arena (mundo
 * destruido). Es quien conoce las clases concretas (hojas y grupos); la arena
 * recibe el resultado como un simple ElementoGrafico.
 */
public final class EscenarioMundoDestruido {

    private EscenarioMundoDestruido() {
    }

    /**
     * Arma el árbol del Composite. La generación aleatoria conserva la semilla
     * fija (42) y el mismo orden de sorteos, así que el decorado es idéntico
     * en todas las partidas.
     *
     *   Escenario
     *   ├── Fondo:  Cielo, Ciudad (6 Edificio)
     *   └── Suelo:  SueloBase, Escombros (14 Escombro), Grietas (2 Grieta)
     */
    public static ElementoGrafico crear() {
        Random rnd = new Random(42);

        GrupoGrafico ciudad = new GrupoGrafico("Ciudad");
        for (int i = 0; i < 6; i++) {
            int ancho = 60 + rnd.nextInt(90);
            int alto = 90 + rnd.nextInt(160);
            int x = i * (GameConfig.ANCHO_ARENA / 6) + rnd.nextInt(30);
            Polygon edificio = new Polygon();
            int baseY = GameConfig.SUELO_Y - 10;
            edificio.addPoint(x, baseY);
            edificio.addPoint(x, baseY - alto + rnd.nextInt(30));
            edificio.addPoint(x + ancho / 3, baseY - alto);
            edificio.addPoint(x + (2 * ancho) / 3, baseY - alto + rnd.nextInt(25));
            edificio.addPoint(x + ancho, baseY - alto + rnd.nextInt(20));
            edificio.addPoint(x + ancho, baseY);
            ciudad.add(new Edificio(edificio));
        }

        GrupoGrafico escombros = new GrupoGrafico("Escombros");
        for (int i = 0; i < 14; i++) {
            int w = 14 + rnd.nextInt(26);
            int h = 8 + rnd.nextInt(14);
            int x = rnd.nextInt(GameConfig.ANCHO_ARENA - w);
            int y = GameConfig.SUELO_Y + rnd.nextInt(GameConfig.ALTO_ARENA - GameConfig.SUELO_Y - h - 5);
            escombros.add(new Escombro(new Rectangle(x, y, w, h)));
        }

        Path2D trazo1 = new Path2D.Double();
        trazo1.moveTo(120, GameConfig.SUELO_Y + 6);
        trazo1.lineTo(200, GameConfig.SUELO_Y + 30);
        trazo1.lineTo(260, GameConfig.SUELO_Y + 14);
        trazo1.lineTo(340, GameConfig.SUELO_Y + 40);
        Path2D trazo2 = new Path2D.Double();
        trazo2.moveTo(560, GameConfig.SUELO_Y + 10);
        trazo2.lineTo(620, GameConfig.SUELO_Y + 34);
        trazo2.lineTo(700, GameConfig.SUELO_Y + 18);
        GrupoGrafico grietas = new GrupoGrafico("Grietas");
        grietas.add(new Grieta(trazo1));
        grietas.add(new Grieta(trazo2));

        GrupoGrafico fondo = new GrupoGrafico("Fondo");
        fondo.add(new Cielo(GameConfig.ANCHO_ARENA, GameConfig.SUELO_Y));
        fondo.add(ciudad);

        GrupoGrafico suelo = new GrupoGrafico("Suelo");
        suelo.add(new SueloBase(GameConfig.ANCHO_ARENA, GameConfig.SUELO_Y, GameConfig.ALTO_ARENA));
        suelo.add(escombros);
        suelo.add(grietas);

        GrupoGrafico raiz = new GrupoGrafico("Escenario");
        raiz.add(fondo);
        raiz.add(suelo);
        return raiz;
    }
}
