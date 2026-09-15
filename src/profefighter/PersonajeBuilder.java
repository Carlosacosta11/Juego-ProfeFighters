package profefighter;

/**
 * PATRÓN BUILDER (interfaz)
 * Define los pasos necesarios para construir un Personaje pieza por pieza,
 * evitando un constructor con muchos parámetros difíciles de leer.
 */
public interface PersonajeBuilder {

    PersonajeBuilder setNombre(String nombre);

    PersonajeBuilder setVidaMaxima(int vidaMaxima);

    PersonajeBuilder setAtaqueBasico(String nombreAtaque, int dano);

    PersonajeBuilder setAtaqueEspecial(String nombreAtaque, int dano);

    Personaje build();
}
