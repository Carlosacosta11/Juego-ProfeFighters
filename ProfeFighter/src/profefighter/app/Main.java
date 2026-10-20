package profefighter.app;

import profefighter.fachada.Partida;
import profefighter.fachada.PartidaFacade;
import profefighter.juego.GameConfig;
import profefighter.juego.Luchador;
import profefighter.ui.ArenaPanel;
import profefighter.ui.PantallaResultado;
import profefighter.ui.PantallaSeleccion;

import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada del juego ProfeFighter (versión con interfaz gráfica).
 * Usa CardLayout para pasar de la selección de personajes al combate y
 * luego a la pantalla de resultado.
 *
 * Es el cliente del patrón Facade: para armar una partida solo habla con
 * PartidaFacade y no conoce los personajes, fábricas ni metadatos.
 */
public class Main {

    private static final String CARTA_SELECCION = "SELECCION";
    private static final String CARTA_COMBATE = "COMBATE";
    private static final String CARTA_RESULTADO = "RESULTADO";

    private final PartidaFacade fachada = new PartidaFacade();
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
        return new PantallaSeleccion(fachada.listarProfesores(), this::onSeleccionCompleta);
    }

    private void onSeleccionCompleta(String idPersonaje1, String idPersonaje2) {
        Partida partida = fachada.iniciarPartida(idPersonaje1, idPersonaje2);

        arenaActual = new ArenaPanel(partida.getJugador1(), partida.getJugador2(), this::onCombateTerminado);
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