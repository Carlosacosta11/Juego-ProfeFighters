package profefighter;

/**
 * PATRÓN FACTORY METHOD
 * Cada fábrica concreta sabe cómo construir (una vez) su personaje y luego
 * cómo entregarlo (clonando el prototype) cada vez que se pide una instancia.
 * BattleController solo conoce esta clase abstracta, nunca las concretas.
 */
public abstract class PersonajeFactory {

    public abstract Personaje crearPersonaje();
}
