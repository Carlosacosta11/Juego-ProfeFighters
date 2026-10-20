package profefighter.personajes;

public class EdithFactory extends PersonajeFactory {

    private final Prototype<Personaje> prototype;   // tipado a la interfaz: solo necesitamos que se pueda clonar

    public EdithFactory(ConcretePersonajeBuilder builder) {
        builder.setTipo(ConcretePersonajeBuilder.EDITH);
        Director director = new Director(builder);
        director.construir(
                "Edith Derivada", 90,
                "Golpe Integral", 12,
                "Combo Diferencial", 28);
        this.prototype = builder.getResult();
    }

    @Override
    public Producto crearPersonaje() {
        // PROTOTYPE: en vez de reconstruir con el Builder, clonamos la plantilla ya creada.
        return prototype.clonar();
    }
}