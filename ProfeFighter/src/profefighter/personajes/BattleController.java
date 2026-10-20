package profefighter.personajes;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controla el registro de fábricas de personajes y la selección de un
 * personaje a partir de un identificador.
 *
 * La lógica de combate (movimiento, daño en tiempo real, energía especial)
 * vive ahora en ArenaPanel, ya que ahí el jugador interactúa directamente
 * con el juego. Este controlador se dedica únicamente a la selección
 * (ver documentación, sección de SOLID actualizada).
 *
 * La selección de fábrica se resuelve con un Map en vez de un if/else,
 * respetando el Open/Closed Principle: agregar un profesor nuevo solo
 * requiere una línea nueva en el registro, sin tocar el resto de la clase.
 * Además, esta clase solo conoce la abstracción PersonajeFactory, nunca una
 * fábrica concreta específica (Dependency Inversion Principle).
 */
public class BattleController {

    private final Map<String, PersonajeFactory> fabricasDisponibles = new LinkedHashMap<>();

    public BattleController() {
        // Registro de fábricas disponibles (cumple OCP: agregar un profesor
        // nuevo no obliga a modificar este código, solo a añadir una entrada).
        fabricasDisponibles.put("1", new MartaFactory(new ConcretePersonajeBuilder()));
        fabricasDisponibles.put("2", new EdithFactory(new ConcretePersonajeBuilder()));
        fabricasDisponibles.put("3", new DaynerFactory(new ConcretePersonajeBuilder()));
        fabricasDisponibles.put("4", new JuanPabloFactory(new ConcretePersonajeBuilder()));
    }

    /** Devuelve el registro de fábricas disponibles (solo lectura). */
    public Map<String, PersonajeFactory> getFabricasDisponibles() {
        return Collections.unmodifiableMap(fabricasDisponibles);
    }

    /** Crea una instancia fresca (vida al máximo) del personaje pedido, o null si el id no existe. */
    public Producto seleccionarPersonaje(String opcion) {
        PersonajeFactory fabrica = fabricasDisponibles.get(opcion);
        if (fabrica == null) {
            return null;
        }
        return fabrica.crearPersonaje();
    }
}