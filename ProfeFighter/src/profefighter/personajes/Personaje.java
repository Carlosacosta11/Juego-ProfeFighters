package profefighter.personajes;

import profefighter.decorador.Combatiente;

import java.util.Collections;
import java.util.List;

/**
 * PATRÓN PROTOTYPE (rol "Concrete Prototype")
 * Personaje es el "producto" que construye el Builder y que las Factory
 * mantienen como plantilla (prototype). Implementa tres interfaces
 * independientes, cada una perteneciente a su propio patrón:
 *   - Prototype<Personaje>: declara clonar(); permite obtener una copia fresca
 *     (vida reiniciada) sin repetir todo el proceso de construcción cada vez
 *     que empieza un nuevo combate.
 *   - Producto: el "Product" del patrón Factory Method (extiende Combatiente).
 *   - Combatiente: el "Component" del patrón Decorator; Personaje es su
 *     "Concrete Component", la interfaz que comparten los personajes y los
 *     decoradores (FuriaDecorator, EscudoDecorator) que se les envuelven en
 *     combate.
 * Es una clase abstracta: las subclases concretas (Marta, Edith, Dayner,
 * JuanPablo) implementan clonar().
 */
public abstract class Personaje implements Prototype<Personaje>, Producto, Combatiente {

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
    @Override
    public abstract Personaje clonar();

    @Override
    public void recibirDano(int cantidad) {
        vidaActual -= cantidad;
        if (vidaActual < 0) {
            vidaActual = 0;
        }
    }

    @Override
    public boolean estaVivo() {
        return vidaActual > 0;
    }

    /**
     * Ejecuta un ataque contra otro personaje.
     * Implementa Producto.atacar() y Combatiente.atacar(): es el "doStuff()" del patrón Factory Method
     * y el "execute()" del patrón Decorator.
     * @param objetivo personaje que recibe el daño
     * @param especial true para usar el ataque especial, false para el básico
     */
    @Override
    public void atacar(Combatiente objetivo, boolean especial) {
        String nombreAtaque = especial ? nombreAtaqueEspecial : nombreAtaqueBasico;
        int dano = especial ? danoEspecial : danoBasico;
        System.out.println(nombre + " usa " + nombreAtaque + " (-" + dano + " de vida a " + objetivo.getNombre() + ")");
        objetivo.recibirDano(dano);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getVidaActual() {
        return vidaActual;
    }

    @Override
    public int getVidaMaxima() {
        return vidaMaxima;
    }

    @Override
    public int getDanoEspecial() {
        return danoEspecial;
    }

    @Override
    public String getNombreAtaqueBasico() {
        return nombreAtaqueBasico;
    }

    @Override
    public int getDanoBasico() {
        return danoBasico;
    }

    @Override
    public String getNombreAtaqueEspecial() {
        return nombreAtaqueEspecial;
    }

    /** Un personaje sin decorar no tiene potenciadores activos. */
    @Override
    public List<String> getEfectos() {
        return Collections.emptyList();
    }
}