package profefighter.personajes;

/**
 * PATRÓN FACTORY METHOD (clase "Creator")
 * Cada fábrica concreta sabe cómo construir (una vez) su personaje y luego
 * cómo entregarlo (clonando el prototype) cada vez que se pide una instancia.
 * El factory method devuelve la interfaz Producto, no una clase concreta.
 * BattleController solo conoce esta clase abstracta, nunca las concretas.
 */
public abstract class PersonajeFactory {

    public abstract Producto crearPersonaje();

    /**
     * Operación de negocio del Creator que no tiene que ver con construir el
     * producto (el "someOperation()" del patrón): arma la descripción que se
     * muestra en la tarjeta de selección, reutilizando el factory method.
     */
    public String describir() {
        Producto muestra = crearPersonaje();
        return "Vida: " + muestra.getVidaMaxima() + "\n"
                + muestra.getNombreAtaqueBasico() + " (-" + muestra.getDanoBasico() + ")\n"
                + muestra.getNombreAtaqueEspecial() + " (-" + muestra.getDanoEspecial() + ")";
    }
}