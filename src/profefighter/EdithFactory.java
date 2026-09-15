package profefighter;

public class EdithFactory extends PersonajeFactory {

    private final Personaje prototype;

    public EdithFactory(ConcretePersonajeBuilder builder) {
        this.prototype = builder
                .setTipo(ConcretePersonajeBuilder.EDITH)
                .setNombre("Edith Derivada")
                .setVidaMaxima(90)
                .setAtaqueBasico("Golpe Integral", 12)
                .setAtaqueEspecial("Combo Diferencial", 28)
                .build();
    }

    @Override
    public Personaje crearPersonaje() {
        return prototype.clonar();
    }
}
