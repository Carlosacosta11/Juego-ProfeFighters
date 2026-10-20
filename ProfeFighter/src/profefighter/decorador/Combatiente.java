package profefighter.decorador;

import java.util.List;

/**
 * PATRÓN DECORATOR (interfaz "Component")
 * Interfaz común tanto para los objetos envueltos (Personaje y sus subclases)
 * como para los envoltorios (los decoradores). Quien use un Combatiente
 * -Luchador, ArenaPanel- no sabe ni necesita saber si está hablando con un
 * personaje "limpio" o con uno envuelto en varias capas de potenciadores.
 *
 * atacar() es el "execute()" del patrón: es la operación que los
 * decoradores enriquecen. Es independiente de Producto (Factory Method) y de
 * Prototype: cada patrón tiene su propio contrato.
 */
public interface Combatiente {

    void atacar(Combatiente objetivo, boolean especial);

    void recibirDano(int cantidad);

    boolean estaVivo();

    String getNombre();

    int getVidaActual();

    int getVidaMaxima();

    int getDanoBasico();

    int getDanoEspecial();

    String getNombreAtaqueBasico();

    String getNombreAtaqueEspecial();

    /** Potenciadores activos en este momento (vacía si el combatiente no está decorado). */
    List<String> getEfectos();
}
