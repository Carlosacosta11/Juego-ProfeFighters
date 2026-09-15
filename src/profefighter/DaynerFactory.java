package profefighter;

public class DaynerFactory extends PersonajeFactory {

    private final Personaje prototype;

    public DaynerFactory(ConcretePersonajeBuilder builder) {
        this.prototype = builder
                .setTipo(ConcretePersonajeBuilder.DAYNER)
                .setNombre("Dayner Core")
                .setVidaMaxima(110)
                .setAtaqueBasico("Overclock Strike", 9)
                .setAtaqueEspecial("Bus de Datos", 22)
                .build();
    }

    @Override
    public Personaje crearPersonaje() {
        return prototype.clonar();
    }
}
