package profefighter.personajes;

/**
 * PATRÓN BUILDER (interfaz "Builder")
 * Declara los pasos de construcción comunes a todos los builders. No declara
 * cómo obtener el resultado: cada builder concreto entrega su producto con
 * getResult(), porque productos de builders distintos pueden no compartir
 * una misma interfaz.
 */
public interface PersonajeBuilder {

    void reset();

    void setNombre(String nombre);

    void setVidaMaxima(int vidaMaxima);

    void setAtaqueBasico(String nombreAtaque, int dano);

    void setAtaqueEspecial(String nombreAtaque, int dano);
}