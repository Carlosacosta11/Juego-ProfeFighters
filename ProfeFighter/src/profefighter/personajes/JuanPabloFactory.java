package profefighter.personajes;

public class JuanPabloFactory extends PersonajeFactory {

    private final Prototype<Personaje> prototype;   // tipado a la interfaz: solo necesitamos que se pueda clonar

    public JuanPabloFactory(ConcretePersonajeBuilder builder) {
        builder.setTipo(ConcretePersonajeBuilder.JUAN_PABLO);
        Director director = new Director(builder);
        director.construir(
                "Coach JP", 95,
                "Grammar Slam", 11,
                "Vocabulary Strike", 24);
        this.prototype = builder.getResult();
    }

    @Override
    public Producto crearPersonaje() {
        // PROTOTYPE: en vez de reconstruir con el Builder, clonamos la plantilla ya creada.
        return prototype.clonar();
    }
}