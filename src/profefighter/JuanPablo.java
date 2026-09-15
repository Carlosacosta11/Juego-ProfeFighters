package profefighter;

/** Personaje "Coach JP" — profesor de Inglés. */
public class JuanPablo extends Personaje {

    public JuanPablo(String nombre, int vidaMaxima,
                      String nombreAtaqueBasico, int danoBasico,
                      String nombreAtaqueEspecial, int danoEspecial) {
        super(nombre, vidaMaxima, nombreAtaqueBasico, danoBasico, nombreAtaqueEspecial, danoEspecial);
    }

    private JuanPablo(JuanPablo otro) {
        super(otro);
    }

    @Override
    public Personaje clonar() {
        return new JuanPablo(this);
    }
}
