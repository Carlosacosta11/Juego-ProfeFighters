package profefighter.decorador;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN DECORATOR (decorador concreto "Escudo")
 * Mientras le queden usos, reduce un porcentaje del daño de cada golpe
 * recibido. Aquí el comportamiento extra() ocurre ANTES de delegar en el
 * objeto envuelto: transforma el daño y luego se lo pasa al componente.
 */
public class EscudoDecorator extends CombatienteDecorator {

    private final int porcentajeReduccion;
    private int usosRestantes;

    public EscudoDecorator(Combatiente c, int usos, int porcentajeReduccion) {
        super(c);
        this.usosRestantes = usos;
        this.porcentajeReduccion = porcentajeReduccion;
    }

    @Override
    public void recibirDano(int cantidad) {
        super.recibirDano(extra(cantidad));        // extra() primero, luego el wrappee
    }

    /** Devuelve el daño ya reducido por el escudo (o el mismo daño si ya no quedan usos). */
    public int extra(int cantidad) {
        if (usosRestantes <= 0) {
            return cantidad;
        }
        usosRestantes--;
        int reducido = cantidad * (100 - porcentajeReduccion) / 100;
        System.out.println(getNombre() + " bloquea con ESCUDO (" + cantidad + " -> " + reducido + ")");
        return reducido;
    }

    @Override
    public List<String> getEfectos() {
        List<String> efectos = new ArrayList<>(super.getEfectos());
        if (usosRestantes > 0) {
            efectos.add("ESCUDO x" + usosRestantes);
        }
        return efectos;
    }
}
