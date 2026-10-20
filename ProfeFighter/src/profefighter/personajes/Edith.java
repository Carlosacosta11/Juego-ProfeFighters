package profefighter.personajes;

/** Personaje "Edith Derivada" — profesora de Ecuaciones Diferenciales / Cálculo. */
public class Edith extends Personaje {

    public Edith(String nombre, int vidaMaxima,
                 String nombreAtaqueBasico, int danoBasico,
                 String nombreAtaqueEspecial, int danoEspecial) {
        super(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
    }

    public Edith(Edith otro) {
        super(otro);
    }

    @Override
    public Personaje clonar() {
        return new Edith(this);
    }
}