package profefighter.personajes;

public class MartaFactory extends PersonajeFactory {

    private final Prototype<Personaje> prototype;   // tipado a la interfaz: solo necesitamos que se pueda clonar

    public MartaFactory(ConcretePersonajeBuilder builder) {
        builder.setTipo(ConcretePersonajeBuilder.MARTA);
        Director director = new Director(builder);
        director.construir(
                "Marta Compiler", 100,
                "Debug Punch", 10,
                "Refactor Combo", 25);
        this.prototype = builder.getResult();
    }

    @Override
    public Producto crearPersonaje() {
        // PROTOTYPE: en vez de reconstruir con el Builder, clonamos la plantilla ya creada.
        return prototype.clonar();
    }
}