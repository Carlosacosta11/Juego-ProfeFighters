package profefighter;

/**
 * PATRÓN PROTOTYPE
 * Personaje es el "producto" que construye el Builder y que las Factory
 * mantienen como plantilla (prototype). El método clonar() permite obtener
 * una copia fresca (vida reiniciada) sin repetir todo el proceso de
 * construcción cada vez que empieza un nuevo combate.
 */
public abstract class Personaje {

    protected String nombre;
    protected int vidaMaxima;
    protected int vidaActual;
    protected String nombreAtaqueBasico;
    protected int danoBasico;
    protected String nombreAtaqueEspecial;
    protected int danoEspecial;

    protected Personaje(String nombre, int vidaMaxima,
                         String nombreAtaqueBasico, int danoBasico,
                         String nombreAtaqueEspecial, int danoEspecial) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.nombreAtaqueBasico = nombreAtaqueBasico;
        this.danoBasico = danoBasico;
        this.nombreAtaqueEspecial = nombreAtaqueEspecial;
        this.danoEspecial = danoEspecial;
    }

    /** Constructor de copia: usado por las subclases dentro de clonar(). */
    protected Personaje(Personaje otro) {
        this.nombre = otro.nombre;
        this.vidaMaxima = otro.vidaMaxima;
        this.vidaActual = otro.vidaMaxima; // vida reiniciada al clonar
        this.nombreAtaqueBasico = otro.nombreAtaqueBasico;
        this.danoBasico = otro.danoBasico;
        this.nombreAtaqueEspecial = otro.nombreAtaqueEspecial;
        this.danoEspecial = otro.danoEspecial;
    }

    /** Cada subclase concreta (Marta, Edith, Dayner, JuanPablo) implementa esto. */
    public abstract Personaje clonar();

    public void recibirDano(int cantidad) {
        vidaActual -= cantidad;
        if (vidaActual < 0) {
            vidaActual = 0;
        }
    }

    public boolean estaVivo() {
        return vidaActual > 0;
    }

    /**
     * Ejecuta un ataque contra otro personaje.
     * @param objetivo personaje que recibe el daño
     * @param especial true para usar el ataque especial, false para el básico
     */
    public void atacar(Personaje objetivo, boolean especial) {
        String nombreAtaque = especial ? nombreAtaqueEspecial : nombreAtaqueBasico;
        int dano = especial ? danoEspecial : danoBasico;
        System.out.println(nombre + " usa " + nombreAtaque + " (-" + dano + " de vida a " + objetivo.nombre + ")");
        objetivo.recibirDano(dano);
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getDanoEspecial() {
        return danoEspecial;
    }

    public String getNombreAtaqueBasico() {
        return nombreAtaqueBasico;
    }

    public int getDanoBasico() {
        return danoBasico;
    }

    public String getNombreAtaqueEspecial() {
        return nombreAtaqueEspecial;
    }
}
