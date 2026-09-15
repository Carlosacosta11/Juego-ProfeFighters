package profefighter;

public class MartaFactory extends PersonajeFactory {

    private final Personaje prototype;

    public MartaFactory(ConcretePersonajeBuilder builder) {
        this.prototype = builder
                .setTipo(ConcretePersonajeBuilder.MARTA)
                .setNombre("Marta Compiler")
                .setVidaMaxima(100)
                .setAtaqueBasico("Debug Punch", 10)
                .setAtaqueEspecial("Refactor Combo", 25)
                .build();
    }

    @Override
    public Personaje crearPersonaje() {
        // PROTOTYPE: en vez de reconstruir con el Builder, clonamos la plantilla ya creada.
        return prototype.clonar();
    }
}
