package profefighter;

import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada del juego ProfeFighter (versión con interfaz gráfica).
 * Usa CardLayout para pasar de la selección de personajes al combate y
 * luego a la pantalla de resultado.
 */
public class Main {

    private static final String CARTA_SELECCION = "SELECCION";
    private static final String CARTA_COMBATE = "COMBATE";
    private static final String CARTA_RESULTADO = "RESULTADO";

    private final BattleController controller = new BattleController();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);
    private ArenaPanel arenaActual;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().iniciar());
    }

    private void iniciar() {
        JFrame frame = new JFrame("ProfeFighter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        contenedor.add(crearPantallaSeleccion(), CARTA_SELECCION);
        contenedor.setPreferredSize(new Dimension(GameConfig.ANCHO_ARENA, GameConfig.ALTO_ARENA + 90));

        frame.setContentPane(contenedor);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cardLayout.show(contenedor, CARTA_SELECCION);
    }

    private PantallaSeleccion crearPantallaSeleccion() {
        return new PantallaSeleccion(controller, this::onSeleccionCompleta);
    }

    private void onSeleccionCompleta(String idPersonaje1, String idPersonaje2) {
        Personaje p1 = controller.seleccionarPersonaje(idPersonaje1);
        Personaje p2 = controller.seleccionarPersonaje(idPersonaje2);

        PersonajeMeta.Info meta1 = PersonajeMeta.of(idPersonaje1);
        PersonajeMeta.Info meta2 = PersonajeMeta.of(idPersonaje2);

        Luchador jugador1 = new Luchador(p1, 160, true, meta1.color, meta1.inicial, "Jugador 1");
        Luchador jugador2 = new Luchador(p2, GameConfig.ANCHO_ARENA - 220, false, meta2.color, meta2.inicial, "Jugador 2");

        arenaActual = new ArenaPanel(jugador1, jugador2, this::onCombateTerminado);
        contenedor.add(arenaActual, CARTA_COMBATE);
        cardLayout.show(contenedor, CARTA_COMBATE);
        arenaActual.requestFocusInWindow();
    }

    private void onCombateTerminado(Luchador ganador) {
        if (arenaActual != null) {
            arenaActual.detener();
        }
        PantallaResultado resultado = new PantallaResultado(ganador, this::reiniciar, () -> System.exit(0));
        contenedor.add(resultado, CARTA_RESULTADO);
        cardLayout.show(contenedor, CARTA_RESULTADO);
    }

    private void reiniciar() {
        contenedor.removeAll();
        contenedor.add(crearPantallaSeleccion(), CARTA_SELECCION);
        contenedor.revalidate();
        contenedor.repaint();
        cardLayout.show(contenedor, CARTA_SELECCION);
    }
}
