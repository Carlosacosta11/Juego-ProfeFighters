package profefighter;

/** Personaje "Marta Compiler" — profesora de Construcción de Software. */
public class Marta extends Personaje {

    public Marta(String nombre, int vidaMaxima,
                 String nombreAtaqueBasico, int danoBasico,
                 String nombreAtaqueEspecial, int danoEspecial) {
        super(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
    }

    private Marta(Marta otro) {
        super(otro);
    }

    @Override
    public Personaje clonar() {
        return new Marta(this);
    }
}
