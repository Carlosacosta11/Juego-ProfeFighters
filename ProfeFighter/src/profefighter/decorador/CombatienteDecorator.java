package profefighter.decorador;

import java.util.List;

/**
 * PATRÓN DECORATOR (clase "Base Decorator")
 * Tiene un campo con el objeto envuelto (wrappee), declarado con el tipo de la
 * interfaz Combatiente para poder contener tanto personajes concretos como
 * otros decoradores. Delega TODAS las operaciones en el objeto envuelto; los
 * decoradores concretos sobrescriben solo las que quieren modificar.
 */
public abstract class CombatienteDecorator implements Combatiente {

    private final Combatiente wrappee;

    public CombatienteDecorator(Combatiente c) {
        this.wrappee = c;
    }

    @Override
    public void atacar(Combatiente objetivo, boolean especial) {
        wrappee.atacar(objetivo, especial);
    }

    @Override
    public void recibirDano(int cantidad) {
        wrappee.recibirDano(cantidad);
    }

    @Override
    public boolean estaVivo() {
        return wrappee.estaVivo();
    }

    @Override
    public String getNombre() {
        return wrappee.getNombre();
    }

    @Override
    public int getVidaActual() {
        return wrappee.getVidaActual();
    }

    @Override
    public int getVidaMaxima() {
        return wrappee.getVidaMaxima();
    }

    @Override
    public int getDanoBasico() {
        return wrappee.getDanoBasico();
    }

    @Override
    public int getDanoEspecial() {
        return wrappee.getDanoEspecial();
    }

    @Override
    public String getNombreAtaqueBasico() {
        return wrappee.getNombreAtaqueBasico();
    }

    @Override
    public String getNombreAtaqueEspecial() {
        return wrappee.getNombreAtaqueEspecial();
    }

    @Override
    public List<String> getEfectos() {
        return wrappee.getEfectos();
    }
}
