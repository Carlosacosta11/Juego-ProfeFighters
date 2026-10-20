package profefighter.fachada;

import profefighter.decorador.Combatiente;
import profefighter.juego.GameConfig;
import profefighter.juego.Luchador;
import profefighter.juego.PersonajeMeta;
import profefighter.personajes.BattleController;

import java.util.List;

/**
 * PATRÓN FACADE (clase "Facade")
 * Da un acceso cómodo a la parte del subsistema que necesita el cliente
 * (Main): armar una partida a partir de dos identificadores. Sabe a quién
 * pedirle cada cosa -BattleController para crear los personajes,
 * PersonajeMeta para su color e inicial, Luchador para ponerlos en la arena-
 * y en qué orden, de modo que el cliente no toca ninguna de esas clases.
 * Las clases del subsistema no conocen la existencia de esta fachada.
 *
 * Es el único punto de entrada del cliente (Main): tanto para armar una partida
 * como para obtener las tarjetas del catálogo, que delega en la fachada adicional.
 */
public class PartidaFacade {

    private final BattleController controller;          // linksToSubsystemObjects
    private final CatalogoFacade catalogo;              // optionalAdditionalFacade

    public PartidaFacade() {
        this(new BattleController());
    }

    public PartidaFacade(BattleController controller) {
        this.controller = controller;
        this.catalogo = new CatalogoFacade(controller);
    }

    /** subsystemOperation(): crea los dos personajes, sus luchadores y los coloca en posición de combate. */
    public Partida iniciarPartida(String idPersonaje1, String idPersonaje2) {
        Combatiente p1 = controller.seleccionarPersonaje(idPersonaje1);
        Combatiente p2 = controller.seleccionarPersonaje(idPersonaje2);
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Personaje desconocido: " + idPersonaje1 + " / " + idPersonaje2);
        }

        PersonajeMeta.Info meta1 = PersonajeMeta.of(idPersonaje1);
        PersonajeMeta.Info meta2 = PersonajeMeta.of(idPersonaje2);

        Luchador jugador1 = new Luchador(p1, 160, true, meta1.color, meta1.inicial, "Jugador 1");
        Luchador jugador2 = new Luchador(p2, GameConfig.ANCHO_ARENA - 220, false,
                meta2.color, meta2.inicial, "Jugador 2");
        return new Partida(jugador1, jugador2);
    }

    /** Delega en la fachada adicional: el cliente no necesita conocer CatalogoFacade. */
    public List<TarjetaProfesor> listarProfesores() {
        return catalogo.listarProfesores();
    }

    /** Acceso a la fachada adicional. */
    public CatalogoFacade getCatalogo() {
        return catalogo;
    }
}