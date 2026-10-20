package profefighter.personajes;

public class DaynerFactory extends PersonajeFactory {

    private final Prototype<Personaje> prototype;   // tipado a la interfaz: solo necesitamos que se pueda clonar

    public DaynerFactory(ConcretePersonajeBuilder builder) {
        builder.setTipo(ConcretePersonajeBuilder.DAYNER);
        Director director = new Director(builder);
        director.construir(
                "Dayner Core", 110,
                "Overclock Strike", 9,
                "Bus de Datos", 22);
        this.prototype = builder.getResult();
    }

    @Override
    public Producto crearPersonaje() {
        // PROTOTYPE: en vez de reconstruir con el Builder, clonamos la plantilla ya creada.
        return prototype.clonar();
    }
}