package profefighter.decorador;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN DECORATOR (decorador concreto "Furia")
 * Mientras le queden usos, cada ataque que conecta hace un porcentaje extra
 * de daño. Primero ejecuta el comportamiento normal (super.atacar, que delega
 * en el objeto envuelto) y DESPUÉS añade el comportamiento extra().
 */
public class FuriaDecorator extends CombatienteDecorator {

    private final int porcentajeExtra;
    private int usosRestantes;

    public FuriaDecorator(Combatiente c, int usos, int porcentajeExtra) {
        super(c);
        this.usosRestantes = usos;
        this.porcentajeExtra = porcentajeExtra;
    }

    @Override
    public void atacar(Combatiente objetivo, boolean especial) {
        super.atacar(objetivo, especial);          // comportamiento original (wrappee)
        if (usosRestantes > 0) {
            usosRestantes--;
            extra(objetivo, especial);             // comportamiento añadido
        }
    }

    /** Daño adicional de la furia: un porcentaje del daño del ataque que se acaba de lanzar. */
    public void extra(Combatiente objetivo, boolean especial) {
        int base = especial ? getDanoEspecial() : getDanoBasico();
        int adicional = base * porcentajeExtra / 100;
        System.out.println(getNombre() + " desata FURIA (+" + adicional + " de daño)");
        objetivo.recibirDano(adicional);
    }

    @Override
    public List<String> getEfectos() {
        List<String> efectos = new ArrayList<>(super.getEfectos());
        if (usosRestantes > 0) {
            efectos.add("FURIA x" + usosRestantes);
        }
        return efectos;
    }
}
