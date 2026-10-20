package profefighter.personajes;

/**
 * PATRÓN BUILDER (clase "Concrete Builder")
 * Va acumulando los datos del personaje paso a paso y al final los entrega
 * en un objeto Personaje ya armado mediante getResult().
 *
 * setTipo(...) es un método adicional (no forma parte de la interfaz
 * PersonajeBuilder) que usan las fábricas concretas -que son el Client del
 * patrón y conocen este builder concreto- para indicarle qué subclase de
 * Personaje debe instanciar al construir.
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

    /**
     * Limpia los datos de configuración (no el tipo, que es independiente
     * de la interfaz PersonajeBuilder). Permite reutilizar el mismo builder
     * para una nueva construcción; el Director la llama antes de empezar.
     */
    @Override
    public void reset() {
        this.nombre = null;
        this.vidaMaxima = 0;
        this.nombreAtaqueBasico = null;
        this.danoBasico = 0;
        this.nombreAtaqueEspecial = null;
        this.danoEspecial = 0;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    @Override
    public void setAtaqueBasico(String nombreAtaque, int dano) {
        this.nombreAtaqueBasico = nombreAtaque;
        this.danoBasico = dano;
    }

    @Override
    public void setAtaqueEspecial(String nombreAtaque, int dano) {
        this.nombreAtaqueEspecial = nombreAtaque;
        this.danoEspecial = dano;
    }

    /** Entrega el Personaje armado con los pasos ejecutados hasta ahora. */
    public Personaje getResult() {
        if (tipo == null) {
            throw new IllegalStateException("Debe llamar setTipo(...) antes de getResult()");
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