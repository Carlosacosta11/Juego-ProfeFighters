package profefighter.fachada;

import profefighter.juego.Luchador;

/** Resultado de armar una partida: los dos luchadores listos para entrar a la arena. */
public class Partida {

    private final Luchador jugador1;
    private final Luchador jugador2;

    public Partida(Luchador jugador1, Luchador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public Luchador getJugador1() {
        return jugador1;
    }

    public Luchador getJugador2() {
        return jugador2;
    }
}
