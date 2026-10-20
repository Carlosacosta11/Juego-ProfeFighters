package profefighter.fachada;

import profefighter.decorador.Combatiente;
import profefighter.juego.PersonajeMeta;
import profefighter.personajes.BattleController;
import profefighter.personajes.PersonajeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PATRÓN FACADE (fachada adicional, "Additional Facade")
 * Se separó de PartidaFacade para no mezclar en una sola fachada funciones
 * que no tienen relación entre sí: PartidaFacade arma una partida; esta
 * clase solo responde "qué profesores hay y cómo se presentan". PartidaFacade
 * la contiene (otra fachada que usa a esta) y la pantalla de selección, como
 * cliente, la obtiene a través de PartidaFacade.getCatalogo().
 * Igual que la principal, depende de las clases del subsistema, pero ellas no
 * saben que existe.
 */
public class CatalogoFacade {

    private final BattleController controller;

    public CatalogoFacade(BattleController controller) {
        this.controller = controller;
    }

    /** anotherOperation(): arma, para cada profesor registrado, todo lo que su tarjeta necesita. */
    public List<TarjetaProfesor> listarProfesores() {
        List<TarjetaProfesor> tarjetas = new ArrayList<>();
        for (Map.Entry<String, PersonajeFactory> entrada : controller.getFabricasDisponibles().entrySet()) {
            String id = entrada.getKey();
            PersonajeFactory fabrica = entrada.getValue();
            Combatiente muestra = fabrica.crearPersonaje();
            PersonajeMeta.Info meta = PersonajeMeta.of(id);
            tarjetas.add(new TarjetaProfesor(id, muestra.getNombre(), meta.materia,
                    meta.color, meta.inicial, fabrica.describir()));
        }
        return tarjetas;
    }
}
