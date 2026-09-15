package profefighter;

public class JuanPabloFactory extends PersonajeFactory {

    private final Personaje prototype;

    public JuanPabloFactory(ConcretePersonajeBuilder builder) {
        this.prototype = builder
                .setTipo(ConcretePersonajeBuilder.JUAN_PABLO)
                .setNombre("Coach JP")
                .setVidaMaxima(95)
                .setAtaqueBasico("Grammar Slam", 11)
                .setAtaqueEspecial("Vocabulary Strike", 24)
                .build();
    }

    @Override
    public Personaje crearPersonaje() {
        return prototype.clonar();
    }
}
