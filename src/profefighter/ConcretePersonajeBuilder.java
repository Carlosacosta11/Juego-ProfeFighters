package profefighter;

/**
 * PATRÓN BUILDER (implementación concreta)
 * Va acumulando los datos del personaje paso a paso y al final los entrega
 * en un objeto Personaje ya armado mediante build().
 *
 * setTipo(...) es un método adicional (no forma parte de la interfaz
 * PersonajeBuilder) que usan las fábricas concretas para indicarle a este
 * builder qué subclase de Personaje debe instanciar al construir.
 */
public class ConcretePersonajeBuilder implements PersonajeBuilder {

    public static final String MARTA = "MARTA";
    public static final String EDITH = "EDITH";
    public static final String DAYNER = "DAYNER";
    public static final String JUAN_PABLO = "JUAN_PABLO";

    private String tipo;
    private String nombre;
    private int vidaMaxima;
    private String nombreAtaqueBasico;
    private int danoBasico;
    private String nombreAtaqueEspecial;
    private int danoEspecial;

    public ConcretePersonajeBuilder setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    @Override
    public PersonajeBuilder setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    @Override
    public PersonajeBuilder setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
        return this;
    }

    @Override
    public PersonajeBuilder setAtaqueBasico(String nombreAtaque, int dano) {
        this.nombreAtaqueBasico = nombreAtaque;
        this.danoBasico = dano;
        return this;
    }

    @Override
    public PersonajeBuilder setAtaqueEspecial(String nombreAtaque, int dano) {
        this.nombreAtaqueEspecial = nombreAtaque;
        this.danoEspecial = dano;
        return this;
    }

    @Override
    public Personaje build() {
        if (tipo == null) {
            throw new IllegalStateException("Debe llamar setTipo(...) antes de build()");
        }
        switch (tipo) {
            case MARTA:
                return new Marta(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
            case EDITH:
                return new Edith(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
            case DAYNER:
                return new Dayner(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
            case JUAN_PABLO:
                return new JuanPablo(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
            default:
                throw new IllegalStateException("Tipo de personaje desconocido: " + tipo);
        }
    }
}
