package profefighter;

/** Personaje "Dayner Core" — profesor de Arquitectura de Computadores. */
public class Dayner extends Personaje {

    public Dayner(String nombre, int vidaMaxima,
                  String nombreAtaqueBasico, int danoBasico,
                  String nombreAtaqueEspecial, int danoEspecial) {
        super(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
    }

    private Dayner(Dayner otro) {
        super(otro);
    }

    @Override
    public Personaje clonar() {
        return new Dayner(this);
    }
}
