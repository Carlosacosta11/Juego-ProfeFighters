package profefighter.personajes;

/**
 * PATRÓN BUILDER — Director
 * Conoce el orden exacto en el que hay que llamar los pasos del builder
 * para dejar un Personaje completamente configurado. Cada fábrica concreta
 * (MartaFactory, EdithFactory, DaynerFactory, JuanPabloFactory) le entrega
 * su ConcretePersonajeBuilder -ya con el tipo fijado mediante setTipo()- y
 * después le pide el resultado al propio builder con getResult(), sin repetir
 * la secuencia de pasos en cada una de las cuatro clases.
 */
public class Director {

    private PersonajeBuilder builder;

    public Director(PersonajeBuilder builder) {
        this.builder = builder;
    }

    /** Permite reutilizar el mismo Director con un builder distinto. */
    public void setBuilder(PersonajeBuilder builder) {
        this.builder = builder;
    }

    /** Ejecuta, en orden, los pasos comunes a la construcción de cualquier Personaje. */
    public void construir(String nombre, int vidaMaxima,
                          String nombreAtaqueBasico, int danoBasico,
                          String nombreAtaqueEspecial, int danoEspecial) {
        builder.reset();
        builder.setNombre(nombre);
        builder.setVidaMaxima(vidaMaxima);
        builder.setAtaqueBasico(nombreAtaqueBasico, danoBasico);
        builder.setAtaqueEspecial(nombreAtaqueEspecial, danoEspecial);
    }
}